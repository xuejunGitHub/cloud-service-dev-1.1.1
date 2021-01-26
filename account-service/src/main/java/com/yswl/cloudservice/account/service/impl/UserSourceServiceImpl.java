package com.yswl.cloudservice.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yswl.cloudservice.account.entity.UserSource;
import com.yswl.cloudservice.account.mapper.UserSourceMapper;
import com.yswl.cloudservice.account.service.UserSourceService;
import org.springframework.stereotype.Service;

/**
 * (UserSource)表服务实现类
 *
 * @author wfc
 * @since 2020-12-24 15:41:02
 */
@Service
public class UserSourceServiceImpl extends ServiceImpl<UserSourceMapper, UserSource> implements UserSourceService {

}