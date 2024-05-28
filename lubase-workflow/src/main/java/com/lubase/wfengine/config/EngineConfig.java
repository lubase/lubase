package com.lubase.wfengine.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EngineConfig {
    /**
     * 流程引擎事件TOPIC
     */
    public static final String MQ_WF_ENGINE_TOPIC = "LCP_V2_WF_ENGINE_TOPIC";
    /**
     * 流程引擎事件消费组
     */
    public static final String MQ_WF_ENGINE_CONSUMER_GROUP = "LCP_V2_WF_ENGINE";
    /**
     * 更新业务表消息发送的TOPIC
     */
    public static final String MQ_UPDATE_TABLE_TOPIC = "LCP_V2_WF_UPDATE_TABLE_TOPIC";
    /**
     * 更新业务表消费组
     */
    public static final String MQ_UPDATE_TABLE_CONSUMER_GROUP = "LCP_V2_WF_UPDATE_TABLE";
    /**
     * 流程引擎公开事件主题
     */
    public static final String MQ_WF_PUBLIC_TOPIC = "LCP_V2_WF_PUBLIC_TOPIC";

    @Value("${custom.wf-engine.enable-scheduled:0}")
    Boolean enableScheduled;

    public Boolean getEnableScheduled() {
        return enableScheduled;
    }
}
