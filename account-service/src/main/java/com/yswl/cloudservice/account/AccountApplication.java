package com.yswl.cloudservice.account;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import static com.yswl.cloudservice.common.core.constants.PkgConstants.*;

@EnableFeignClients(basePackages = {CLIENT_SYSTEM, CLIENT_NOTIFY})
@SpringCloudApplication
@ComponentScan({ACCOUNT, COMMON_WEB, COMMON_SECURITY, COMMON_UPLOAD})
public class AccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }

}
