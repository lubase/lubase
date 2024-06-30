package com.lubase.sso.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "custom.sso")
public class SSOConfig {
    private  String ssoTokenUrl;
    private  String platformCode;
}
