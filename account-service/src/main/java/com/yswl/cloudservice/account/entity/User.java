package com.yswl.cloudservice.account.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yswl.cloudservice.common.core.util.RegexpUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.util.Date;

/**
 * @author wangfengchen
 */
@Data
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    @NotNull(groups = Upd.class, message = "用户id不能为空")
    private String id;
    @NotNull(groups = Add.class, message = "手机号不能为空")
    @Pattern(groups = Add.class, regexp = RegexpUtils.REGEX_MOBILE, message = "手机号格式不正确")
    private String phone;
    private String name;
    private Integer gender;
    private String headUrl;
    private String email;
    private String realName;
    private String idNumber;
    private Integer source;
    private String birthday;
    private Integer status;
    private Integer haierUserId;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    public interface Add extends Default {}
    public interface Upd extends Default {}
}
