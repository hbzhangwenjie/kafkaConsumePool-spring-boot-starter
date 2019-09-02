package com.zwj.kafkaListeneradvice;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.util.ReflectionUtils;

/**
 * @Author: zwj
 * @Date: 2019-08-27 15:54
 */
public class ConsumeAnnotationBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor implements EnvironmentAware {

    private Executor executor;

    public ConsumeAnnotationBeanPostProcessor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        for (Method method : methods) {
            if (method.isAnnotationPresent(KafkaListener.class)) {
                ProxyFactory proxyFactory = prepareProxyFactory(bean, beanName);
                if (!proxyFactory.isProxyTargetClass()) {
                    evaluateProxyInterfaces(bean.getClass(), proxyFactory);
                }
                proxyFactory.addAdvisor(this.advisor);
                customizeProxyFactory(proxyFactory);
                return proxyFactory.getProxy(getProxyClassLoader());
            }
        }
        return bean;

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.advisor = new ConsumeAnnotationAdvisor(executor, environment);
    }
}
