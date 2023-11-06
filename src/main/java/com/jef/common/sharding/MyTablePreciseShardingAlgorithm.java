package com.jef.common.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * 自定义标准分表策略
 *
 * @author Jef
 * @date 2023/11/6
 */
public class MyTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<Long> shardingValue) {
        /**
         * tableNames 对应分片库中所有分片表的集合
         * shardingValue 为分片属性，其中 logicTableName 为逻辑表，columnName 分片健（字段），value 为从 SQL 中解析出的分片健的值
         */
        /**
         * 取模算法，分片健 % 表数量
         * 根据value值处理，根据实际情况，可以是按范围、按日期、取模、按特定字段
         */
        String value = shardingValue.getValue() % tableNames.size() + "";
        for (String tableName : tableNames) {
            if (tableName.endsWith(value)) {
                System.out.println("开始doShardingTable,tableName=" + tableName);
                return tableName;
            }
        }
        throw new IllegalArgumentException();
    }
}