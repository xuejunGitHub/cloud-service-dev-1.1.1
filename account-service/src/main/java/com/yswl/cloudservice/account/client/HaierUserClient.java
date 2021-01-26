package com.yswl.cloudservice.account.client;

import com.yswl.cloudservice.account.model.haier.HaierUserModel;
import feign.HeaderMap;
import feign.Headers;
import feign.QueryMap;
import feign.RequestLine;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 结算中台Client
 */
public interface HaierUserClient {


    @Headers({"Content-Type: application/json"})
    @RequestLine("POST /v2/haier/md/userinfo")
    Map<String, Object> postUserInfo(@HeaderMap Map<String, Object> headerMap, HaierUserModel userInfoModel);

    @RequestLine("GET /v2/haier/userinfo")
    Map<String, Object> getUserInfo(@HeaderMap Map<String, Object> headerMap);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @RequestLine("POST /oauth/token")
    Map<String,String> getUserToken(@QueryMap Map<String,String> params);


    @Headers({"Content-Type: multipart/form-data"})
    @RequestLine("PUT /v2/user/avatar/upload")
    Map<String,Object> upload(@HeaderMap Map<String, Object> headerMap,@RequestPart("file") MultipartFile file);


}
