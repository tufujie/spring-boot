package com.jef.common.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * 自定义标准分库策略
 *
 * @author Jef
 * @date 2023/11/6
 */
public class MyDBPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> databaseNames, PreciseShardingValue<Long> shardingValue) {

        /**
         * databaseNames 所有分片库的集合
         * shardingValue 为分片属性，其中 logicTableName 为逻辑表，columnName 分片健（字段），value 为从 SQL 中解析出的分片健的值
         */
        String value = shardingValue.getValue() % databaseNames.size() + "";
        for (String databaseName : databaseNames) {
            if (databaseName.endsWith(value)) {
                System.out.println("开始doShardingDB，databaseName=" + databaseName);
                return databaseName;
            }
        }
        throw new IllegalArgumentException();
    }
}
