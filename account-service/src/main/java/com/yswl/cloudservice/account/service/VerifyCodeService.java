package com.yswl.cloudservice.account.service;


public interface VerifyCodeService {

    /**
     * 检查验证码是否正确
     * @return 0 失效 1 正确 2 错误
     */
    int checkVerifyCode(Integer proId, String phone, String verifyCode);

}
