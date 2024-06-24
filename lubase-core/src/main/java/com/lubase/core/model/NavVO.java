package com.lubase.core.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 导航数据的VO
 *
 * @author A
 */
@Data
public class NavVO implements Serializable {
    private static final long serialVersionUID = 1026242975157456896L;
    private Long id;
    private Long parentId;
    private String name;
    private String des;
    private Integer orderId;
    private String vueComponent;
    private String vueRouter;
    private String iconCode;
    private Integer type;
}
