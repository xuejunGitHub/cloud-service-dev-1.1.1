package com.yswl.cloudservice.account.controller;

import com.yswl.cloudservice.account.entity.UserAddress;
import com.yswl.cloudservice.account.model.UserQuery;
import com.yswl.cloudservice.account.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("userAddress")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;


    /**
     * 新增用户地址
     */
    @PostMapping("addUserAddress")
    public Object addUserAddress(@RequestBody @Validated(UserAddress.Add.class) UserAddress userAddress) {
        return userAddressService.addUserAddress(userAddress);
    }

    /**
     * 编辑用户地址
     */
    @PostMapping("updateUserAddress")
    public Object updateUserAddress(@RequestBody @Validated(UserAddress.Upd.class) UserAddress userAddress) {
        return userAddressService.updateUserAddress(userAddress);
    }

    /**
     * 删除用户地址
     */
    @PostMapping("removeUserAddress")
    public Object removeUserAddress(@RequestBody UserAddress userAddress) {
        return userAddressService.removeUserAddress(userAddress);
    }

    /**
     * 根据地址id获取
     */
    @GetMapping("getById")
    public Object getById(Long id) {
        return userAddressService.getById(id);
    }

    /**
     * 获取地址
     */
    @GetMapping("getUserAddress")
    public Object getUserAddress(UserQuery q) {
        return userAddressService.getUserAddress(q);
    }
}
