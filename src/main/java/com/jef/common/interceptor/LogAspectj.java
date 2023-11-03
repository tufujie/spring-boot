package com.jef.common.interceptor;

import com.jef.entity.ExceptionLog;
import com.jef.entity.SqlLog;
import com.jef.entity.UserLog;
import com.jef.util.ExceptionUtil;
import com.jef.util.SqlUtils;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 控制器切面
 * 非常经典的切面写法
 *
 * @author Jef
 * @date 2021/4/15
 */
@Aspect
@Component
public class LogAspectj {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    private static final Logger logger = LogManager.getLogger(LogAspectj.class);

    /**
     * 权限管理
     */
    @Pointcut("execution(* com.jef.controller.*.*(..))")
    public void userPermission() {
    }

    /**
     * 用户日志记录
     *
     * @Pointcut : 创建一个切点，方便同一方法的复用。
     * value属性就是AspectJ表达式，
     */
    @Pointcut("execution(* com.jef.controller.*.*(..))")
//    @Pointcut("@annotation(com.jef.annotation.LogAnno)")
    public void userLog() {
    }

    @Pointcut("execution(* com.jef.dao.*.*(..))")
    public void sqlLog() {
    }

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void transactionalControl() {
    }

    @Pointcut("execution(* com.jef.controller.*.*(..))")
    public void exceptionLog() {
    }

    //前置通知
    //指定该方法是前置通知，并指定切入点
    @Before("userLog()")
    // 等效于下面的写法，此时切入点不要
//    @Before("execution(* com.jef.controller.*.*(..))")
    public void userLog(JoinPoint pj) {
        try {
            UserLog userLog = new UserLog();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String method = request.getMethod();
            Signature signature = pj.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method targetMethod = methodSignature.getMethod();
            if ("POST".equals(method) || "GET".equals(method)) {
                String ipAddress = getIpAddress(request);
                String requestId = (String) request.getAttribute("requestId");
                // 根据请求参数或请求头判断是否有“requestId”，有则使用，无则创建
                if (StringUtils.isEmpty(requestId)) {
                    requestId = "req_" + System.currentTimeMillis();
                    request.setAttribute("requestId", requestId);
                }
                //请求id
                userLog.setRequestId(requestId);
                //方法名
                userLog.setMethodName(targetMethod.getName());
                //方法所在的类名
                userLog.setMethodClass(signature.getDeclaringTypeName());
                //请求URI
                userLog.setRequestUrl(request.getRequestURL().toString());
                //操作IP地址
                userLog.setRemoteIp(ipAddress);
                System.out.println(userLog);
//                logService.saveUserLog(userLog);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    //环绕通知
    @Around("sqlLog()")
    public Object sqlLog(ProceedingJoinPoint pj) throws Throwable {
        // 发送异步日志事件
        long start = System.currentTimeMillis();
        SqlLog sqlLog = new SqlLog();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Signature signature = pj.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        String ipAddress = getIpAddress(request);
        String requestId = (String) request.getAttribute("requestId");
        // 根据请求参数或请求头判断是否有“requestId”，有则使用，无则创建
        if (StringUtils.isEmpty(requestId)) {
            requestId = "req_" + System.currentTimeMillis();
            request.setAttribute("requestId", requestId);
        }
        //执行方法
        Object object = pj.proceed();
        //获取sql
        String sql = SqlUtils.getMybatisSql(pj, sqlSessionFactory);
        //执行时长(毫秒)
        long loadTime = System.currentTimeMillis() - start;
        //请求id
        sqlLog.setRequestId(requestId);
        //方法名
        sqlLog.setMethodName(targetMethod.getName());
        //方法所在的类名
        sqlLog.setMethodClass(signature.getDeclaringTypeName());
        //请求URI
        sqlLog.setRequestUrl(request.getRequestURL().toString());
        //操作IP地址
        sqlLog.setRemoteIp(ipAddress);
        //sql
        sqlLog.setSql(sql);
        //执行时间
        sqlLog.setLoadTime(loadTime);
        System.out.println(sqlLog);
//        logService.saveSqlLog(sqlLog);
        return object;
    }

    //异常通知 用于拦截异常日志
    @AfterThrowing(pointcut = "exceptionLog()", throwing = "e")
    public void exceptionLog(JoinPoint pj, Throwable e) {
        try {
            ExceptionLog exceptionLog = new ExceptionLog();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            Signature signature = pj.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method targetMethod = methodSignature.getMethod();
            String ipAddress = getIpAddress(request);
            String requestId = (String) request.getAttribute("requestId");
            // 根据请求参数或请求头判断是否有“requestId”，有则使用，无则创建
            if (StringUtils.isEmpty(requestId)) {
                requestId = "req_" + System.currentTimeMillis();
                request.setAttribute("requestId", requestId);
            }
            //请求id
            exceptionLog.setRequestId(requestId);
            //方法名
            exceptionLog.setMethodName(targetMethod.getName());
            //方法所在的类名
            exceptionLog.setMethodClass(signature.getDeclaringTypeName());
            //请求URI
            exceptionLog.setRequestUrl(request.getRequestURL().toString());
            //异常信息
            exceptionLog.setMessage(e.getMessage());
            //操作IP地址
            exceptionLog.setRemoteIp(ipAddress);
            String stackTraceMessage = ExceptionUtil.getExceptionStackTraceMessage(e.getStackTrace(), e, new StringBuffer());
            exceptionLog.setStackTraceMessage(stackTraceMessage);
            System.out.println(exceptionLog);
//            logService.saveExceptionLog(exceptionLog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 获取IP地址的方法
     *
     * @param request 传一个request对象下来
     * @return
     */
    public String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    // 实际连接点方法
    @Before("userPermission()")
    public void userPermission(JoinPoint pj) {
        System.out.println("权限控制");
    }

    @Before("transactionalControl()")
    public void transactionalControl(JoinPoint pj) {
        System.out.println("事务控制");
    }
}