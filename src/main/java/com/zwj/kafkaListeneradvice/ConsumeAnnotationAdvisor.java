package com.zwj.kafkaListeneradvice;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @Author: zwj
 * @Date: 2019-08-27 15:47
 */
public class ConsumeAnnotationAdvisor extends AbstractPointcutAdvisor {

    private Advice advice;

    private Pointcut pointcut;

    public ConsumeAnnotationAdvisor(Executor executor, Environment environment) {
        Set<Class<? extends Annotation>> consumeAnnotationTypes = new LinkedHashSet<>();
        consumeAnnotationTypes.add(KafkaListener.class);
        this.advice = buildAdvice(executor,environment);
        this.pointcut = buildPointcut(consumeAnnotationTypes);
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    protected Pointcut buildPointcut(Set<Class<? extends Annotation>> consumeAnnotationTypes) {
        ComposablePointcut result = null;
        for (Class<? extends Annotation> consumeAnnotationType : consumeAnnotationTypes) {
            Pointcut mpc = AnnotationMatchingPointcut.forMethodAnnotation(consumeAnnotationType);
            if (result == null) {
                result = new ComposablePointcut(mpc);
            } else {
                result.union(mpc);
            }
        }
        return result;
    }

    protected Advice buildAdvice(Executor executor, Environment environment) {
        return new ConsumeAnnotationInterceptor(executor,environment);
    }
}
