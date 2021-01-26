package com.yswl.cloudservice.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class UserSource {
    //主键id        
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    //项目id        
    private Integer proId;
    //用户id        
    private String userId;
    //注册时间        
    private Date createTime;

}