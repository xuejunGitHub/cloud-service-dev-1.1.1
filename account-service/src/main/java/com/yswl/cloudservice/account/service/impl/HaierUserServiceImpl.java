package com.yswl.cloudservice.account.service.impl;

import com.yswl.cloudservice.account.api.wx.MiniAppApi;
import com.yswl.cloudservice.account.client.HaierUserClient;
import com.yswl.cloudservice.account.entity.haier.HaierUser;
import com.yswl.cloudservice.account.entity.haier.HaierUserAddress;
import com.yswl.cloudservice.account.mapper.HaierUserAddressMapper;
import com.yswl.cloudservice.account.mapper.HaierUserMapper;
import com.yswl.cloudservice.account.model.UserQuery;
import com.yswl.cloudservice.account.model.haier.HaierUserAddressModel;
import com.yswl.cloudservice.account.service.HaierUserService;
import com.yswl.cloudservice.client.sys.SystemClient;
import com.yswl.cloudservice.client.sys.model.ConfModel;
import com.yswl.cloudservice.client.sys.model.ConfQuery;
import com.yswl.cloudservice.common.core.constants.CacheKeyConstants;
import com.yswl.cloudservice.common.core.constants.PayConstants;
import com.yswl.cloudservice.common.core.exps.BusException;
import com.yswl.cloudservice.common.core.util.BeanUtils;
import com.yswl.cloudservice.common.core.util.MapUtils;
import com.yswl.cloudservice.common.web.redis.RedisAssistant;
import com.yswl.cloudservice.common.web.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @since 2020/12/14-下午4:53
 */
@Slf4j
@RefreshScope
@Service
public class HaierUserServiceImpl implements HaierUserService {

//	@Value("${config.haier.psi.uri}")
//	private String requestUrl;
	@Value("${config.haier.psi.clientId}")
	private String clientId;
	@Value("${config.haier.psi.clientSecret}")
	private String clientSecret;

	@Autowired
	private RedisAssistant redisAssistant;
	@Autowired
	private SystemClient systemClient;
	@Autowired
	private HaierUserMapper haierUserMapper;
	@Autowired
	private HaierUserAddressMapper haierUserAddressMapper;
	@Autowired
	private MiniAppApi miniAppApi;
	@Autowired
	private HaierUserClient haierUserClient;


//	/**
//	 * 接口〇：获取应用级保护的access_token
//	 * 接口文档地址
//	 * http://d.uoc.haier.net/web/#/p/bdbd43afb76dc44e1b4ab4cda1d60d4e
//	 */
//	@SneakyThrows
//	public String getHaierAccessToken() {
//		String key = CacheKeyConstants.HAIER_USER_CENTER_KEY;
//		String token = (String) redisAssistant.get(key);
//		if (token != null) {
//			return token;
//		}
//		Map<String, String> params
//				= MapUtils.create("client_id", clientId,
//				"client_secret", clientSecret,
//				"grant_type", "client_credentials");
//		ResponseBody post = HttpClient.getInstance().post(requestUrl + "/oauth/token", params, "Content-Type", "application/x-www-form-urlencoded");
//		String result = post.string();
//		Map r = JsonUtils.toObject(result, Map.class);
//		if (MapUtils.getInt(r, "errcode", 0) == 0) {
//			token = MapUtils.getString(r, "access_token");
//			int expiresIn = MapUtils.getInt(r, "expires_in", 800000);
//			redisAssistant.set(key, token, expiresIn, TimeUnit.SECONDS);
//			return token;
//		} else {
//			throw new BusException(MapUtils.getString(r, "errmsg"));
//		}
//	}

//	@Override
//	public Object updateUserInfo(HaierUserModel userInfoModel) {
//		updateHaierUserInfo(userInfoModel);
//		UserQuery query = new UserQuery();
//		query.setPhone(userInfoModel.getPhone());
//		query.setOpenId(userInfoModel.getOpenId());
//		query.setProId(userInfoModel.getProId());
//		return setUserInfo(query);
//	}


