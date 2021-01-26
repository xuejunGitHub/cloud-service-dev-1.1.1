package com.yswl.cloudservice.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yswl.cloudservice.account.entity.UserPointsLog;
import com.yswl.cloudservice.account.model.UserQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * 用户积分记录(UserPointsLog)表数据库访问层
 *
 * @author wangfengchen
 * @since 2020-11-05 09:16:54
 */
@Mapper
public interface UserPointsLogMapper extends BaseMapper<UserPointsLog> {

    @SelectProvider(type = UserPointsLogProvider.class, method = "getUserPointsLogList")
    List<UserPointsLog> getUserPointsLogList(UserQuery q);

    class UserPointsLogProvider {

        public String getUserPointsLogList(UserQuery q) {
            SQL sql = new SQL(){{
                SELECT("id,points");
                FROM("ys_user_points_log");
            }};
            return sql.toString();
        }
    }
}