package com.yswl.cloudservice.account.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ExpUser {
    @ExcelProperty("手机号")
    private String phone;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("性别")
    private String genderName;
    @ExcelProperty("生日")
    private String birthday;
    @ExcelProperty("注册时间")
    private Date createTime;
}
