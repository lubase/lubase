package com.lubase.orm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {
    long timeoutMs = 1000L;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        traceRequest(request, body);
        long start = System.currentTimeMillis();
        ClientHttpResponse response = execution.execute(request, body);
        long cost = System.currentTimeMillis() - start;
        traceResponse(response);
        if (cost > timeoutMs) {
            log.warn("Request uri: [{}], Cost times: {}ms", request.getURI(), cost);
        }
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        log.debug("===========================request begin================================================");
        log.debug("URI         : {}", request.getURI());
        log.debug("Method      : {}", request.getMethod());
        log.debug("Headers     : {}", request.getHeaders());
        log.debug("Request body: {}", new String(body, "UTF-8"));
        log.debug("==========================request end================================================");
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        log.debug("============================response begin==========================================");
        log.debug("Status code  : {}", response.getStatusCode());
        log.debug("Status text  : {}", response.getStatusText());
        log.debug("Headers      : {}", response.getHeaders());
        log.debug("Response body: {}", inputStringBuilder.toString());
        log.debug("=======================response end=================================================");
    }

}
