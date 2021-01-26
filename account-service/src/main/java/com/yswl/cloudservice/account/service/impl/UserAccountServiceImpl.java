package com.yswl.cloudservice.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yswl.cloudservice.account.entity.UserAccount;
import com.yswl.cloudservice.account.entity.UserPointsLog;
import com.yswl.cloudservice.account.entity.RefundMoneyLog;
import com.yswl.cloudservice.account.mapper.UserAccountMapper;
import com.yswl.cloudservice.account.model.UserQuery;
import com.yswl.cloudservice.account.service.UserAccountService;
import com.yswl.cloudservice.account.service.UserPointsLogService;
import com.yswl.cloudservice.account.service.RefundMoneyLogService;
import com.yswl.cloudservice.common.core.constants.CacheKeyConstants;
import com.yswl.cloudservice.common.core.exps.BusException;
import com.yswl.cloudservice.common.web.redis.DistributedLock;
import com.yswl.cloudservice.common.web.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户账户表(UserAccount)表服务实现类
 *
 * @author wangfengchen
 * @since 2020-11-05 09:11:27
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

    @Autowired
    private UserPointsLogService userPointsLogService;
    @Autowired
    private RefundMoneyLogService refundMoneyLogService;


    @Override
    @DistributedLock(lockName = CacheKeyConstants.USER_UPDATE_POINTS_LOCK, params = {"userId"})
    public boolean updatePoints(UserPointsLog upl) {
        AssertUtils.hasText(upl.getUserId(), "用户id不能为空");
        AssertUtils.notNull(upl.getPoints(), "积分不能为空");
        LambdaQueryWrapper<UserAccount> q = new LambdaQueryWrapper<>();
        q.eq(UserAccount::getUserId, upl.getUserId());
        UserAccount ua = getOne(q, false);
        if (ua == null) {
            if (upl.getPoints() < 0) {
                throw new BusException("积分不足");
            }
            ua = new UserAccount();
            ua.setUserId(upl.getUserId());
            ua.setPoints(upl.getPoints());
            ua.setTotalPoints(upl.getPoints());
            save(ua);
        } else {
            if (upl.getPoints() < 0 && Math.abs(upl.getPoints()) > ua.getPoints()) {
                throw new BusException("积分不足");
            }
            //更新积分数
            UpdateWrapper<UserAccount> upd = new UpdateWrapper<>();
            upd.eq("id", ua.getId());
            if (upl.getPoints() > 0) {
                //增加
                upd.setSql("total_points = total_points + " + upl.getPoints());
            } else if (upl.getPoints() < 0) {
                //消耗
                upd.setSql("locking_points = locking_points + " + ( - upl.getPoints()));
            }
            upd.setSql("points = points + " + upl.getPoints());
            update(upd);
        }
        if (upl.getPoints() > 0) {
            upl.setType(0);
        } else {
            upl.setType(1);
        }
        //增加日志
        userPointsLogService.save(upl);
        return true;
    }

    @Override
    public UserAccount getUserAccount(UserQuery q) {
        LambdaQueryWrapper<UserAccount> qw = new LambdaQueryWrapper<>();
        qw.eq(UserAccount::getUserId, q.getUserId());
        return getOne(qw, false);
    }

    @Override
    @DistributedLock(lockName = CacheKeyConstants.USER_UPDATE_REFUND_MONEY_LOCK, params = {"userId"})
    public boolean updateRefundMoney(RefundMoneyLog log) {
        AssertUtils.hasText(log.getUserId(), "用户id不能为空");
        AssertUtils.notNull(log.getMoney(), "金额不能为空");
        LambdaQueryWrapper<UserAccount> q = new LambdaQueryWrapper<>();
        q.eq(UserAccount::getUserId, log.getUserId());
        UserAccount ua = getOne(q, false);
        if (ua == null) {
            ua = new UserAccount();
            ua.setUserId(log.getUserId());
            save(ua);
        }
        UpdateWrapper<UserAccount> upd = new UpdateWrapper<>();
        upd.eq("user_id", log.getUserId());
        switch (log.getType()) {
            case 4:
            case 6:
                //发起提现
                //拒绝提现
                upd.setSql("refund_money = refund_money + " + log.getMoney());
                upd.setSql("refund_withdrawal_money = refund_withdrawal_money - " + log.getMoney());
                break;
            case 5:
                //提现成功
                upd.setSql("refund_withdrawal_money = refund_withdrawal_money - " + log.getMoney());
                upd.setSql("refund_withdrawn_money = refund_withdrawn_money + " + log.getMoney());
                break;
            case 1:
                //分佣未入账
                upd.setSql("refunding_money = refunding_money + " + log.getMoney());
                break;
            case 2:
                //分佣已入账
                upd.setSql("refunding_money = refunding_money - " + log.getMoney());
                upd.setSql("refund_money = refund_money + " + log.getMoney());
                break;
            case 3:
                //分佣退账
                upd.setSql("refunding_money = refunding_money - " + log.getMoney());
                break;
        }
        boolean r = update(upd);
        if (r) {
            refundMoneyLogService.save(log);
        }
        return r;
    }

}