package com.jef.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 类单例
 *
 * @author tufujie
 * @date 2023/8/18
 */
public class ClassInstance {
    private static Map<String, Class> uniqueInstance = new HashMap();

    /**
     * 必须是私有的，防止实例化
     */
    private ClassInstance() {
        System.out.println("实例化单例对象");
    }

    /**
     * 定义一个方法来为客户端提供类实例
     *
     * @return
     */
    public static synchronized Class getInstance(String className) throws ClassNotFoundException {
        Class clazz = uniqueInstance.get(className);
        if (clazz == null) {
            uniqueInstance.put(className, Class.forName(className));
        }
        return uniqueInstance.get(className);
    }
}