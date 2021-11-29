package com.danmi.sms.controller;


import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Authentication;
import com.danmi.sms.entity.Sign;
import com.danmi.sms.entity.Sign;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.SignRequest;
import com.danmi.sms.enums.SignApproveStatusEnum;
import com.danmi.sms.enums.SignStatusEnum;
import com.danmi.sms.service.ISignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**

 签名管理
 * @author chunqiu
 * @since 2021-11-28
 */
@RestController
@RequestMapping("/sign")
@Api("签名管理")
public class SignController {

    @Autowired
    private ISignService signService;

    @PostMapping("")
    @ResponseBody
    @ApiOperation(value = "添加签名", notes = "添加签名")
    public Result<Object> addRole(@RequestBody Sign sign, HttpServletRequest request) {
        // 判断必须参数
        if (!StringUtils.hasLength(sign.getContent())) {
            return Result.fail("必传参数不能为空！");
        }

        Object userInfo = request.getSession().getAttribute("userInfo");
        User loginUser = new User();
        if (userInfo instanceof User) {
            loginUser = (User) userInfo;

            sign.setCa(loginUser.getCode()).setCt(LocalDateTime.now())
                    .setStatus(SignStatusEnum.USE.getStatus())
                    .setApproveStatus(SignApproveStatusEnum.UN_APPROVE.getStatus());

            boolean flag = signService.save(sign);
            if (flag) {
                return Result.success("添加成功！");
            } else {
                return Result.fail("添加失败！");
            }
        }

        return Result.fail("用户未登录！");

    }

    @GetMapping("list")
    @ResponseBody
    @ApiOperation(value = "签名列表", notes = "签名列表")
    public Result<Object> list(SignRequest sign) {

        PageDTO<Sign> rolePageDTO = signService.listSignPage(sign);
        return Result.success(rolePageDTO.getRecords(), rolePageDTO.getTotal());
    }

    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "根据id获取签名", notes = "根据id获取签名")
    public Result<Object> getById(@PathVariable(value = "id") Integer id) {
        Sign sign = signService.getById(id);
        return Result.success(sign);
    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    @ApiOperation(value = "根据id批量删除签名", notes = "根据id批量删除签名")
    @ApiImplicitParams({@ApiImplicitParam(name = "ids", value = "删除的签名id，多个id逗号分割", dataTypeClass = String.class)})
    public Result<Object> deleteRoleByIds(@PathVariable("ids") String ids) {
        List<String> cids = Arrays.asList(ids.split(","));
        signService.removeByIds(cids);
        return Result.success("删除成功！");
    }

    @PutMapping("")
    @ResponseBody
    @ApiOperation(value = "根据id更新签名", notes = "根据id更新签名")
    public Result<Object> updateRoleById(@RequestBody Sign sign) {
        if (ObjectUtils.isEmpty(sign.getId())) {
            return Result.fail("必传参数不能为空！");
        }
        boolean flag = signService.updateById(sign);
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
    public Result<Object> approve(@RequestBody Sign sign) {
        if (ObjectUtils.isEmpty(sign.getId()) || ObjectUtils.isEmpty(sign.getApproveStatus())) {
            return Result.fail("必传参数不能为空！");
        }
        boolean flag = signService.updateById(sign);
        if (flag) {
            return Result.success("审核成功！");
        } else {
            return Result.fail("审核失败！");
        }
    }
}
