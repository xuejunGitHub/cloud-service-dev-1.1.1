package com.yswl.cloudservice.account.entity.haier;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yswl.cloudservice.account.model.haier.HaierUserAddressModel;
import lombok.Data;

/**
 * @projectName yswl-cloud-service
 * @since 2020/12/16-上午9:59
 */
@Data
public class HaierUser {

	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	private Integer userId;
	private String username;
	private String mobile;
	private String email;
	private String givenName;
	private String nickname;
	private String avatarUrl;
	private String gender;
	private String birthday;
	private String marriage;
	private String education;
	private String extraPhone;
	private String familyNum;
	private String income;
	private String height;
	private String weight;
	private String signature;
	private String regClientId;

	@TableField(exist = false)
	private HaierUserAddressModel address;

}
