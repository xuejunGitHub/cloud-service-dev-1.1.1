package com.yswl.cloudservice.account.model.haier;

import lombok.Data;

/**
 * @projectName yswl-cloud-service
 * @since 2020/12/15-下午4:19
 */
@Data
public class HaierUserAddressModel {

	    private String province;
		private String province_id;
		private String city;
		private String city_id;
		private String district;
		private String district_id;
		private String line1;
		private String postcode;

}
