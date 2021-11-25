package com.zfsoft.certificate.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 86131
 */
@Slf4j
public class MyDatasourceRoutingAlgorithm implements HintShardingAlgorithm<String> {

    /**
     * 自定义Hint 实现算法
     * 能够保证绕过Sharding-JDBC SQL解析过程
     *
     * @param availableTargetNames
     * @param shardingValue        不再从SQL 解析中获取值，而是直接通过下面代码参数指定
     *                             HintManager hintManager = HintManager.getInstance();
     *                             hintManager.setDatabaseShardingValue("ds_exchange");
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, HintShardingValue<String> shardingValue) {
        /*log.info("+++++++++shardingValue++++++++" + shardingValue);*/
        List<String> shardingResult = new ArrayList<>();
        for (String value : shardingValue.getValues()) {
            if (availableTargetNames.contains(value)) {
                shardingResult.add(value);
            }
        }
        return shardingResult;
    }

}
