package com.lubase.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 保存用户登录信息
 *
 * @author A
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {
    private Long id;
    private String code;
    private String name;
    private String token;
    /**
     * 用户部门id
     */
    private String orgId;
    private int errorCount;
}
