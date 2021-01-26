package com.yswl.cloudservice.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yswl.cloudservice.account.entity.UserPointsLog;
import com.yswl.cloudservice.account.mapper.UserPointsLogMapper;
import com.yswl.cloudservice.account.model.UserQuery;
import com.yswl.cloudservice.account.service.UserPointsLogService;
import com.yswl.cloudservice.common.web.util.AssertUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户积分记录(UserPointsLog)表服务实现类
 *
 * @author wangfengchen
 * @since 2020-11-05 09:16:55
 */
@Service
public class UserPointsLogServiceImpl extends ServiceImpl<UserPointsLogMapper, UserPointsLog> implements UserPointsLogService {

    @Override
    public Object getUserPointsLogList(UserQuery q) {
        AssertUtils.notNull(q.getUserId(), "用户id不能为空");
        AssertUtils.notNull(q.getProId(), "项目id不能为空");
        PageHelper.startPage(q.getPageNum(), q.getPageSize());
        List<UserPointsLog> list = baseMapper.getUserPointsLogList(q);
        return new PageInfo<>(list);
    }
}