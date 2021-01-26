package com.yswl.cloudservice.account.sync.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yswl.cloudservice.account.entity.User;
import com.yswl.cloudservice.account.sync.entity.WashUser;
import com.yswl.cloudservice.account.sync.entity.YanxuanUser;
import com.yswl.cloudservice.account.sync.service.SyncService;
import com.yswl.cloudservice.account.sync.service.WashUserService;
import com.yswl.cloudservice.account.sync.service.YanxuanUserService;
import com.yswl.cloudservice.common.core.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class SyncServiceImpl implements SyncService {

    @Autowired
    private WashUserService washUserService;
    @Autowired
    private YanxuanUserService yanxuanUserService;

    @Override
    public void syncUserInfo(User user) {
        try {
            syncWashUserInfo(user);
            syncYanxuanUserInfo(user);
        } catch (Exception e) {
            //异常手动同步
            log.error(e.getMessage(), e);
        }
    }

    private void syncWashUserInfo(User user) {
        LambdaUpdateWrapper<WashUser> q = new LambdaUpdateWrapper<>();
        q.eq(WashUser::getPlatformUserId, user.getId());
        WashUser r = washUserService.getOne(q, false);
        if (r == null) {
            log.info("洗衣用户为空id={}", user.getId());
            return;
        }
        LambdaUpdateWrapper<WashUser> uw = new LambdaUpdateWrapper<>();
        uw.eq(WashUser::getPlatformUserId, user.getId());
        if (StringUtils.isNotBlank(user.getPhone()) && !Objects.equals(r.getMobile(), user.getPhone())) {
            uw.set(WashUser::getMobile, user.getPhone());
        }
        if (StringUtils.isNotBlank(user.getName()) && !Objects.equals(r.getUsername(), user.getName())) {
            uw.set(WashUser::getUsername, user.getName());
        }
        if (StringUtils.isNotBlank(user.getHeadUrl()) && !Objects.equals(r.getAvatar(), user.getHeadUrl())) {
            uw.set(WashUser::getAvatar, user.getHeadUrl());
        }
        if (user.getGender() != null && !Objects.equals(r.getSex(), user.getGender())) {
            uw.set(WashUser::getSex, user.getGender());
        }
        if (user.getBirthday() != null) {
            int y = DateUtils.ymdToStamp(user.getBirthday());
            if (!Objects.equals(String.valueOf(y), r.getBirthday())) {
                uw.set(WashUser::getBirthday, y);
            }
        }
        if (!uw.getParamNameValuePairs().isEmpty()) {
            washUserService.update(uw);
        }
    }

    private void syncYanxuanUserInfo(User user) {
        LambdaUpdateWrapper<YanxuanUser> q = new LambdaUpdateWrapper<>();
        q.eq(YanxuanUser::getMicroServiceId, user.getId());
        YanxuanUser r = yanxuanUserService.getOne(q, false);
        if (r == null) {
            log.info("严选用户为空id={}", user.getId());
            return;
        }
        LambdaUpdateWrapper<YanxuanUser> uw = new LambdaUpdateWrapper<>();
        uw.eq(YanxuanUser::getMicroServiceId, user.getId());
        if (StringUtils.isNotBlank(user.getPhone()) && !Objects.equals(r.getTelephone(), user.getPhone())) {
            uw.set(YanxuanUser::getTelephone, user.getPhone());
        }
        if (StringUtils.isNotBlank(user.getName()) && !Objects.equals(r.getNickname(), user.getName())) {
            uw.set(YanxuanUser::getNickname, user.getName());
        }
        if (StringUtils.isNotBlank(user.getHeadUrl()) && !Objects.equals(r.getAvatar(), user.getHeadUrl())) {
            uw.set(YanxuanUser::getAvatar, user.getHeadUrl());
        }
        if (user.getGender() != null) {
            uw.set(YanxuanUser::getGender, user.getGender() == 1 ? "male" : "female");
        }
        if (!uw.getParamNameValuePairs().isEmpty()) {
            yanxuanUserService.update(uw);
        }
    }
}
