package com.yswl.cloudservice.account.model;

import com.yswl.cloudservice.common.core.base.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wangfengchen
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQuery extends BaseQuery {
    private String phone;
    private String name;
    private String userIds;
    private String openId;
}
