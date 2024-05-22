package com.lcp.qibao.service.userright.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleModel implements Serializable {
    private Long id;
    private Integer roleType;
    private Long appId;
}
