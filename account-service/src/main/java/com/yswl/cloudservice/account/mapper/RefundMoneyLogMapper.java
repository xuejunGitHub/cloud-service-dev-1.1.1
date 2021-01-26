package com.yswl.cloudservice.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yswl.cloudservice.account.entity.RefundMoneyLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 钱包日志(WalletMoneyLog)表数据库访问层
 *
 * @author wfc
 * @since 2020-11-24 15:32:32
 */
@Mapper
public interface RefundMoneyLogMapper extends BaseMapper<RefundMoneyLog> {

}