package com.lubase.orm.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceOrmConfig {

    @Bean(name = "orm")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messagesOrm");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}