package com.danmi.sms.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Authentication;
import com.danmi.sms.entity.Role;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.AuthenticationRequest;
import com.danmi.sms.enums.AuthenticationApproveStatusEnum;
import com.danmi.sms.service.IAuthenticationService;
import com.danmi.sms.service.IRoleService;
import com.danmi.sms.utils.FilePathUtils;
import com.danmi.sms.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 认证管理
 *
 * @author chunqiu
 * @since 2021-11-28
 */
@RestController
@RequestMapping("/authentication")
@Api("认证管理")
public class AuthenticationController {


    @Autowired
    private IAuthenticationService authenticationService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private UserUtils userUtils;
    @Value("${authent.file.path}")
    private String uploadPath;


    @PostMapping(value = "certify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @ApiOperation(value = "企业认证", notes = "企业认证")
    public Result<Object> addAuthentication(Authentication authentication, HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {

        User loginUser = userUtils.getUser();
        if (ObjectUtils.isEmpty(loginUser)) {
            return Result.fail("用户未登录！");
        }

        //         判断必须参数
        if (!StringUtils.hasLength(authentication.getCompany()) || !StringUtils.hasLength(authentication.getLegalPerson()) || file.isEmpty()) {
            return Result.fail("必传参数不能为空！");
        }

        if (file.isEmpty()) {
            return Result.fail("请上传文件！");
        }

        List<Authentication> authentications = authenticationService.list(Wrappers.<Authentication>lambdaQuery().eq(Authentication::getCa, loginUser.getCode()));
        if (!ObjectUtils.isEmpty(authentication) && authentications.size() >0) {
            return Result.fail("已经有了认证信息，如果被拒，请修改重新提交！");
        }

        String pathStr = FilePathUtils.getFilePath();

        String originalFilename = file.getOriginalFilename();
        int index = originalFilename.lastIndexOf('.') + 1;//获取地址.的前面的数字，从0开始
        String type = originalFilename.substring(index);//从地址.开始截取后缀

//        jpg/jpeg/png/gif
        if (!("jpg".equals(type) || "jpeg".equals(type) || "png".equals(type) || "gif".equals(type))) {
            return Result.fail("文件格式不正确，支持jpg/jpeg/png/gif！");
        }

        long size = file.getSize();
        if (size / 1024 / 1024 > 10) {
            return Result.fail("文件大小不能超过10M！");
        }

        //生成新文件名字
        String newFileName = System.currentTimeMillis() + "." + type;
        // 封装上传文件位置的全路径
        File targetFile = new File(pathStr, newFileName);
        //把本地文件上传到封装上传文件位置的全路径
        file.transferTo(targetFile.getAbsoluteFile());


        authentication.setCa(loginUser.getCode())
                .setCt(LocalDateTime.now())
                .setCertification(newFileName)
                .setCertification(uploadPath + "/" + newFileName)
                .setApproveStatus(AuthenticationApproveStatusEnum.UN_APPROVE.getStatus());

        boolean flag = authenticationService.save(authentication);
        if (flag) {
            return Result.success(authentication);
        } else {
            return Result.fail("添加失败！");
        }


    }

    /**
     * 系统管理员查看认证列表
     *
     * @param authentication
     * @param request
     * @return
     */
    @GetMapping("list")
    @ResponseBody
    @ApiOperation(value = "系统管理员查看认证列表", notes = "系统管理员查看认证列表")
    public Result<Object> list(AuthenticationRequest authentication, HttpServletRequest request) {

        Object userInfo = request.getSession().getAttribute("userInfo");

        if (!(userInfo instanceof User)) {
            return Result.success("您尚未登录！");
        }
        User loginUser = (User) userInfo;
        Role role = roleService.getById(loginUser.getRoleId());
        Boolean isSuperAdmin = "system_admin".equals(role.getCode());

        if (!isSuperAdmin) {
            return Result.success("您没有权限查看！");
        }

        PageDTO<Authentication> rolePageDTO = authenticationService.listAuthenticationPage(authentication);
        return Result.success(rolePageDTO.getRecords(), rolePageDTO.getTotal());
    }

    /**
     * 当前登录人的个人认证详情
     *
     * @param authentication
     * @param request
     * @return
     */
    @GetMapping("get-by-single")
    @ResponseBody
    @ApiOperation(value = "当前登录人的个人认证详情", notes = "当前登录人的个人认证详情")
    public Result<Object> getBySingle(AuthenticationRequest authentication, HttpServletRequest request) {

        Object userInfo = request.getSession().getAttribute("userInfo");
        User loginUser = (User) userInfo;
        if (!(userInfo instanceof User)) {
            return Result.success("您尚未登录！");
        }

        Authentication one = authenticationService.getOne(Wrappers.<Authentication>lambdaQuery().eq(Authentication::getCa, loginUser.getCode()));

        if (ObjectUtils.isEmpty(one)) {
            return Result.fail("没有查到您的认证信息");
        } else {
            return Result.success(authentication);
        }
    }

    /**
     * 管理员： 根据id获取认证
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "根据id获取认证", notes = "根据id获取认证")
    public Result<Object> getById(@PathVariable(value = "id") Integer id, HttpServletRequest request) {

        Object userInfo = request.getSession().getAttribute("userInfo");

        if (!(userInfo instanceof User)) {
            return Result.success("您尚未登录！");
        }
        User loginUser = (User) userInfo;
        Role role = roleService.getById(loginUser.getRoleId());
        Boolean isSuperAdmin = "system_admin".equals(role.getCode());

        if (!isSuperAdmin) {
            return Result.success("您没有权限查看！");
        }

        Authentication authentication = authenticationService.getById(id);
        return Result.success(authentication);
    }

    /**
     * 管理员： 根据id批量删除认证
     *
     * @return
     */
    @DeleteMapping("/{ids}")
    @ResponseBody
    @ApiOperation(value = "根据id批量删除认证", notes = "根据id批量删除认证")
    @ApiImplicitParams({@ApiImplicitParam(name = "ids", value = "删除的签名id，多个id逗号分割", dataTypeClass = String.class)})
    public Result<Object> deleteAuthenticationByIds(@PathVariable("ids") String ids, HttpServletRequest request) {

        Object userInfo = request.getSession().getAttribute("userInfo");

        if (!(userInfo instanceof User)) {
            return Result.success("您尚未登录！");
        }
        User loginUser = (User) userInfo;
        Role role = roleService.getById(loginUser.getRoleId());
        Boolean isSuperAdmin = "system_admin".equals(role.getCode());

        if (!isSuperAdmin) {
            return Result.success("您没有权限删除！");
        }

        List<String> cids = Arrays.asList(ids.split(","));
        authenticationService.removeByIds(cids);
        return Result.success("删除成功！");
    }


    @PutMapping("")
    @ResponseBody
    @ApiOperation(value = "根据id更新认证", notes = "根据id更新认证")
    public Result<Object> updateAuthenticationById(@RequestBody Authentication authentication, HttpServletRequest request) {

        Object userInfo = request.getSession().getAttribute("userInfo");

        if (!(userInfo instanceof User)) {
            return Result.success("您尚未登录！");
        }
        User loginUser = (User) userInfo;
        if (ObjectUtils.isEmpty(authentication.getId())) {
            return Result.fail("必传参数不能为空！");
        }
        boolean flag = authenticationService.updateById(authentication);
        if (flag) {
            return Result.success("修改成功！");
        } else {
            return Result.fail("修改失败！");
        }
    }

    // @ todo 权限
    @PostMapping("approve")
    @ResponseBody
    @ApiOperation(value = "审核", notes = "审核")
    public Result<Object> approve(@RequestBody Authentication authentication, HttpServletRequest request) {

        Object userInfo = request.getSession().getAttribute("userInfo");

        if (!(userInfo instanceof User)) {
            return Result.success("您尚未登录！");
        }
        User loginUser = (User) userInfo;
        Role role = roleService.getById(loginUser.getRoleId());
        Boolean isSuperAdmin = "system_admin".equals(role.getCode());

        if (!isSuperAdmin) {
            return Result.success("您没有权限查看！");
        }

        if (ObjectUtils.isEmpty(authentication.getId()) || ObjectUtils.isEmpty(authentication.getApproveStatus())) {
            return Result.fail("必传参数不能为空！");
        }
        boolean flag = authenticationService.updateById(authentication);
        if (flag) {
            return Result.success("审核成功！");
        } else {
            return Result.fail("审核失败！");
        }
    }
}
