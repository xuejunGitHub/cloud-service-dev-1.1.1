package com.yswl.cloudservice.account.controller;

import com.yswl.cloudservice.account.entity.User;
import com.yswl.cloudservice.account.model.UserModel;
import com.yswl.cloudservice.account.model.UserQuery;
import com.yswl.cloudservice.account.service.UserService;
import com.yswl.cloudservice.common.core.base.BatchUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 添加用户
     */
    @PostMapping("addUser")
    public Object addUser(@RequestBody @Validated(User.Add.class) User user) {
        return userService.addUser(user);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("updateUser")
    public Object updateUser(@RequestBody @Validated(User.Upd.class) User user) {
        return userService.updateUser(user);
    }

    /**
     * 批量添加用户
     */
    @PostMapping("addUserBatch")
    public Object addUserBatch(@RequestBody BatchUpdateModel<User> bu) {
        return userService.addUserBatch(bu);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("searchUserInfo")
    public Object searchUserInfo(UserQuery q) {
        return userService.searchUserInfo(q);
    }

    /**
     * 用户列表
     */
    @GetMapping("getUserList")
    public Object getUserList(UserQuery q) {
        return userService.getUserList(q);
    }

    /**
     * 批量获取用户
     */
    @GetMapping("getUserBatch")
    public Object getUserBatch(UserQuery q) {
        return userService.getUserBatch(q);
    }

    /**
     * 根据id获取手机号
     */
    @GetMapping("getById")
    public Object getById(String id) {
        return userService.getById(id);
    }

    /**
     * 修改手机号
     */
    @PostMapping("modifyPhone")
    public Object modifyPhone(@RequestBody UserModel user) {
        return userService.modifyPhone(user);
    }

    /**
     * 更新用户状态
     */
    @PostMapping("updateStatus")
    public Object updateStatus(@RequestBody User user) {
        return userService.updateStatus(user);
    }

    /**
     * 导出
     */
    @GetMapping("exportUser")
    public void exportUser(HttpServletResponse response, UserQuery q) {
        userService.exportUser(response, q);
    }

    /**
     * 更新用户微信信息
     */
    @PostMapping("updateUserWxInfo")
    public Object updateUserWxInfo(@RequestBody User user) {
        return userService.updateUserWxInfo(user);
    }
}
