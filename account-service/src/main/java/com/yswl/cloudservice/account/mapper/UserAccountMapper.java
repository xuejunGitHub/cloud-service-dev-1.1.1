package com.yswl.cloudservice.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yswl.cloudservice.account.entity.UserAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户账户表(UserAccount)表数据库访问层
 *
 * @author wangfengchen
 * @since 2020-11-05 09:11:27
 */
@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {

}