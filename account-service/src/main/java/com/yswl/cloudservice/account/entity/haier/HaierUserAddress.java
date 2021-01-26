package com.yswl.cloudservice.account.entity.haier;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @projectName yswl-cloud-service
 * @since 2020/12/16-上午10:00
 */
@Data
public class HaierUserAddress {

	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	private Integer haierUserId;
	private String province;
	private String provinceId;
	private String city;
	private String cityId;
	private String district;
	private String districtId;
	private String line1;
	private String postcode;

}
