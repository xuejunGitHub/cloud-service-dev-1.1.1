package com.yswl.cloudservice.account.sync.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("smart_user")
public class YanxuanUser {
    private String microServiceId;
    private String telephone;
    private String nickname;
    private String gender;
    private String avatar;

}
