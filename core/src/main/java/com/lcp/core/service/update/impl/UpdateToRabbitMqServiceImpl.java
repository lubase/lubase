package com.lcp.core.service.update.impl;

import com.alibaba.fastjson.JSON;
import com.lcp.core.model.DbCollection;
import com.lcp.core.service.update.UpdateTriggerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(9)
public class UpdateToRabbitMqServiceImpl implements UpdateTriggerService {

    @Autowired
    AmqpTemplate amqpTemplate;

    String enableFullText;

    public UpdateToRabbitMqServiceImpl(Environment environment) {
        this.enableFullText = environment.getProperty("custom.enable-fulltext");
    }

    @Override
    public void afterUpdate(DbCollection collection, Integer updateRowCount) {
        if (enableFullText.equals("0")) {
            return;
        }
        log.info(String.format("表%s开始更新：", collection.getTableName()));
        amqpTemplate.convertAndSend("table-update", JSON.toJSONString(collection));
        log.info("发送mq成功！");
    }
}
