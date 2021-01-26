package com.yswl.cloudservice.account.mapper;

import com.yswl.cloudservice.account.entity.User;
import com.yswl.cloudservice.account.model.UserQuery;
import com.yswl.cloudservice.common.web.mp.CommonMapper;
import com.yswl.cloudservice.common.web.util.SqlUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @author wangfengchen
 */
@Mapper
public interface UserMapper extends CommonMapper<User> {

    @SelectProvider(type = UserProvider.class, method = "getUserList")
    List<User> getUserList(UserQuery q);

    @SelectProvider(type = UserProvider.class, method = "searchUserInfo")
    List<User> searchUserInfo(UserQuery q);

    @SelectProvider(type = UserProvider.class, method = "getUserBatch")
    List<User> getUserBatch(UserQuery q);

    class UserProvider {

        public String getUserList(UserQuery q) {
            SQL sql = new SQL() {{
                SELECT("u.id,u.name,u.phone,u.head_url,u.gender,u.birthday,u.update_time");
                FROM("ys_user u");
                if (q.getProId() != null) {
                    SELECT("us.create_time");
                    INNER_JOIN("ys_user_source us on us.pro_id=#{proId} and u.id=us.user_id");
                } else {
                    SELECT("u.create_time");
                }
                if (StringUtils.isNotBlank(q.getPhone())) {
                    WHERE("u.phone like concat('%', #{phone}, '%')");
                }
                if (StringUtils.isNotBlank(q.getName())) {
                    WHERE("u.name like concat('%', #{name}, '%')");
                }
                ORDER_BY("u.create_time desc");
            }};
            return sql.toString();
        }

        public String searchUserInfo(UserQuery q) {
            SQL sql = new SQL() {{
                SELECT("u.id,u.name,u.phone,u.head_url,u.gender,u.birthday,u.update_time,u.create_time");
                FROM("ys_user u");
                if (q.getId() != null) {
                    WHERE("u.id=#{id}");
                }
                if (StringUtils.isNotBlank(q.getSearch())) {
                    WHERE("(name like concat('%', #{search}, '%') or phone like concat('%', #{search}, '%'))");
                }
                if (StringUtils.isNotBlank(q.getUserIds())) {
                    WHERE("u.id in (" + q.getUserIds() + ")");
                }
            }};
            return sql.toString();
        }

        public String getUserBatch(UserQuery q) {
            SQL sql = new SQL() {{
                SELECT("u.id,u.name,u.phone,u.head_url,u.gender,u.birthday,u.update_time,u.create_time");
                FROM("ys_user u");
                WHERE("u.id in (" + SqlUtils.long2Str(q.getTargetIds()) +")");
            }};
            return sql.toString();
        }
    }
}
