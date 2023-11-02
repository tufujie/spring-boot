package com.jef.entity;

/**
 * @author Jef
 * @date: 2021/4/15 19:15
 */
public class ExceptionLog {

    private String requestId;
    private String methodName;
    private String methodClass;
    private String requestUrl;
    private String remoteIp;
    private String message;
    private String stackTraceMessage;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodClass() {
        return methodClass;
    }

    public void setMethodClass(String methodClass) {
        this.methodClass = methodClass;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTraceMessage() {
        return stackTraceMessage;
    }

    public void setStackTraceMessage(String stackTraceMessage) {
        this.stackTraceMessage = stackTraceMessage;
    }

    @Override
    public String toString() {
        return "异常日志：\nrequestUrl = " + this.getRequestUrl() + "\nmessage=" + message + "\nstackTraceMessage=\n" + stackTraceMessage;
    }
}