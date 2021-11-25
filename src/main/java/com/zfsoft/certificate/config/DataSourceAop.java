package com.zfsoft.certificate.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.hint.HintManager;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 结合MyDatasourceRoutingAlgorithm自定义强制路由策略，实现自动切换数据源
 * 
 * @author 86131
 */
@Slf4j
@Aspect
@Order(1)
@Component
public class DataSourceAop {

    /**
     * db1库切入点,读写分离又shadingjdbc控制
     */
    @Pointcut("execution(* com.zfsoft.certificate.mapper.db1..*.*(..))")
    public void switchDataSourceDb1() {
    }

    @Before("switchDataSourceDb1()")
    public void doDb1Before() {
        HintManager hintManager = HintManager.getInstance();
        hintManager.setDatabaseShardingValue("db1");
    }

    /**
     * 恢复默认数据源
     */
    @After("switchDataSourceDb1()")
    public void doDb1after() {
        //清理掉当前设置的数据源，让默认的数据源不受影响
        HintManager.clear();
    }


    /**
     * db2库切入点,读写分离又shadingjdbc控制
     */
    @Pointcut("execution(* com.zfsoft.certificate.mapper.db2..*.*(..))")
    public void switchDataSourceDb2() {
    }

    @Before("switchDataSourceDb2()")
    public void doDb2Before() {
        HintManager hintManager = HintManager.getInstance();
        hintManager.setDatabaseShardingValue("db2");
    }

    @After("switchDataSourceDb2()")
    public void doDb2after() {
        HintManager.clear();
    }


    /**
     * db3库切入点,读写分离又shadingjdbc控制
     */
    @Pointcut("execution(* com.zfsoft.certificate.mapper.db3..*.*(..))")
    public void switchDataSourceDb3() {
    }

    @Before("switchDataSourceDb3()")
    public void doDb3Before() {
        HintManager hintManager = HintManager.getInstance();
        hintManager.setDatabaseShardingValue("db3");
    }

    @After("switchDataSourceDb3()")
    public void doDb3after() {
        HintManager.clear();
    }


    /**
     * db4库切入点,读写分离又shadingjdbc控制
     */
    @Pointcut("execution(* com.zfsoft.certificate.mapper.db4..*.*(..))")
    public void switchDataSourceDb4() {
    }

    @Before("switchDataSourceDb4()")
    public void doDb4Before() {
        HintManager hintManager = HintManager.getInstance();
        hintManager.setDatabaseShardingValue("db4");
    }

    @After("switchDataSourceDb4()")
    public void doDb4after() {
        HintManager.clear();
    }

    /**
     * db5库切入点,读写分离又shadingjdbc控制
     */
    @Pointcut("execution(* com.zfsoft.certificate.mapper.db5..*.*(..))")
    public void switchDataSourceDb5() {
    }

    @Before("switchDataSourceDb5()")
    public void doDb5Before() {
        HintManager hintManager = HintManager.getInstance();
        hintManager.setDatabaseShardingValue("db5");
    }

    @After("switchDataSourceDb5()")
    public void doDb5after() {
        HintManager.clear();
    }

    /**
     * db6库切入点,读写分离又shadingjdbc控制
     */
    @Pointcut("execution(* com.zfsoft.certificate.mapper.db6..*.*(..))")
    public void switchDataSourceDb6() {
    }

    @Before("switchDataSourceDb6()")
    public void doDb6Before() {
        HintManager hintManager = HintManager.getInstance();
        hintManager.setDatabaseShardingValue("db6");
    }

    @After("switchDataSourceDb6()")
    public void doDb6after() {
        HintManager.clear();
    }

}
