package com.yswl.cloudservice.account.model;

import lombok.Data;

/**
 * @projectName yswl-cloud-service
 * @since 2020/12/18-下午7:15
 */
@Data
public class RequestHaierModel {

	private String client_id;
	private String client_secret;
	private String grant_type;
	private String connection;
	private String username;
	private String wechat_access_token;
	private String wechat_openid;
	private String wechat_scene;
	private String wechat_client_ip;
	private String wechat_appid;


}
