package com.yswl.cloudservice.account.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yswl.cloudservice.account.entity.UserSource;
import com.yswl.cloudservice.account.entity.excel.ExpUser;
import com.yswl.cloudservice.account.model.UserModel;
import com.yswl.cloudservice.account.service.HaierUserService;
import com.yswl.cloudservice.account.service.UserService;
import com.yswl.cloudservice.account.service.UserSourceService;
import com.yswl.cloudservice.account.service.VerifyCodeService;
import com.yswl.cloudservice.account.mapper.UserMapper;
import com.yswl.cloudservice.account.mapper.UserWxMapper;
import com.yswl.cloudservice.account.sync.service.SyncService;
import com.yswl.cloudservice.common.core.base.BatchUpdateModel;
import com.yswl.cloudservice.common.core.constants.CacheKeyConstants;
import com.yswl.cloudservice.common.core.constants.ConfigConstants;
import com.yswl.cloudservice.account.entity.User;
import com.yswl.cloudservice.account.entity.UserWx;
import com.yswl.cloudservice.common.core.exps.BusException;
import com.yswl.cloudservice.account.model.AccountModel;
import com.yswl.cloudservice.account.model.UserQuery;
import com.yswl.cloudservice.common.core.util.AesEncryptUtils;
import com.yswl.cloudservice.common.core.util.BeanUtils;
import com.yswl.cloudservice.common.core.util.MapUtils;
import com.yswl.cloudservice.common.core.util.UUIDGeneratorUtils;
import com.yswl.cloudservice.common.upload.service.UploadService;
import com.yswl.cloudservice.common.web.mp.CommonServiceImpl;
import com.yswl.cloudservice.common.web.redis.RedisAssistant;
import com.yswl.cloudservice.common.web.util.HttpClient;
import com.yswl.cloudservice.common.web.util.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangfengchen
 */
@Slf4j
@Service
@RefreshScope
public class UserServiceImpl extends CommonServiceImpl<UserMapper, User> implements UserService {

    @Value("${config.defaultHeadUrl}")
    private String defaultHeadUrl;
    @Value("${config.jingxiHeadUrl}")
    private String jingxiHeadUrl;

    @Autowired
    private RedisAssistant redisAssistant;
    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    private UserWxMapper userWxMapper;
    @Autowired
    private SyncService syncService;
    @Value("${config.haier.psi.openApps}")
    private String openApps;
    @Autowired
    private HaierUserService haierUserService;
    @Autowired
    private UserSourceService userSourceService;
    @Autowired
    private UploadService uploadService;

    @Override
    public Map unifiedLogin(AccountModel am, User info) {
        if (info != null) {
            if (info.getStatus() == 2) {
                throw new BusException("该账号已被冻结，请联系客服人员");
            }
            //设置token
            return MapUtils.create("user", info);
        }
        return null;
    }

    @Override
    public Map login(AccountModel any) {
        switch (any.getLoginType()) {
            case ConfigConstants.AUTH_TYPE_PASSWORD:
                break;
            case ConfigConstants.AUTH_TYPE_VERIFY_CODE:
                String phone = any.getPhone();
                int check = verifyCodeService.checkVerifyCode(any.getProId(), phone, any.getVerCode());
                switch (check) {
                    case 0:
                        throw new BusException("验证码已失效");
                    case 2:
                        throw new BusException("验证码不正确");
                    case 1:
                        //使验证码失效
                        redisAssistant.del(CacheKeyConstants.USER_VERIFY_CODE_KEY + phone);
                        Map result = baseLogin(any);
                        if (result != null) {
                            return result;
                        }
                        break;
                }
            case ConfigConstants.AUTH_TYPE_THIRD:
                if (any.getPhone() != null) {
                    return baseLogin(any);
                } else {
                    return thirdLogin(any);
                }
        }
        return null;
    }

    @Override
    public Map register(AccountModel any) {
        User user = new User();
        user.setPhone(any.getPhone());
        user.setName(any.getPhone());
        user.setSource(any.getSource());
        user.setHeadUrl(getDefaultHeadUrl(any.getProId()));
        boolean r = save(user);
        if (r) {
            any.setUserId(user.getId());
            return MapUtils.create("user", user);
        }
        return null;
    }

    private String getDefaultHeadUrl(Integer proId) {
        if (proId == null) {
            return defaultHeadUrl;
        }
        if (proId == 6) {
            return jingxiHeadUrl;
        }
        return defaultHeadUrl;
    }

    @Override
    public Map baseLogin(AccountModel am) {
        Map result;
        User info = getUserByPhone(am.getPhone());
        if (info != null) {
            checkBindThirdAuth(info.getId(), am.getUnionId(), am.getThirdType());
            //统一登录
            result = unifiedLogin(am, info);
        } else {//注册
            result = register(am);
        }
        return result;
    }

