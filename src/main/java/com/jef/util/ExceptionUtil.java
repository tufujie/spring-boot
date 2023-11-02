package com.jef.util;

/**
 * 异常处理
 *
 * @author Jef
 * @date 2019/5/8
 */
public class ExceptionUtil {
    /**
     * 获取异常信息
     * @author Jef
     * @date 2019/8/20
     * @param msg
     * @param e
     * @return void
     */
    public static String getExceptionStackTraceMessage(String msg, Exception e) {
        StringBuffer sb = new StringBuffer();
        if (e == null) {
            return sb.toString();
        }
        if (msg != null) {
            sb.append(msg).append("\n");
        }
        return getExceptionStackTraceMessage(e.getStackTrace(), e, sb);
    }

    /**
     * 获取异常信息
     * @author Jef
     * @date 2021/4/15
     * @param m
     * @param e
     * @param sb
     * @return java.lang.String
     */
    public static String getExceptionStackTraceMessage(StackTraceElement[] m, Throwable e, StringBuffer sb) {
        for (StackTraceElement ste : m) {
            sb.append(ste.toString());
            sb.append("\n");
        }
        sb.append("\n");
        sb.append(e.getCause());
        return sb.toString();
    }

}