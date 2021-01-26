package com.yswl.cloudservice.account.service.impl;

import com.yswl.cloudservice.account.api.wx.MiniAppApi;
import com.yswl.cloudservice.account.model.WxaCodeModel;
import com.yswl.cloudservice.account.service.WxMiniService;
import com.yswl.cloudservice.common.core.util.MapUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Base64;

@Service
public class WxMiniServiceImpl implements WxMiniService {

    @Autowired
    private MiniAppApi miniAppApi;


    @SneakyThrows
    @Override
    public Object getWxaCodeUnLimit(WxaCodeModel model) {
        InputStream is = miniAppApi.getWxaCodeUnLimit(model);
        byte[] bytes = IOUtils.toByteArray(is);
        String data = Base64.getEncoder().encodeToString(bytes);
        return MapUtils.create("data", "data:image/png;base64," + data);
    }
}
