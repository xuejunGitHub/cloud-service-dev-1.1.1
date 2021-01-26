package com.yswl.cloudservice.account.sync.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ys_users")
public class WashUser {
    private String platformUserId;
    private String mobile;
    private String username;
    private String avatar;
    private Integer sex;
    private String birthday;
}
