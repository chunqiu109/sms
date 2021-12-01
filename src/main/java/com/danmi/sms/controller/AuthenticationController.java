package com.danmi.sms.controller;


import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Authentication;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.AuthenticationRequest;
import com.danmi.sms.enums.AuthenticationApproveStatusEnum;
import com.danmi.sms.service.IAuthenticationService;
import com.danmi.sms.utils.FilePathUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 *
 *  认证管理
 *
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
    @Value("${authent.file.path}")
    private String uploadPath;


    @PostMapping("certify")
    @ResponseBody
    @ApiOperation(value = "企业认证", notes = "企业认证")
    public Result<Object> addRole(Authentication authentication, HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
//         判断必须参数
        if (!StringUtils.hasLength(authentication.getCompany()) || !StringUtils.hasLength(authentication.getCertification())
                || !StringUtils.hasLength(authentication.getLegalPerson()) || file.isEmpty()) {
            return Result.fail("必传参数不能为空！");
        }

        if (file.isEmpty()) {
            return Result.fail("请上传文件！");
        }

        String pathStr = FilePathUtils.getFilePath();

        String originalFilename = file.getOriginalFilename();
        int index=originalFilename.lastIndexOf('.')+1;//获取地址.的前面的数字，从0开始
        String type=originalFilename.substring(index);//从地址.开始截取后缀

//        jpg/jpeg/png/gif
        if (!("jpg".equals(type)||"jpeg".equals(type)||"png".equals(type)||"gif".equals(type))) {
            return Result.fail("文件格式不正确，支持jpg/jpeg/png/gif！");
        }

        long size = file.getSize();
        if (size/1024/1024>10) {
            return Result.fail("文件大小不能超过10M！");
        }

        //生成新文件名字
        String newFileName = System.currentTimeMillis() + "." + type;
        // 封装上传文件位置的全路径en
        File targetFile  = new File(pathStr,newFileName);
        //把本地文件上传到封装上传文件位置的全路径
        file.transferTo(targetFile);

        Object userInfo = request.getSession().getAttribute("userInfo");
        User loginUser = new User();
        if (userInfo instanceof User) {
            loginUser = (User) userInfo;

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
        return Result.fail("用户未登录！");
    }

    // @ todo 权限!!!!!
    @GetMapping("list")
    @ResponseBody
    @ApiOperation(value = "认证列表", notes = "认证列表")
    public Result<Object> list(AuthenticationRequest authentication) {

        PageDTO<Authentication> rolePageDTO = authenticationService.listAuthenticationPage(authentication);
        return Result.success(rolePageDTO.getRecords(), rolePageDTO.getTotal());
    }

    // @ todo 权限
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "根据id获取认证", notes = "根据id获取认证")
    public Result<Object> getById(@PathVariable(value = "id") Integer id) {
        Authentication authentication = authenticationService.getById(id);
        return Result.success(authentication);
    }

    // @ todo 权限
    @DeleteMapping("/{ids}")
    @ResponseBody
    @ApiOperation(value = "根据id批量删除认证", notes = "根据id批量删除认证")
    @ApiImplicitParams({@ApiImplicitParam(name = "ids", value = "删除的签名id，多个id逗号分割", dataTypeClass = String.class)})
    public Result<Object> deleteRoleByIds(@PathVariable("ids") String ids) {
        List<String> cids = Arrays.asList(ids.split(","));
        authenticationService.removeByIds(cids);
        return Result.success("删除成功！");
    }

    // @ todo 权限
    @PutMapping("")
    @ResponseBody
    @ApiOperation(value = "根据id更新认证", notes = "根据id更新认证")
    public Result<Object> updateRoleById(@RequestBody Authentication authentication) {
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
    public Result<Object> approve(@RequestBody Authentication authentication) {
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