    private void checkBindThirdAuth(String userId, String unionId, Integer thirdType) {
        if (unionId != null && thirdType != null) {
            if (thirdType == 1) {
                //微信
                LambdaQueryWrapper<UserWx> q = new LambdaQueryWrapper<>();
                q.eq(UserWx::getUserId, userId);
                if (userWxMapper.selectCount(q) > 0) {
                    throw new BusException("该手机号已经绑定过微信");
                }
                LambdaQueryWrapper<UserWx> q2 = new LambdaQueryWrapper<>();
                q2.eq(UserWx::getUnionId, unionId);
                q2.ne(UserWx::getUserId, 0);
                if (userWxMapper.selectCount(q2) > 0) {
                    throw new BusException("该微信已经绑定其他账号");
                }
            }
        }
    }

    @Override
    public User getUserByPhone(String phone) {
        LambdaQueryWrapper<User> q = new LambdaQueryWrapper<>();
        q.eq(User::getPhone, phone);
        return getOne(q);
    }

    @Override
    public Object getUserList(UserQuery q) {
        PageHelper.startPage(q.getPageNum(), q.getPageSize());
        List<User> list = baseMapper.getUserList(q);
        return new PageInfo<>(list);
    }

    @Override
    public Object getUserBatch(UserQuery q) {
        if (StringUtils.isBlank(q.getTargetIds())) {
            return Collections.emptyList();
        }
        return baseMapper.getUserBatch(q);
    }

    @Override
    public Object addUser(User user) {
        User r = getUserByPhone(user.getPhone());
        if (r != null) {
            throw new BusException(201, "该用户已存在", r);
        }
        save(user);
        return user;
    }

    @Override
    public boolean updateUser(User user) {
        user.setPhone(null);//不可修改手机号
        user.setSource(null);//不可修改用户来源
        boolean r = updateById(user);
        if (r) {
            //同步用户信息
            syncService.syncUserInfo(user);
        }
        return r;
    }

    @Override
    public Object addUserBatch(BatchUpdateModel<User> bu) {
        if (bu.getData() == null || bu.getData().isEmpty()) {
            throw new BusException("数据为空");
        }
        int size = bu.getData().size();
        if (size > 5000) {
            throw new BusException("超过限制数量5000");
        }
        List<User> existUsers = getExistUsers(bu.getData());
        List<User> users = filterExistUsers(bu.getData(), existUsers);
        fastSaveIgnoreBatch(users);
        if (existUsers != null && !existUsers.isEmpty()) {
            users.addAll(existUsers);
        }
        return users;
    }

    private List<User> getExistUsers(List<User> users) {
        val q = new QueryWrapper<User>();
        q.in("phone", users.stream().map(User::getPhone).collect(Collectors.toList()));
        return list(q);
    }

    private List<User> filterExistUsers(List<User> users, List<User> existUsers) {
        if (existUsers == null || existUsers.isEmpty()) {
            return users;
        }
        List<String> exists = existUsers.stream().map(User::getPhone).collect(Collectors.toList());
        return users.stream().filter(p -> !exists.contains(p.getPhone())).collect(Collectors.toList());
    }

    @Override
    public Object searchUserInfo(UserQuery q) {
        PageHelper.startPage(q.getPageNum(), q.getPageSize());
        List<User> list = baseMapper.searchUserInfo(q);
        return new PageInfo<>(list);
    }

    @Override
    public boolean modifyPhone(UserModel user) {
        if (getUserByPhone(user.getPhone()) != null) {
            throw new BusException("该手机号已存在");
        }
        int r = verifyCodeService.checkVerifyCode(user.getProId(), user.getPhone(), user.getVerCode());
        switch (r) {
            case 0:
                throw new BusException("验证码已失效");
            case 2:
                throw new BusException("验证码错误");
        }
        User upd = new User();
        upd.setId(user.getId());
        upd.setPhone(user.getPhone());
        boolean mr = updateById(upd);
        if (mr) {
            //同步手机号
            syncService.syncUserInfo(upd);
        }
        return mr;
    }

    @Override
    public boolean updateStatus(User user) {
        return updateById(user);
    }

