package com.jef.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 通过apollo获取某个前缀的所有配置,转换为map
 */
public class ApolloUtil {

    private static Logger logger = LoggerFactory.getLogger(ApolloUtil.class);

    /**
     * 获取某个key的配置
     *
     * @param namespace
     * @param configKey
     * @return
     */
    public static String getApolloConfig(String namespace, String configKey, String defaultValue) {
        Config config = ConfigService.getConfig(namespace);
        if (config != null) {
            return config.getProperty(configKey, defaultValue);
        }
        return null;
    }

    /**
     * 将apollo某个前缀的值，转换为对象
     *
     * @param namespace
     * @param apolloPrefix
     * @return
     */
    public static Properties getApolloConfigProperties(String namespace, String apolloPrefix) {
        Config config = ConfigService.getConfig(namespace);
        Properties properties = new Properties();
        if (config != null) {
            Iterator<String> keyIt = config.getPropertyNames().iterator();
            while (keyIt.hasNext()) {
                String fullKey = keyIt.next();
                if (StrUtil.isNotEmpty(fullKey) && fullKey.indexOf(apolloPrefix) == 0) {
                    String key = fullKey.substring(apolloPrefix.length() + 1);
                    properties.put(key, config.getProperty(fullKey, ""));
                }
            }
        }
        logger.info("获取{},{},配置properties为:{}", namespace, apolloPrefix, properties);
        return properties;
    }

    /**
     * 将apollo某个前缀的值，转换为对象
     *
     * @param namespace
     * @param apolloPrefix
     * @return
     */
    public static <T> T getApolloConfig(String namespace, String apolloPrefix, Class<T> configClass) {
        Map<String, Object> configMap = getApolloConfig(namespace, apolloPrefix);
        if (configMap != null) {
            return BeanUtil.mapToBean(configMap, configClass, false);
        }
        return null;
    }

    /**
     * 获取某个命名空间的配置,树形结构
     *
     * @param namespace
     * @param apolloPrefix
     * @return
     */
    public static Map<String, Object> getApolloConfig(String namespace, String apolloPrefix) {
        Config config = ConfigService.getConfig(namespace);
        if (config != null) {
            Iterator<String> keyIt = config.getPropertyNames().iterator();
            Map<String, Object> properties = new HashMap<String, Object>();
            while (keyIt.hasNext()) {
                String fullKey = keyIt.next();
                if (StrUtil.isNotEmpty(fullKey) && fullKey.indexOf(apolloPrefix) == 0) {
                    String key = fullKey.substring(apolloPrefix.length() + 1);
                    String value = config.getProperty(fullKey, "");
                    String keys[] = key.split("\\.");
                    if (keys.length == 1) {
                        properties.put(keys[0], value);
                    } else if (keys.length > 1) {
                        Map map = (Map) properties.get(keys[0]);
                        if (map == null) {
                            map = new HashMap<String, Object>();
                            properties.put(keys[0], map);
                        }

                        int maxInx = keys.length - 1;
                        for (int i = 1; i < keys.length; i++) {
                            String subKey = keys[i];
                            Object subMap = properties.get(subKey);
                            if (subMap == null) {
                                if (i == maxInx) {
                                    map.put(subKey, value);
                                } else {
                                    subMap = new HashMap<String, Object>();
                                    map.put(subKey, subMap);
                                    map = (Map) subMap;
                                }
                            } else if (subMap instanceof Map) {
                                map = (Map) subMap;
                            }
                        }
                    }
                }
            }
            logger.info("获取{},{},配置map为:{}", namespace, apolloPrefix, properties);
            return properties;
        }
        return null;
    }
}