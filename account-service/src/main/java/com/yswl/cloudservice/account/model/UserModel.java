package com.yswl.cloudservice.account.model;

import lombok.Data;

@Data
public class UserModel {
    private String id;
    private Integer proId;
    private String phone;
    private String verCode;
}
