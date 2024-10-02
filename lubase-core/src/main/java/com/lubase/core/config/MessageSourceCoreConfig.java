package com.lubase.core.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class MessageSourceCoreConfig {

    @Bean(name = "core")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messagesCore");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return messageSource;
    }
}