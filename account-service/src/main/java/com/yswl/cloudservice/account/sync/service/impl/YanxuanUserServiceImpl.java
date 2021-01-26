package com.yswl.cloudservice.account.sync.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yswl.cloudservice.account.sync.entity.YanxuanUser;
import com.yswl.cloudservice.account.sync.mapper.YanxuanUserMapper;
import com.yswl.cloudservice.account.sync.service.YanxuanUserService;
import org.springframework.stereotype.Service;

@DS("yanxuan")
@Service
public class YanxuanUserServiceImpl extends ServiceImpl<YanxuanUserMapper, YanxuanUser> implements YanxuanUserService {
}
