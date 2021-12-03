package com.danmi.sms.controller;


import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Template;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.TemplateRequest;
import com.danmi.sms.enums.TemplateApproveStatusEnum;
import com.danmi.sms.enums.TemplateStatusEnum;
import com.danmi.sms.service.ITemplateService;
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
 * 模板管理
 *
 * @author chunqiu
 * @since 2021-11-28
 */
@RestController
@RequestMapping("/template")
@Api("模板管理")
public class TemplateController {
    @Autowired
    private ITemplateService templateService;

    @PostMapping("")
    @ResponseBody
    @ApiOperation(value = "添加模板", notes = "添加模板")
    public Result<Object> addRole(@RequestBody Template template, HttpServletRequest request) {
        // 判断必须参数
        if (!StringUtils.hasLength(template.getContent()) || ObjectUtils.isEmpty(template.getSign())) {
            return Result.fail("必传参数不能为空！");
        }

        Object userInfo = request.getSession().getAttribute("userInfo");
        User loginUser;
        if (userInfo instanceof User) {
            loginUser = (User) userInfo;

            template.setCa(loginUser.getCode()).setCt(LocalDateTime.now()).setStatus(TemplateStatusEnum.USE.getStatus())
                    .setApproveStatus(TemplateApproveStatusEnum.UN_APPROVE.getStatus());
            boolean flag = templateService.save(template);
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
    @ApiOperation(value = "模板列表", notes = "模板列表")
    public Result<Object> list(TemplateRequest template, HttpServletRequest request) {

        Object userInfo = request.getSession().getAttribute("userInfo");

        if (!(userInfo instanceof User)) {
            return Result.success("您尚未登录！");
        }
        User loginUser = (User) userInfo;
        PageDTO<Template> rolePageDTO = templateService.listTemplatePage(template, loginUser);
        return Result.success(rolePageDTO.getRecords(), rolePageDTO.getTotal());
    }

    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "根据id获取模板", notes = "根据id获取模板")
    public Result<Object> getById(@PathVariable(value = "id") Integer id) {
        Template template = templateService.getById(id);
        return Result.success(template);
    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    @ApiOperation(value = "根据id批量删除模板", notes = "根据id批量删除模板")
    @ApiImplicitParams({@ApiImplicitParam(name = "ids", value = "删除的模板id，多个id逗号分割", dataTypeClass = String.class)})
    public Result<Object> deleteRoleByIds(@PathVariable("ids") String ids) {
        List<String> cids = Arrays.asList(ids.split(","));
        templateService.removeByIds(cids);
        return Result.success("删除成功！");
    }

    @PutMapping("")
    @ResponseBody
    @ApiOperation(value = "根据id更新模板", notes = "根据id更新模板")
    public Result<Object> updateRoleById(@RequestBody Template template) {
        if (ObjectUtils.isEmpty(template.getId())) {
            return Result.fail("必传参数不能为空！");
        }
        boolean flag = templateService.updateById(template);
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
    public Result<Object> approve(@RequestBody Template template) {
        if (ObjectUtils.isEmpty(template.getId()) || ObjectUtils.isEmpty(template.getApproveStatus())) {
            return Result.fail("必传参数不能为空！");
        }
        boolean flag = templateService.updateById(template);
        if (flag) {
            return Result.success("审核成功！");
        } else {
            return Result.fail("审核失败！");
        }
    }
}
