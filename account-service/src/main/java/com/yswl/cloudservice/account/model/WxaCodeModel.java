package com.yswl.cloudservice.account.model;

import lombok.Data;

import java.util.Map;

@Data
public class WxaCodeModel {
    private Integer proId;
    private String scene;
    private String page;
    private Integer width;
    private Boolean auto_color;
    private Map<String, Object> line_color;
    private Boolean is_hyaline;
}