    @SneakyThrows
    @Override
    public void exportUser(HttpServletResponse response, UserQuery q) {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("用户列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<User> list = baseMapper.getUserList(q);
        EasyExcel.write(response.getOutputStream(), ExpUser.class).sheet("用户列表")
                .doWrite(list.stream().map(this::convertExpUser).collect(Collectors.toList()));
    }

    private ExpUser convertExpUser(User user) {
        ExpUser expUser = new ExpUser();
        BeanUtils.copyProperties(user, expUser);
        if (user.getGender() != null) {
            expUser.setGenderName(user.getGender() == 1 ? "男" : "女");
        }
        return expUser;
    }

    private Map thirdLogin(AccountModel any) {
        switch (any.getThirdType()) {
            case ConfigConstants.AUTH_THIRD_TYPE_MINI_APP:
                return miniAppLogin(any);
            case ConfigConstants.AUTH_THIRD_TYPE_WX:
                return wxAuthorization(any);
            case ConfigConstants.AUTH_THIRD_TYPE_QQ:
                break;
        }
        return null;
    }

    private Map miniAppLogin(AccountModel any) {
        User user = null;
        if (!StringUtils.isAnyBlank(any.getSessionKey(), any.getEncryptedData(), any.getIvStr())) {
            String ss = AesEncryptUtils.decrypt(any.getSessionKey().replace(" ", "+"), any.getEncryptedData().replace(" ", "+"), any.getIvStr().replace(" ", "+"));
            Map userInfo = JsonUtils.toObject(ss, Map.class);
            if (userInfo != null) {
                String phone = MapUtils.getString(userInfo,"purePhoneNumber", null);
                any.setPhone(phone);
                setHaierUser(any);
                if (StringUtils.isNoneBlank(phone)) {
                    user = getUserByPhone(phone);
                }
                if (user != null) {
                    //统一登录
                    any.setUserId(user.getId());
                    Map map = unifiedLogin(any, user);
                    if (map != null) {
                        int isRegister = setUserSource(any);
                        map.put("isRegister", isRegister);
                        return map;
                    }
                } else {//注册
                    if (StringUtils.isBlank(phone)) {
                        throw new BusException("手机号不能为空");
                    }
                    any.setAreaCode(MapUtils.getString(userInfo, "countryCode", null));
                    any.setUnionId(MapUtils.getString(userInfo, "unionId", null));
                    any.setPhone(phone);
                    Map map = register(any);
                    if (map != null) {
                        int isRegister = setUserSource(any);
                        map.put("isRegister", isRegister);
                        return map;
                    }
                }
            }
        }
        throw new BusException("信息解析错误");
    }

    private int setUserSource(AccountModel any) {
        if (any.getProId() == null) {
            log.error("用户来源proId为空");
            return 0;
        }
        LambdaQueryWrapper<UserSource> usq = new LambdaQueryWrapper<>();
        usq.eq(UserSource::getProId, any.getProId());
        usq.eq(UserSource::getUserId, any.getUserId());
        UserSource us = userSourceService.getOne(usq, false);
        if (us == null) {
            us = new UserSource();
            us.setProId(any.getProId());
            us.setUserId(any.getUserId());
            userSourceService.save(us);
            return 1;
        }
        return 0;
    }

    private Map wxAuthorization(AccountModel any) {
        LambdaQueryWrapper<UserWx> q = new LambdaQueryWrapper<>();
        q.eq(UserWx::getUnionId, any.getUnionId());
        UserWx wx = userWxMapper.selectOne(q);
        if (wx == null) {
            wx = new UserWx();
            wx.setUnionId(any.getUnionId());
            wx.setOpenId(any.getOpenId());
            wx.setNickname(any.getNickname());
            userWxMapper.insert(wx);
        } else {
            if (wx.getUserId() != null) {
                User user = getById(wx.getUserId());
                return unifiedLogin(any, user);
            }
        }
        //已存在微信信息，未绑定系统手机号
        return MapUtils.create("unionId", any.getUnionId());
    }

    private void setHaierUser(AccountModel any) {
        String proId = any.getProId().toString();
        log.info("setHaierUser.AccountModel={}", any);
        log.info("openApps={},proId={},openApps.contains(proId)={}", openApps, proId, openApps.contains(proId));
        if (Arrays.asList(openApps.split(",")).contains(proId)) {
            UserQuery uq = new UserQuery();
            uq.setOpenId(any.getOpenId());
            uq.setPhone(any.getPhone());
            uq.setProId(any.getProId());
            haierUserService.setUserInfo(uq);
            log.info("--------设置海尔用户成功---------");
        }
    }

    @Override
    public Object updateUserWxInfo(User user) {
        InputStream is = HttpClient.getInstance()
                .get(user.getHeadUrl(), null)
                .byteStream();
        String ok = "upload/portals/head/" + UUIDGeneratorUtils.get32UUID() + ".png";
        Map<String, Object> result = (Map<String, Object>) uploadService.uploadFile(is, ok);
        if (MapUtils.getInt(result, "statusCode", 0) == 200) {
            user.setHeadUrl(ok);
            updateById(user);
            return true;
        }
        return false;
    }
}
