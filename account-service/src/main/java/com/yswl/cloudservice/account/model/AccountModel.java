package com.yswl.cloudservice.account.model;

import com.yswl.cloudservice.common.core.constants.ConfigConstants;
import lombok.Data;

/**
 * @author wangfengchen
 */
@Data
public class AccountModel {
    /**
     * 项目id
     */
    private Integer proId;
    private String phone;
    private String userId;
    private String verCode;
    /**
     * @see ConfigConstants
     */
    private String code;
    private String loginType;
    private String sessionKey;
    private String encryptedData;
    private String ivStr;
    private String areaCode;
    private String unionId;
    /**
     * @see ConfigConstants
     */
    private Integer thirdType;
    /**
     * openid
     */
    private String openId;
    private String nickname;
    /**
     * 来源
     */
    private Integer source;
}
