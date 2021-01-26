package com.yswl.cloudservice.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yswl.cloudservice.account.entity.UserAccount;
import com.yswl.cloudservice.account.entity.UserPointsLog;
import com.yswl.cloudservice.account.entity.RefundMoneyLog;
import com.yswl.cloudservice.account.model.UserQuery;

/**
 * 用户账户表(UserAccount)表服务接口
 *
 * @author wangfengchen
 * @since 2020-11-05 09:11:27
 */
public interface UserAccountService extends IService<UserAccount> {

    boolean updatePoints(UserPointsLog upl);

    UserAccount getUserAccount(UserQuery q);

    boolean updateRefundMoney(RefundMoneyLog log);
}