package com.yswl.cloudservice.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yswl.cloudservice.account.entity.UserAddress;
import com.yswl.cloudservice.account.mapper.UserAddressMapper;
import com.yswl.cloudservice.account.model.UserQuery;
import com.yswl.cloudservice.account.service.UserAddressService;
import com.yswl.cloudservice.common.core.exps.BusException;
import com.yswl.cloudservice.common.core.util.MapUtils;
import com.yswl.cloudservice.common.web.util.GeoUtils;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {


    @Override
    public Object addUserAddress(UserAddress userAddress) {
        setLngLat(userAddress);
        isDefault(userAddress);
        save(userAddress);
        return userAddress;
    }

    @Override
    public boolean updateUserAddress(UserAddress userAddress) {
        setLngLat(userAddress);
        isDefault(userAddress);
        return updateById(userAddress);
    }

    @Override
    public boolean removeUserAddress(UserAddress userAddress) {
        UserAddress upd = new UserAddress();
        upd.setId(userAddress.getId());
        upd.setIsDelete(1);
        return updateById(upd);
    }

    private void isDefault(UserAddress userAddress) {
        if (userAddress.getIsDefault() != null && userAddress.getIsDefault() == 1) {
            val uw = new UpdateWrapper<UserAddress>();
            uw.set("is_default", 0);
            uw.eq("user_id", userAddress.getUserId());
            update(uw);
        }
    }

    private void setLngLat(UserAddress userAddress) {
        Map result = GeoUtils.getLngLat(userAddress.getStreetAddress());
        if (result == null) {
            throw new BusException("地址解析错误");
        }
        userAddress.setLongitude(MapUtils.getString(result, "lng"));
        userAddress.setLatitude(MapUtils.getString(result, "lat"));
    }

    @Override
    public List<UserAddress> getUserAddress(UserQuery q) {
        val qw = new QueryWrapper<UserAddress>();
        qw.eq("user_id", q.getUserId());
        qw.eq("is_delete", 0);//未删除
        qw.orderByDesc("is_default, update_time");
        return list(qw);
    }
}
