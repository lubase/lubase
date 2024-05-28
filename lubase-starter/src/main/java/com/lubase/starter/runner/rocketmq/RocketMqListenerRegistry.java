package com.lubase.starter.runner.rocketmq;

import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class RocketMqListenerRegistry {
    private final static Logger log = LoggerFactory.getLogger(
            org.apache.rocketmq.spring.autoconfigure.ListenerContainerConfiguration.class);

    private final ConfigurableApplicationContext applicationContext;

    private final AtomicLong counter = new AtomicLong(0);

    private final StandardEnvironment environment;

    private final RocketMQProperties rocketMqProperties;

    private final RocketMQMessageConverter rocketMqMessageConverter;

    public RocketMqListenerRegistry(ConfigurableApplicationContext applicationContext,
                                    RocketMQMessageConverter rocketMqMessageConverter,
                                    StandardEnvironment environment,
                                    RocketMQProperties rocketMqProperties) {
        this.applicationContext = applicationContext;
        this.rocketMqMessageConverter = rocketMqMessageConverter;
        this.environment = environment;
        this.rocketMqProperties = rocketMqProperties;
    }


    public void afterSingletonsInstantiated() {
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(RocketMQMessageListener.class)
                .entrySet().stream().filter(entry -> !ScopedProxyUtils.isScopedTarget(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        beans.forEach(this::registerContainer);
    }

    public void registerContainer(String beanName, Object bean) {
        Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);

        if (RocketMQListener.class.isAssignableFrom(bean.getClass()) && RocketMQReplyListener.class.isAssignableFrom(
                bean.getClass())) {
            throw new IllegalStateException(
                    clazz + " cannot be both instance of " + RocketMQListener.class.getName() + " and " + RocketMQReplyListener.class.getName());
        }

        if (!RocketMQListener.class.isAssignableFrom(bean.getClass()) && !RocketMQReplyListener.class.isAssignableFrom(
                bean.getClass())) {
            throw new IllegalStateException(
                    clazz + " is not instance of " + RocketMQListener.class.getName() + " or " + RocketMQReplyListener.class.getName());
        }

        RocketMQMessageListener annotation = clazz.getAnnotation(RocketMQMessageListener.class);

        String consumerGroup = this.environment.resolvePlaceholders(annotation.consumerGroup());
        String topic = this.environment.resolvePlaceholders(annotation.topic());

        boolean listenerEnabled = (boolean) rocketMqProperties.getConsumer().getListeners()
                .getOrDefault(consumerGroup, Collections.EMPTY_MAP).getOrDefault(topic, true);

        if (!listenerEnabled) {
            log.debug(
                    "Consumer Listener (group:{},topic:{}) is not enabled by configuration, will ignore initialization.",
                    consumerGroup, topic);
            return;
        }
        validate(annotation);

        String containerBeanName = String.format("%s_%s", DefaultRocketMQListenerContainer.class.getName(),
                counter.incrementAndGet());
        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;

        genericApplicationContext.registerBean(containerBeanName, DefaultRocketMQListenerContainer.class,
                () -> createRocketMQListenerContainer(containerBeanName, bean, annotation));
        DefaultRocketMQListenerContainer container = genericApplicationContext.getBean(containerBeanName,
                DefaultRocketMQListenerContainer.class);
        if (!container.isRunning()) {
            try {
                container.start();
            } catch (Exception e) {
                log.error("Started container failed. {}", container, e);
                throw new RuntimeException(e);
            }
        }

        log.info("Register the listener to container, listenerBeanName:{}, containerBeanName:{}", beanName,
                containerBeanName);
    }

    private DefaultRocketMQListenerContainer createRocketMQListenerContainer(String name, Object bean,
                                                                             RocketMQMessageListener annotation) {
        DefaultRocketMQListenerContainer container = new DefaultRocketMQListenerContainer();

        container.setRocketMQMessageListener(annotation);

        String nameServer = environment.resolvePlaceholders(annotation.nameServer());
        nameServer = StringUtils.isEmpty(nameServer) ? rocketMqProperties.getNameServer() : nameServer;
        String accessChannel = environment.resolvePlaceholders(annotation.accessChannel());
        container.setNameServer(nameServer);
        if (!StringUtils.isEmpty(accessChannel)) {
            container.setAccessChannel(AccessChannel.valueOf(accessChannel));
        }
        container.setTopic(environment.resolvePlaceholders(annotation.topic()));
        String tags = environment.resolvePlaceholders(annotation.selectorExpression());
        if (!StringUtils.isEmpty(tags)) {
            container.setSelectorExpression(tags);
        }
        container.setConsumerGroup(environment.resolvePlaceholders(annotation.consumerGroup()));
        if (RocketMQListener.class.isAssignableFrom(bean.getClass())) {
            container.setRocketMQListener((RocketMQListener) bean);
        } else if (RocketMQReplyListener.class.isAssignableFrom(bean.getClass())) {
            container.setRocketMQReplyListener((RocketMQReplyListener) bean);
        }
        container.setMessageConverter(rocketMqMessageConverter.getMessageConverter());
        container.setName(name);

        return container;
    }

    private void validate(RocketMQMessageListener annotation) {
        if (annotation.consumeMode() == ConsumeMode.ORDERLY && annotation.messageModel() == MessageModel.BROADCASTING) {
            throw new BeanDefinitionValidationException(
                    "Bad annotation definition in @RocketMQMessageListener, messageModel BROADCASTING does not support ORDERLY message!");
        }
    }
}