	/**
	 * 1.根据proId获取到appId,secret
	 * 2.根据appId,secret换取微信access_token
	 * 3.获取海尔用户中心access_token
	 * 4.携带海尔access_token请求
	 */
	@Override
	public Object setUserInfo(UserQuery query) {
		Map<String, Object> haierUserInfo = getHaierUserInfo(query);
		saveHaierUserInfo(haierUserInfo);
		return haierUserInfo;
	}

//	private void setHaierUserInfo(Map<String, Object> map, UserQuery query) {
//		saveHaierUserInfo(map, query.getProId());
//		HaierUserModel model = convertHaierUserModel(haierUser);
//		model.setProId(query.getProId());
//		model.setOpenId(query.getOpenId());
//		model.setPhone(query.getPhone());
//		updateHaierUserInfo(model);
//		updateLocalUser(map);
//	}

//	/**
//	 * 更新用户中心信息
//	 */
//	@Override
//	public boolean updateHaierUserInfo(HaierUserModel model) {
//		AssertUtils.hasText(model.getOpenId(), "openId不能为空");
//		AssertUtils.hasText(model.getPhone(), "手机号不能为空");
//		AssertUtils.notNull(model.getProId(), "项目id不能为空");
//		UserQuery query = new UserQuery();
//		query.setProId(model.getProId());
//		query.setOpenId(model.getOpenId());
//		query.setPhone(model.getPhone());
//		String haierUserToken = getHaierUserToken(query);
//		haierUserClient.postUserInfo(MapUtils.create("Authorization", "Bearer " + haierUserToken), model);
//		return true;
//	}

	/**
	 * http://d.uoc.haier.net/web/#/p/f7214b556fb9186730ea3d8011f78804
	 * 用户中心头像上传接口
	 * PUT /v2/user/avatar/upload HTTP/1.1
	 * Host: https://taccount.haier.com [海尔品牌测试环境]
	 * https://account-api.haier.net [海尔品牌正式环境]
	 * Authorization: Bearer xxxxx
	 * Content-Type: multipart/form-data
	 */
	@Override
	public Object uploadFile(MultipartFile file, UserQuery query) {
		String haierUserToken = getHaierUserToken(query);
		Map<String, Object> r = haierUserClient.upload(MapUtils.create("Authorization", "Bearer " + haierUserToken), file);
		if ("pass".equals(MapUtils.getString(r, "error", "pass"))) {
			setUserInfo(query);
		}
		return r;
	}

	@Override
	public Map<String, Object> getHaierUserInfo(UserQuery query) {
		AssertUtils.notNull(query.getProId(), "项目id不能为空");
		AssertUtils.hasText(query.getOpenId(), "openId不能为空");
		AssertUtils.hasText(query.getPhone(), "手机号不能为空");
		String haierUserToken = getHaierUserToken(query);
		return getHaierUserInfo(haierUserToken);
	}

	/**
	 * 获得海尔用户个人级token
	 */
	private String getHaierUserToken(UserQuery query) {
		ConfModel confWxModel = getConfWxByProId(query.getProId());
		//2个小时
		String wxAccessToken = miniAppApi.getAccessToken(query.getProId());
		log.info("wxAccessToken={},proId={}", wxAccessToken, query.getProId());
		//10天
		return getHaierUserToken(query, confWxModel.getAppId(), wxAccessToken);
	}

	/**
	 * http://d.uoc.haier.net/web/#/p/bdbd43afb76dc44e1b4ab4cda1d60d4e
	 * 获得海尔用户个人级token
	 */
	private String getHaierUserToken(UserQuery query, String appId, String wxToken) {

		String key = CacheKeyConstants.HAIER_USER_TOKEN_KEY + query.getOpenId();
		String token = (String) redisAssistant.get(key);
		log.info("tokenKey={}", key);
		if (token != null) {
			return token;
		}
		Map<String, String> params
				= MapUtils.create("client_id", clientId,
				"client_secret", clientSecret,
				"grant_type", "password",
				"connection", "type_wechat_access_token",
				"username", query.getPhone(),
				"wechat_access_token", wxToken,
				"wechat_openid", query.getOpenId(),
				"wechat_scene", "0",
				"wechat_client_ip", "27.223.25.34",
				"wechat_appid", appId
		);
//		BeanUtils.mapToBean(params, RequestHaierModel.class);
		log.info("getHaierUserToken.params={}", params);
		Map<String, String> r = haierUserClient.getUserToken(params);
		log.info("getHaierUserToken.Response={}", r);
		if (StringUtils.isNotEmpty(MapUtils.getString(r, "error"))) {
			throw new BusException(MapUtils.getString(r, "error"));
		}
		token = MapUtils.getString(r, "access_token");
		int expiresIn = MapUtils.getInt(r, "expires_in", 3500);
		redisAssistant.set(key, token, expiresIn, TimeUnit.SECONDS);
		return token;
	}

