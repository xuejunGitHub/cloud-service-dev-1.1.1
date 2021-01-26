package com.yswl.cloudservice.account.sync.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yswl.cloudservice.account.sync.entity.WashUser;
import com.yswl.cloudservice.account.sync.mapper.WashUserMapper;
import com.yswl.cloudservice.account.sync.service.WashUserService;
import org.springframework.stereotype.Service;

@DS("wash")
@Service
public class WashUserServiceImpl extends ServiceImpl<WashUserMapper, WashUser> implements WashUserService {
}
