package com.lubase.orm.service.impl;

import com.lubase.orm.model.LoginUser;
import com.lubase.orm.service.AppHolderService;
import org.springframework.stereotype.Component;

@Component
public class AppHolderServiceImpl implements AppHolderService {
    private ThreadLocal<LoginUser> user = new ThreadLocal<>();

    @Override
    public void setUser(LoginUser user) {
        this.user.set(user);
    }

    @Override
    public LoginUser getUser() {
        return this.user.get();
    }

    @Override
    public void clear() {
        this.user.remove();
    }
}
