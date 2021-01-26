package com.yswl.cloudservice.account.model.haier;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @projectName yswl-cloud-service
 * @since 2020/12/15-下午4:10
 */
@Data
public class HaierUserModel {

	  private Integer userId;
	  private String givenName;
	  private String nickname;
	  private String avatarUrl;
	  private String gender;
	  private String birthdate;
	  private String marriage;
	  private String education;
	  private String extraPhone;
	  private String familyNum;
	  private String income;
	  private String height;
	  private String weight;
	  private String signature;
	  private HaierUserAddressModel address;

	  private String openId;
	  private String phone;
	  private Integer proId;
}
