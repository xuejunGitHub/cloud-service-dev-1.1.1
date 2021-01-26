package com.yswl.cloudservice.account.controller;

import com.yswl.cloudservice.account.entity.UserPointsLog;
import com.yswl.cloudservice.account.entity.RefundMoneyLog;
import com.yswl.cloudservice.account.model.UserQuery;
import com.yswl.cloudservice.account.service.UserAccountService;
import com.yswl.cloudservice.account.service.UserPointsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户账户
 */
@RestController
@RequestMapping("userAccount")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserPointsLogService userPointsLogService;


    /**
     * 更新用户积分数
     */
    @PostMapping("updatePoints")
    public Object updatePoints(@RequestBody UserPointsLog upl) {
        return userAccountService.updatePoints(upl);
    }

    /**
     * 获取我的账户信息
     */
    @GetMapping("getUserAccount")
    public Object getUserAccount(UserQuery q) {
        return userAccountService.getUserAccount(q);
    }

    /**
     * 获取积分记录
     */
    @GetMapping("getUserPointsLogList")
    public Object getUserPointsLogList(UserQuery q) {
        return userPointsLogService.getUserPointsLogList(q);
    }

    /**
     * 更新用户钱包金额
     */
    @PostMapping("updateRefundMoney")
    public Object updateRefundMoney(@RequestBody RefundMoneyLog log) {
        return userAccountService.updateRefundMoney(log);
    }
}
