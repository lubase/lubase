package com.lubase.orm.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class RestTemplateConfig {
    @Value("${lubase.call-api-log:1}")
    private String enableLog;

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(30000);
        factory.setReadTimeout(30000);
        CloseableHttpClient httpClient = HttpClientBuilder
                .create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();

        factory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(factory));
        if (enableLog.equals("1")) {
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
            interceptors.add(new LoggingRequestInterceptor());
            restTemplate.setInterceptors(interceptors);
        }
        return restTemplate;
    }
}
