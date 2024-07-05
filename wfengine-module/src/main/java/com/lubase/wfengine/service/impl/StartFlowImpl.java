package com.lubase.wfengine.service.impl;

import com.lubase.orm.exception.WarnCommonException;
import com.lubase.wfengine.model.RemoteCallbackModel;
import com.lubase.wfengine.service.StartFlow;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class StartFlowImpl implements StartFlow {
    @Autowired
    RestTemplate restTemplate;

    @Value("${lubase.main-api-server}")
    private String urlTemplate;

    @SneakyThrows
    @Override
    public Integer startWfByApi(String serviceId, String dataId, String userId) {
        if (StringUtils.isEmpty(serviceId) || StringUtils.isEmpty(dataId) || StringUtils.isEmpty(userId)) {
            return 0;
        }
        String url = String.format("%s/wft/manualStart", urlTemplate);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("serviceId", serviceId);
        map.add("dataId", dataId);
        map.add("userId", userId);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(map, headers);
        try {
            log.info(formEntity.toString());
            RemoteCallbackModel result = restTemplate.postForObject(url, formEntity, RemoteCallbackModel.class);
            if (result == null || result.getSuccess() == null) {
                throw new WarnCommonException("启动流程错误，通过接口启动流程返回值不应该为空");
            }
            if (result.getSuccess().equals(1)) {
                return 1;
            } else {
                throw new WarnCommonException(result.getMessage());
            }
        } catch (Exception exception) {
            log.error("提交流程报错" + url, exception);
            throw new WarnCommonException("提交流程报错" + exception.getMessage());
        }
    }

}
