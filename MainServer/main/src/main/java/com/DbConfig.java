package com;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;
import org.springframework.context.annotation.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com"})
//Конфигурация транзакций
public class DbConfig
{
    JtaTransactionManager jtaTransactionManager;
    @Bean(name = "dataSourceOne")
    public DataSource dataSourceOne() {
        PoolingDataSource ds = new PoolingDataSource();

        ds.setClassName("org.postgresql.xa.PGXADataSource");
        ds.setUniqueName("ds1");
        ds.setMaxPoolSize(10);
        Properties props = new Properties();
        props.put("url", "jdbc:postgresql://localhost:5433/LabOneBusinessLogic");
        props.put("user", "postgres");
        props.put("password", "123456789");

        ds.setDriverProperties(props);
        ds.setAllowLocalTransactions(true);

        ds.init();
        return ds;
    }
    @Bean(name="bitronixManager",destroyMethod = "shutdown")
    public BitronixTransactionManager bitronixManager() {
        return TransactionManagerServices.getTransactionManager();
    }


    @Bean(name="transactionManager")
    public JtaTransactionManager jtaTransactionManager() {
        JtaTransactionManager jta = new JtaTransactionManager();
        jta.setTransactionManager(bitronixManager());
        jta.setUserTransaction(bitronixManager());
        jtaTransactionManager=jta;
        return jta;
    }
    @Bean(name="transactionTemplate")
    public TransactionTemplate transactionTemplate() {
        TransactionTemplate transactionTemplate= new TransactionTemplate(jtaTransactionManager());
        return transactionTemplate;
    }

}
