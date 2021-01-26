package com.yswl.cloudservice.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yswl.cloudservice.account.entity.User;
import com.yswl.cloudservice.account.model.AccountModel;
import com.yswl.cloudservice.account.model.UserModel;
import com.yswl.cloudservice.account.model.UserQuery;
import com.yswl.cloudservice.common.core.base.BatchUpdateModel;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author wangfengchen
 */
public interface UserService extends IService<User> {

    Map unifiedLogin(AccountModel am, User info);

    Map login(AccountModel any);

    Map register(AccountModel any);

    Map baseLogin(AccountModel am);

    User getUserByPhone(String phone);

    Object getUserList(UserQuery q);

    Object getUserBatch(UserQuery q);

    Object addUser(User user);

    boolean updateUser(User user);

    Object addUserBatch(BatchUpdateModel<User> bu);

    Object searchUserInfo(UserQuery q);

    boolean modifyPhone(UserModel user);

    boolean updateStatus(User user);

    void exportUser(HttpServletResponse response, UserQuery q);

    Object updateUserWxInfo(User user);
}
