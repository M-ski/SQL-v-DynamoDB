package com.mski.spring.jv.demo.locks;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.stereotype.Component;

@Component
public class LockInterceptor extends AbstractPointcutAdvisor {

    public static final ComposablePointcut POINTCUT = new ComposablePointcut(new AnnotationMatchingPointcut(null, LockWith.class, true));
    public static final LockInterceptorAdvice ADVICE_INSTANCE = new LockInterceptorAdvice();


    @Override
    public Pointcut getPointcut() {
        return POINTCUT;
    }

    @Override
    public Advice getAdvice() {
        return ADVICE_INSTANCE;
    }

    @Slf4j
    private static class LockInterceptorAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("Locking...");
            return invocation.proceed();
        }
    }
}