	/**
	 * 查询海尔用户全量信息
	 * 接口文档地址
	 * http://d.uoc.haier.net/web/#/p/fc502b09ca83d6219e7da3342ef4950d
	 */
	private Map<String, Object> getHaierUserInfo(String haierUserToken) {
		Map<String, Object> r = haierUserClient.getUserInfo(MapUtils.create("Authorization", "Bearer " + haierUserToken));
		if ("pass".equals(MapUtils.getString(r, "error", "pass"))) {
			return r;
		} else {
			throw new BusException(MapUtils.getString(r, "errmsg"));
		}
	}

	/**
	 * 新增/更新本地海尔用户信息
	 */
	@Transactional
	@Override
	public HaierUser saveHaierUserInfo(Map<String, Object> userMap) {
		HaierUser userModel = BeanUtils.mapToBean(userMap, HaierUser.class);
		Integer userId = userModel.getUserId();
		HaierUser existUser = haierUserMapper.getHaierUserByUserId(userId);
		if (existUser != null) {
			userModel.setId(existUser.getId());
			haierUserMapper.updateById(userModel);
		} else {
			haierUserMapper.insert(userModel);
		}
		HaierUserAddressModel addressModel = userModel.getAddress();
		HaierUserAddress address = convertLocalUserAddress(addressModel);
		if (address != null) {
			address.setHaierUserId(userId);
			address.setCityId(addressModel.getCity_id());
			address.setProvinceId(addressModel.getProvince_id());
			address.setDistrictId(addressModel.getDistrict_id());
			HaierUserAddress existAddress = haierUserAddressMapper.getHaierUserAddressByUserId(userId);
			if (existAddress != null) {
				address.setId(existAddress.getId());
				haierUserAddressMapper.updateById(address);
			} else {
				haierUserAddressMapper.insert(address);
			}
		}
		return userModel;
	}


//	private Map updateHaierUserInfo(String haierUserToken, HaierUserModel userInfoModel) throws IOException {
//		return haierUserClient.postUserInfo(MapUtils.create("Authorization", "Bearer " + haierUserToken), userInfoModel);
//	}

//	/**
//	 * 修改本地用户信息
//	 */
//	private void updateLocalUser(Map<String, Object> haierUserMap) {
//		HaierUser haierUser = BeanUtils.mapToBean(haierUserMap, HaierUser.class);
//		User localUser = userService.getUserByPhone(haierUser.getMobile());
//		if (localUser != null) {
//			localUser.setHaierUserId(haierUser.getUserId());
//			localUser.setHeadUrl(haierUser.getAvatarUrl());
//			localUser.setBirthday(StringUtils.isNotEmpty(haierUser.getBirthday())
//					? haierUser.getBirthday() : localUser.getBirthday());
//			localUser.setName(StringUtils.isNotEmpty(haierUser.getNickname())
//					? haierUser.getNickname() : localUser.getName());
//			localUser.setEmail(StringUtils.isNotEmpty(haierUser.getEmail())
//					? haierUser.getEmail() : localUser.getEmail());
//			if (StringUtils.isNotEmpty(haierUser.getGender())) {
//				localUser.setGender("male".equals(haierUser.getGender()) ? 1 : 0);
//			}
//			userService.updateById(localUser);
//		} else {
//			userService.save(convertLocalUser(haierUser));
//		}
//	}

	/**
	 * 根据项目Id获得微信配置信息
	 */
	private ConfModel getConfWxByProId(Integer proId) {
		ConfQuery confQuery = new ConfQuery();
		confQuery.setType(PayConstants.PAYMENT_TYPE_WX);
		confQuery.setProId(proId);
		return systemClient.getThirdAppConf(confQuery).successData();
	}

//	private User convertLocalUser(HaierUser haierUser) {
//		User user = new User();
//		user.setPhone(haierUser.getMobile());
//		user.setHeadUrl(haierUser.getAvatarUrl());
//		user.setHaierUserId(haierUser.getUserId());
//		//生日
//		user.setBirthday(haierUser.getBirthday());
//		return user;
//	}

	private HaierUserAddress convertLocalUserAddress(HaierUserAddressModel address) {
		if (address != null) {
			HaierUserAddress entity = new HaierUserAddress();
			BeanUtils.copyProperties(address, entity);
			return entity;
		}
		return null;

	}

//	private HaierUserModel convertHaierUserModel(HaierUser haierUser) {
//		HaierUserModel userModel = new HaierUserModel();
//		BeanUtils.copyProperties(haierUser, userModel);
//		return userModel;
//	}
}
