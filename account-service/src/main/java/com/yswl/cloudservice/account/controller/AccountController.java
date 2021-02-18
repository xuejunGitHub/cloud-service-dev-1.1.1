package com.yswl.cloudservice.account.controller;

import com.yswl.cloudservice.account.api.wx.MiniAppApi;
import com.yswl.cloudservice.account.service.UserService;
import com.yswl.cloudservice.account.model.AccountModel;
import com.yswl.cloudservice.account.model.UserQuery;
import com.yswl.cloudservice.common.web.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * @author wangfengchen
 */
@Slf4j
@RestController
@RequestMapping("/")
public class AccountController {//Hello
    //Hello2
    //Hello3

    @Autowired
    private UserService userService;
    @Autowired
    private MiniAppApi miniAppApi;


    @PostMapping("login")
    public Object login(@RequestBody AccountModel any) {
        return userService.login(any);
    }

    /**
     * 微信小程序CODE换SESSION_KEY
     */
    @GetMapping("webAccess")
    public Object webAccess(AccountModel any) {
        return miniAppApi.getWebAccess(any.getCode(), any.getProId());
    }

    @GetMapping("getObj")
    public Object getObj(UserQuery q) {
        log.info("uq {}", JsonUtils.toJson(q));
        return null;
    }
}
