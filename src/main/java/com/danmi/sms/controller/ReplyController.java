package com.danmi.sms.controller;


import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Reply;
import com.danmi.sms.entity.Role;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.ReplyRequest;
import com.danmi.sms.entity.request.RoleRequest;
import com.danmi.sms.service.IReplyService;
import com.danmi.sms.utils.UserUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chunqiu
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/reply")
public class ReplyController {
    @Autowired
    private IReplyService replyService;
    @Autowired
    private UserUtils userUtils;

    /**
     *
     * @param request
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "获取角色列表（分页）", notes = "获取角色列表（分页）")
    public Result<Object> getRoleList(ReplyRequest request) {

        Object userInfo = userUtils.getUser();

        if (userInfo == null) {
            return Result.success("您尚未登录！");
        }
        User loginUser = (User) userInfo;
        PageDTO<Reply> replyPageDTO = replyService.listReplyPage(request, loginUser);
        return Result.success(replyPageDTO.getRecords(), replyPageDTO.getTotal());
    }

}
