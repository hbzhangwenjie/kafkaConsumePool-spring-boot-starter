package com.zwj.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: zwj
 * @Date: 2019-08-30 14:01
 */
@ConfigurationProperties(prefix = "kafka.consume.pool")
public class KafkaConsumePoolProperties {

    /**
     * 核心线程数
     */
    private int coreSize = 8;
    /**
     * 最大线程数
     */
    private int maxSize = 16;
    /**
     * 队列大小
     */
    private int queueSize = 32;
    /**
     * 空闲存活时间
     */
    private int keepAliveTimeSec = 60;
    /**
     * 是否启用线程池处理消息
     */
    private boolean enabled = false;

    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getKeepAliveTime() {
        return keepAliveTimeSec;
    }

    public void setKeepAliveTime(int keepAliveTimeSec) {
        this.keepAliveTimeSec = keepAliveTimeSec;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
