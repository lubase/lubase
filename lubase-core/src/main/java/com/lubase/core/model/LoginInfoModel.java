package com.lubase.core.model;

import com.lubase.core.exception.LoginErrorException;
import com.lubase.orm.model.LoginUser;
import lombok.Data;

@Data
public class LoginInfoModel {
    private LoginUser loginUser;
    private LoginErrorException loginErrorException;
}
