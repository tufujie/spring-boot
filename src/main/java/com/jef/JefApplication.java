package com.jef;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 使用 @ServletComponentScan注解后，Servlet、Filter、Listener
 * 可以直接通过 @WebServlet、@WebFilter、@WebListener 注解自动注册，无需其他代码
 * 默认扫描路径等同于@ComponentScan("com.jef.*") 可以不写@ComponentScan("com.jef.*")
 * 去除MultipartAutoConfiguration.class 不然自动定义Bean 导致上传不了文件
 *
 * @MapperScan("com.jef.*") 避免有些bean在MybatisConfig加载之前就加载导致异常
 * @ComponentScan("com.jef.*") 扫描路径指定对应的包可以指定 basePackages = {"com.jef.*.service.impl,com.jef.*.controller"...}
 * 避免Aspect,Compent,Config,扫不到，需要规范相关的包路径，目前扫描com.jef所有
 * @see MultipartAutoConfiguration
 * @see javax.servlet.MultipartConfigElement
 */
@ServletComponentScan
@ComponentScan("com.jef.*")
//@EnableDubbo(basePackages = {"com.jef.*.service.impl,com.jef.*.controller"})
//@EnableApolloConfig
@Configuration
@MapperScan("com.jef.dao") //扫描的mapper
@EnableDubbo(scanBasePackages = {"com.jef.service.impl*"})
@EnableAutoDataSourceProxy
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        ActiveMQAutoConfiguration.class,
        SolrAutoConfiguration.class,
        FreeMarkerAutoConfiguration.class,
        MailSenderAutoConfiguration.class,
        MultipartAutoConfiguration.class,
        RedisReactiveAutoConfiguration.class
})
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class JefApplication {

    public static void main(String[] args) {
        SpringApplication.run(JefApplication.class, args);
    }

}
