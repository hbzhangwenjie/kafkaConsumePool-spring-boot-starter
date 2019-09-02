package com.zwj.config;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zwj.kafkaListeneradvice.ConsumeAnnotationBeanPostProcessor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zwj
 * @Date: 2019-08-30 20:28
 */

@Configuration
@EnableConfigurationProperties(KafkaConsumePoolProperties.class)
@ConditionalOnProperty(prefix = "kafka.consume.pool", value = "enabled")
public class KafkaConsumePoolConfig {

    private  KafkaConsumePoolProperties kafkaConsumePoolProperties;

    public KafkaConsumePoolConfig(KafkaConsumePoolProperties kafkaConsumePoolProperties) {
        this.kafkaConsumePoolProperties = kafkaConsumePoolProperties;
    }

    private ThreadPoolExecutor getKafkaThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("handle-kafka-pool-%d").build();
        ThreadPoolExecutor handelKafkaThreadPool = new ThreadPoolExecutor(kafkaConsumePoolProperties.getCoreSize(),
                kafkaConsumePoolProperties.getMaxSize(), kafkaConsumePoolProperties.getKeepAliveTime(), TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(kafkaConsumePoolProperties.getQueueSize()), namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        //如有监控加入监控
        return handelKafkaThreadPool;
    }

    @Bean
    public ConsumeAnnotationBeanPostProcessor consumeAnnotationBeanPostProcessor() {
        return new ConsumeAnnotationBeanPostProcessor(getKafkaThreadPool());
    }


}
