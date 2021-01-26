package com.yswl.cloudservice.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class UserAccount {
    //主键id        
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    //用户id
    private String userId;
    //历史总量        
    private Integer totalPoints;
    //积分数        
    private Integer points;
    //锁定积分数        
    private Integer lockingPoints;
    //创建时间        
    private Date createTime;
    //分佣未入账金额
    private Double refundingMoney;
    //可提现分账金额
    private Double refundMoney;
    //分账提现中
    private Double refundWithdrawalMoney;
    //分账已提现
    private Double refundWithdrawnMoney;

}