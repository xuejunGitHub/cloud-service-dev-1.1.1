package com.yswl.cloudservice.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yswl.cloudservice.account.entity.haier.HaierUser;
import com.yswl.cloudservice.account.entity.haier.HaierUserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @projectName yswl-cloud-service
 * @since 2020/12/16-上午10:28
 */
@Mapper
public interface HaierUserAddressMapper extends BaseMapper<HaierUserAddress> {


	@Select("select id,haier_user_id,province,province_id,city,city_id,district,district_id,line1,postcode from ys_haier_user_address where haier_user_id = #{userId} limit 1")
	HaierUserAddress getHaierUserAddressByUserId(Integer userId);

	@Select("select count(1) from ys_haier_user_address where haier_user_id = #{userId} limit 1")
	int getCountByUserId(Integer userId);



}
