package com.yswl.cloudservice.account.service;

import com.yswl.cloudservice.account.entity.haier.HaierUser;
import com.yswl.cloudservice.account.model.UserQuery;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @since 2020/12/14-下午5:23
 */
public interface HaierUserService {

//	boolean updateHaierUserInfo(HaierUserModel userInfoModel);

	Object setUserInfo(UserQuery query);

	HaierUser saveHaierUserInfo(Map<String, Object> userMap);

	Object uploadFile(MultipartFile file, UserQuery query);

	Object getHaierUserInfo(UserQuery query);

}
