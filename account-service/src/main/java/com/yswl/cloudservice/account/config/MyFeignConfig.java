package com.yswl.cloudservice.account.config;

import com.yswl.cloudservice.account.client.HaierUserClient;
import feign.Feign;
import feign.Request;
import feign.RequestTemplate;
import feign.Target;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MyFeignConfig {

    @Value("${config.haier.psi.uri}")
    private String requestUri;
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public SpringEncoder springEncoder() {
        return new SpringEncoder(this.messageConverters);
    }

    @Bean
    public SpringDecoder springDecoder() {
        return new SpringDecoder(this.messageConverters);
    }

    @Bean
    public HaierUserClient haierUserClient(SpringEncoder springEncoder, SpringDecoder springDecoder, OkHttpClient okHttpClient) {
        return Feign.builder()
                .client(new feign.okhttp.OkHttpClient(okHttpClient))
                .encoder(springEncoder)
                .decoder(springDecoder)
                .target(new Target<HaierUserClient>() {
                    @Override
                    public Class<HaierUserClient> type() {
                        return HaierUserClient.class;
                    }

                    @Override
                    public String name() {
                        return "haierUser";
                    }

                    @Override
                    public String url() {
                        // 获取实时url
                        return requestUri;
                    }

                    @Override
                    public Request apply(RequestTemplate input) {
                        if (input.url().indexOf("http") != 0) {
                            input.insert(0, url());
                        }
                        return input.request();
                    }
                });
    }

}
