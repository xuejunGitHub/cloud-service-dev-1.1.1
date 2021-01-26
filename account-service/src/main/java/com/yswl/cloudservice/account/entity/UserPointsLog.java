package com.yswl.cloudservice.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class UserPointsLog {
    //主键id        
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    //用户id        
    private String userId;
    //积分数        
    private Integer points;
    //0 赚取 1 消费        
    private Integer type;
    //备注        
    private String remark;
    //创建时间        
    private Date createTime;

}