package com.lubase.starter.service.userright.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserRightInfo {
    private Long userId;
    /**
     * 是否超级管理员
     */
    private Boolean isSupperAdministrator = false;
    /**
     * 是否应用管理员
     */
    private Boolean isAppAdministrator = false;

    /**
     * 所属角色列表
     */
    private List<Long> roleList = new ArrayList<>();

    /**
     * 功能权限列表
     */
    private List<Long> funcRightList = new ArrayList<>();

    /**
     * 字段权限列表
     */
    private List<ColumnRightModelVO> colRightList = new ArrayList<>();
}
