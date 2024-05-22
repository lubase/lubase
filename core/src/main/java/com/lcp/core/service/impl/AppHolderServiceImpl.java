package com.lcp.core.service.impl;

import com.lcp.core.model.LoginUser;
import com.lcp.core.service.AppHolderService;
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
