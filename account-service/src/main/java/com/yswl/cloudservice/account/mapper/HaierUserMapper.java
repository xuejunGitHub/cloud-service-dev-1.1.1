package com.yswl.cloudservice.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yswl.cloudservice.account.entity.RefundMoneyLog;
import com.yswl.cloudservice.account.entity.haier.HaierUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @projectName yswl-cloud-service
 * @since 2020/12/16-上午10:27
 */
@Mapper
public interface HaierUserMapper extends BaseMapper<HaierUser> {

	@Select("select id,user_id,mobile,email,username,nickname,given_name,avatar_url,gender,birthday,marriage,education,extra_phone,family_num,income,height,weight,signature,reg_client_id from ys_haier_user where user_id = #{userId} limit 1")
	HaierUser getHaierUserByUserId(Integer userId);

	@Select("select count(1) from ys_haier_user where user_id = #{userId} limit 1")
	int getCountByUserId(Integer userId);


}
