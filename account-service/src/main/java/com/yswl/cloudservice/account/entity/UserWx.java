package com.yswl.cloudservice.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author wangfengchen
 */
@Data
public class UserWx {
    @TableId(type = IdType.AUTO)
    private String id;
    private String userId;
    private String unionId;
    private String openId;
    private String nickname;
}
