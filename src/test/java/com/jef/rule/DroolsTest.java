package com.jef.rule;

import com.jef.entity.ComparisonOperatorEntity;
import com.jef.entity.Order;
import com.jef.entity.User;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tufujie
 * @date 2023/8/21
 */
public class DroolsTest {

    @Test
    public void bookDiscountTest() {
        KieServices kieServices = KieServices.Factory.get();
        // 获取Kie容器对象（默认容器对象
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        // 从Kie容器对象中获取会话对象（默认session对象
        KieSession kieSession = kieContainer.newKieSession();

        Order order = new Order();
        order.setOriginalPrice(160d);

        User user = new User();
        user.setAge(30);
        user.setGender("male");

        // 将order对象插入工作内存
        kieSession.insert(order);
        kieSession.insert(user);

        System.out.println("匹配规则前优惠后价格：" + order.getRealPrice());

        // 匹配对象
        // 激活规则，由drools框架自动进行规则匹配。若匹配成功，则执行
        kieSession.fireAllRules();

        // 关闭会话
        kieSession.dispose();

        System.out.println("优惠前价格：" + order.getOriginalPrice() + "\n优惠后价格：" + order.getRealPrice());
    }

    @Test
    public void test() {
        KieServices kieServices = KieServices.Factory.get();
        // 获取Kie容器对象（默认容器对象
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        // 从Kie容器对象中获取会话对象（默认session对象
        KieSession kieSession = kieContainer.newKieSession();

        ComparisonOperatorEntity fact = new ComparisonOperatorEntity();
        String name = "Jef";
        fact.setName(name);

        List<String> list = new ArrayList<String>();
        list.add("Jef");
        list.add("Other");
        fact.setList(list);
        // 将order对象插入工作内存
        kieSession.insert(fact);

        // 匹配对象
        // 激活规则，由drools框架自动进行规则匹配。若匹配成功，则执行
        kieSession.fireAllRules();

        System.out.println("------");
        // 通过规则过滤器实现只执行指定规则
        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("rule_comparison_memberOf"));

        // 关闭会话
        kieSession.dispose();

    }
}