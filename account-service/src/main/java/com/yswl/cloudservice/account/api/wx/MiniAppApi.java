package com.yswl.cloudservice.account.api.wx;

import com.yswl.cloudservice.account.model.WxaCodeModel;
import com.yswl.cloudservice.client.sys.SystemClient;
import com.yswl.cloudservice.client.sys.model.ConfModel;
import com.yswl.cloudservice.client.sys.model.ConfQuery;
import com.yswl.cloudservice.common.core.constants.CacheKeyConstants;
import com.yswl.cloudservice.common.core.constants.PayConstants;
import com.yswl.cloudservice.common.core.exps.BusException;
import com.yswl.cloudservice.common.core.util.MapUtils;
import com.yswl.cloudservice.common.web.redis.RedisAssistant;
import com.yswl.cloudservice.common.web.util.HttpClient;
import com.yswl.cloudservice.common.web.util.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wangfengchen
 * 微信小程序
 */
@Slf4j
@Component
public class MiniAppApi {

    private static final String WEB_ACCESS_TO_KEN_HTTPS = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    private static final String GET_WXA_CODE_UN_LIMIT = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";
    private static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    @Autowired
    private SystemClient systemClient;
    @Autowired
    private RedisAssistant redisAssistant;


    @SneakyThrows
    public Map getWebAccess(String code, Integer proId) {
        val q = new ConfQuery();
        q.setProId(proId);
        q.setType(PayConstants.PAYMENT_TYPE_WX);
        q.setSubType(PayConstants.WX_TYPE_MINI);
        ConfModel conf = systemClient.getThirdAppConf(q).successData();
        String url = String.format(WEB_ACCESS_TO_KEN_HTTPS, conf.getAppId(), conf.getSecret(), code);
        String response = HttpClient.getInstance()
                .get(url, null).string();
        return JsonUtils.toObject(response, Map.class);
    }

    @SneakyThrows
    public InputStream getWxaCodeUnLimit(WxaCodeModel model) {
        String token = getAccessToken(model.getProId());
        String url = String.format(GET_WXA_CODE_UN_LIMIT, token);
        ResponseBody responseBody =  HttpClient.getInstance()
                .postJson(url, MapUtils.create(
                        "scene", model.getScene(),
                        "page", model.getPage(),
                        "width", model.getWidth(),
                        "auto_color", model.getAuto_color(),
                        "line_color", model.getLine_color(),
                        "is_hyaline", model.getIs_hyaline()
                        ));
//        log.error(responseBody.string());
        return responseBody.byteStream();
    }

    @SneakyThrows
    public String getAccessToken(Integer proId) {
        String key = CacheKeyConstants.WX_MINI_APP_TOKEN_KEY + proId;
        String token = (String) redisAssistant.get(key);
        if (token != null) {
            return token;
        }
        val q = new ConfQuery();
        q.setProId(proId);
        q.setType(PayConstants.PAYMENT_TYPE_WX);
        q.setSubType(PayConstants.WX_TYPE_MINI);
        ConfModel conf = systemClient.getThirdAppConf(q).successData();
        String url = String.format(GET_ACCESS_TOKEN, conf.getAppId(), conf.getSecret());
        String response = HttpClient.getInstance()
                .get(url, null).string();
        Map r = JsonUtils.toObject(response, Map.class);
        if (MapUtils.getInt(r, "errcode", 0) == 0) {
            token = MapUtils.getString(r, "access_token");
            int expiresIn = MapUtils.getInt(r, "expires_in", 7200);
            redisAssistant.set(key, token, expiresIn, TimeUnit.SECONDS);
            return token;
        } else {
            throw new BusException(MapUtils.getString(r,"errmsg"));
        }
    }
}
