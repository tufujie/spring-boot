package com.jef.common.interceptor;

import com.jef.common.annotations.MyAnnotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Jef
 * @date 2023/11/2
 */
@Aspect
@Component
public class MyAnnotationAspect {
    private final static Logger LOGGER = LoggerFactory.getLogger(MyAnnotationAspect.class);

    @Pointcut("@annotation(com.jef.common.annotations.MyAnnotation)")
    public void syncMyAnnotation() {
    }

    @Before("syncMyAnnotation()")
    public void doBefore(JoinPoint joinPoint) {
        MyAnnotation myAnnotation = getMyAnnotationAnnotation(joinPoint);
        String value = myAnnotation.value();
        String menuId = myAnnotation.menuId();
        LOGGER.info("@MyAnnotation before msg: {}, menuId: {}", value, menuId);
        //do something
    }


    @After(value = "syncMyAnnotation() && @annotation(myAnnotation)")
    public void testAfter(MyAnnotation myAnnotation) {
        String value = myAnnotation.value();
        String menuId = myAnnotation.menuId();
        LOGGER.info("@MyAnnotation after msg: {}, menuId: {}", value, menuId);
        //do something
    }

    @AfterReturning(pointcut = "syncMyAnnotation()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, String result) {

        System.out.println("方法执行完执行...afterRunning");
        System.out.println("返回数据：{}" + result);

        System.out.println("方法执行完执行...afterRunning，执行完毕了:" + result);
    }

    @AfterThrowing(value = "syncMyAnnotation()")
    public void afterThrowing(JoinPoint joinPoint) {
        System.out.println("异常出现之后...afterThrowing");
    }

    private MyAnnotation getMyAnnotationAnnotation(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        Signature sig = joinPoint.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }

        MyAnnotation myAnnotation = method.getAnnotation(MyAnnotation.class);
        return myAnnotation;
    }
}