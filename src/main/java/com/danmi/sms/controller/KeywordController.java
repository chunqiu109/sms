package com.danmi.sms.controller;


import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Keyword;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.KeywordRequest;
import com.danmi.sms.enums.KeywordApproveStatusEnum;
import com.danmi.sms.enums.KeywordStatusEnum;
import com.danmi.sms.service.IKeywordService;
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
 *
 *  关键词
 *
 *
 * @author chunqiu
 * @since 2021-11-29
 */
@RestController
@RequestMapping("/keyword")
@Api("关键词管理")
public class KeywordController {
    @Autowired
    private IKeywordService keywordService;

    /**
     * 添加关键词
     * @param keyword
     * @param request
     * @return
     */
    @PostMapping("")
    @ResponseBody
    @ApiOperation(value = "添加关键词", notes = "添加关键词")
    public Result<Object> addRole(@RequestBody Keyword keyword, HttpServletRequest request) {
        // 判断必须参数
        if (!StringUtils.hasLength(keyword.getKeyword()) || !StringUtils.hasLength(keyword.getReplyContent())) {
            return Result.fail("必传参数不能为空！");
        }

        Object userInfo = request.getSession().getAttribute("userInfo");
        User loginUser = new User();
        if (userInfo instanceof User) {
            loginUser = (User) userInfo;

            keyword.setCa(loginUser.getCode()).setCt(LocalDateTime.now())
                    .setStatus(KeywordStatusEnum.USE.getStatus())
                    .setApproveStatus(KeywordApproveStatusEnum.UN_APPROVE.getStatus());

            boolean flag = keywordService.save(keyword);
            if (flag) {
                return Result.success("添加成功！");
            } else {
                return Result.fail("添加失败！");
            }
        }

        return Result.fail("用户未登录！");

    }

    /**
     * 关键词列表
     * @return
     */
    @GetMapping("list")
    @ResponseBody
    @ApiOperation(value = "关键词列表", notes = "关键词列表")
    public Result<Object> list(KeywordRequest keyword, HttpServletRequest request) {

        Object userInfo = request.getSession().getAttribute("userInfo");
        User loginUser = new User();
        if (!(userInfo instanceof User)) {
            return Result.success("您尚未登录！");
        }



        PageDTO<Keyword> rolePageDTO = keywordService.listKeywordPage(keyword, loginUser);
        return Result.success(rolePageDTO.getRecords(), rolePageDTO.getTotal());
    }

    /**
     * 根据id获取关键词
     * @return
     */
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "根据id获取关键词", notes = "根据id获取关键词")
    public Result<Object> getById(@PathVariable(value = "id") Integer id) {
        Keyword keyword = keywordService.getById(id);
        return Result.success(keyword);
    }

    /**
     * 根据id批量删除关键词
     * @return
     */
    @DeleteMapping("/{ids}")
    @ResponseBody
    @ApiOperation(value = "根据id批量删除关键词", notes = "根据id批量删除关键词")
    @ApiImplicitParams({@ApiImplicitParam(name = "ids", value = "删除的关键词id，多个id逗号分割", dataTypeClass = String.class)})
    public Result<Object> deleteRoleByIds(@PathVariable("ids") String ids) {
        List<String> cids = Arrays.asList(ids.split(","));
        keywordService.removeByIds(cids);
        return Result.success("删除成功！");
    }

    /**
     * 根据id更新关键词
     * @return
     */
    @PutMapping("")
    @ResponseBody
    @ApiOperation(value = "根据id更新关键词", notes = "根据id更新关键词")
    public Result<Object> updateRoleById(@RequestBody Keyword keyword) {
        if (ObjectUtils.isEmpty(keyword.getId())) {
            return Result.fail("必传参数不能为空！");
        }
        boolean flag = keywordService.updateById(keyword);
        if (flag) {
            return Result.success("修改成功！");
        } else {
            return Result.fail("修改失败！");
        }
    }

    /**
     * 审核
     * @return
     */
    // @ todo 权限
    @PostMapping("approve")
    @ResponseBody
    @ApiOperation(value = "审核", notes = "审核")
    public Result<Object> approve(@RequestBody Keyword keyword) {
        if (ObjectUtils.isEmpty(keyword.getId()) || ObjectUtils.isEmpty(keyword.getApproveStatus())) {
            return Result.fail("必传参数不能为空！");
        }
        boolean flag = keywordService.updateById(keyword);
        if (flag) {
            return Result.success("审核成功！");
        } else {
            return Result.fail("审核失败！");
        }
    }
}
