package com.yswl.cloudservice.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yswl.cloudservice.account.entity.UserPointsLog;
import com.yswl.cloudservice.account.model.UserQuery;

/**
 * 用户积分记录(UserPointsLog)表服务接口
 *
 * @author wangfengchen
 * @since 2020-11-05 09:16:55
 */
public interface UserPointsLogService extends IService<UserPointsLog> {

    Object getUserPointsLogList(UserQuery q);
}