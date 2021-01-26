package com.yswl.cloudservice.account.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yswl.cloudservice.common.web.util.AddressUtils;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;

@Data
public class UserAddress {
    @TableId(type = IdType.ASSIGN_ID)
    @NotNull(groups = Upd.class, message = "用户地址id不能为空")
    private String id;
    @NotNull(message = "用户id不能为空")
    private String userId;
    private String consigneeName;
    private String consigneePhone;
    @NotNull(message = "省编码不能为空")
    @Min(value = 1)
    private Integer province;
    @NotNull(message = "省不能为空")
    private String provinceName;
    @NotNull(message = "市编码不能为空")
    @Min(value = 1)
    private Integer city;
    @NotNull(message = "市不能为空")
    private String cityName;
    @NotNull(message = "地区编码不能为空")
    @Min(value = 1)
    private Integer district;
    @NotNull(message = "地区不能为空")
    private String districtName;
    @NotNull(message = "街道编码不能为空")
    @Min(value = 1)
    private Integer street;
    @NotNull(message = "街道不能为空")
    private String streetName;
    @NotNull(message = "详细地址不能为空")
    private String address;
    private Integer isDefault;
    private String longitude;
    private String latitude;
    private Integer isDelete;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(exist = false)
    private String fullAddress;
    @TableField(exist = false)
    private String streetAddress;

    public String getFullAddress() {
        return AddressUtils.getFullAddress(provinceName, cityName, districtName, streetName, address);
    }

    public String getStreetAddress() {
        return AddressUtils.getStreetAddress(provinceName, cityName, districtName, streetName);
    }

    public interface Add extends Default {}
    public interface Upd extends Default {}
}
