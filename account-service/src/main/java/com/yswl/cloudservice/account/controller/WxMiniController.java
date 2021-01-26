package com.yswl.cloudservice.account.controller;

import com.yswl.cloudservice.account.model.WxaCodeModel;
import com.yswl.cloudservice.account.service.WxMiniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("wxmini")
public class WxMiniController {

    @Autowired
    private WxMiniService wxMiniService;


    /**
     * 获取小程序二维码
     */
    @PostMapping("getWxaCodeUnLimit")
    public Object getWxaCodeUnLimit(@RequestBody WxaCodeModel model) {
        return wxMiniService.getWxaCodeUnLimit(model);
    }
}
