package com.yswl.cloudservice.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class RefundMoneyLog {
    //主键id        
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    //用户id        
    private String userId;
    //订单编号        
    private String orderNo;
    //产生金额
    private Double money;
    // 0 赚取 1 消费 2 提现
    private Integer type;
    //备注        
    private String remark;
    //创建时间        
    private Date createTime;

}