package com.yswl.cloudservice.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yswl.cloudservice.account.entity.UserSource;
import org.apache.ibatis.annotations.Mapper;

/**
 * (UserSource)表数据库访问层
 *
 * @author wfc
 * @since 2020-12-24 15:41:01
 */
@Mapper
public interface UserSourceMapper extends BaseMapper<UserSource> {

}