package com.yswl.cloudservice.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yswl.cloudservice.account.entity.UserAddress;
import com.yswl.cloudservice.account.model.UserQuery;

import java.util.List;

public interface UserAddressService extends IService<UserAddress> {

    Object addUserAddress(UserAddress userAddress);

    boolean updateUserAddress(UserAddress userAddress);

    boolean removeUserAddress(UserAddress userAddress);

    List<UserAddress> getUserAddress(UserQuery q);

}
