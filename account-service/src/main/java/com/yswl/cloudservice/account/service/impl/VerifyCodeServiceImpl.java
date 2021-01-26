package com.yswl.cloudservice.account.service.impl;

import com.yswl.cloudservice.account.service.VerifyCodeService;
import com.yswl.cloudservice.client.notify.NotifyClient;
import com.yswl.cloudservice.client.notify.model.NotifyModel;
import com.yswl.cloudservice.common.core.util.MapUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    private NotifyClient notifyClient;

    @Override
    public int checkVerifyCode(Integer proId, String phone, String verifyCode) {
        val any = new NotifyModel();
        any.setProId(proId);
        any.setPhone(phone);
        any.setCode(verifyCode);
        Map r = notifyClient.verifySmsCode(any).successData();
        return MapUtils.getInt(r, "status", 0);
    }

}
