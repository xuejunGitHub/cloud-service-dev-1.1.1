package com.yswl.cloudservice.account.model;

import com.yswl.cloudservice.common.core.base.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WithdrawQuery extends BaseQuery {
    private Integer withdrawStatus;
}
