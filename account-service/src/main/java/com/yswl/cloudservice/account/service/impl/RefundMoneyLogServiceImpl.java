package com.yswl.cloudservice.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yswl.cloudservice.account.entity.RefundMoneyLog;
import com.yswl.cloudservice.account.mapper.RefundMoneyLogMapper;
import com.yswl.cloudservice.account.service.RefundMoneyLogService;
import org.springframework.stereotype.Service;

/**
 * 钱包日志(WalletMoneyLog)表服务实现类
 *
 * @author wfc
 * @since 2020-11-24 15:32:32
 */
@Service
public class RefundMoneyLogServiceImpl extends ServiceImpl<RefundMoneyLogMapper, RefundMoneyLog> implements RefundMoneyLogService {

}