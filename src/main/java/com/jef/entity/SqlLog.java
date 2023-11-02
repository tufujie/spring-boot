package com.jef.entity;

/**
 * @author Jef
 * @date: 2021/4/15 19:10
 */
public class SqlLog {

    private String requestId;
    private String methodName;
    private String methodClass;
    private String requestUrl;
    private String remoteIp;
    private String sql;
    private Long loadTime;

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

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Long getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Long loadTime) {
        this.loadTime = loadTime;
    }

    @Override
    public String toString() {
        return "SQL日志：SQL = " + this.getSql();
    }
}