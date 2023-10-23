package com.jef.rule;

import com.jef.entity.EasyRule;
import com.jef.entity.EasyRuleFact;
import com.jef.entity.Person;
import com.jef.util.ClassInstance;
import com.jef.util.EasyRuleUtil;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.InferenceRulesEngine;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tufujie
 * @date 2023/8/21
 */
public class EasyRuleTest {

    /**
     * 晴天，下雨测试
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     */
    @Test
    public void testSunAndRain() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException, InstantiationException {
        // 定义规则列表，一次性全部定义，可以从数据库直接配置，配置类和规则，这里模拟从数据库取出已经配置的多个
        List<EasyRule> easyRuleList = new ArrayList<>();
        EasyRule easyRuleRain = new EasyRule();
        easyRuleRain.setRuleName("getWeatherRule");
        easyRuleRain.setClassName("com.jef.util.EasyRuleUtil");

        EasyRule easyRuleSun = new EasyRule();
        easyRuleSun.setRuleName("getSunRule");
        easyRuleSun.setClassName("com.jef.util.EasyRuleUtil");

        easyRuleList.add(easyRuleRain);
        easyRuleList.add(easyRuleSun);

        Rules rules = new Rules();
        for (EasyRule easyRule : easyRuleList) {
            Class clazz = ClassInstance.getInstance(easyRule.getClassName());
            Object obj = clazz.newInstance();
            Method method = clazz.getDeclaredMethod(easyRule.getRuleName());
            rules.register(method.invoke(obj));
        }

        RulesEngine rulesEngine = new DefaultRulesEngine();
        // 在已知的事实上执行规则
        // 定义事实列表
        Facts facts = new Facts();
        // 传参，将业务的参数传进来
        facts.put("rain", true);
        rulesEngine.fire(rules, facts);
        System.out.println("------");

        facts = new Facts();
        facts.put("sun", true);
        rulesEngine.fire(rules, facts);
        System.out.println("------");

        // 组合事实，要应用的地方，配置不同的key和value在数据库，然后可以在数据库直接拉取规则
        System.out.println("开始组合");
        String businessNameKey = "business1";
        List<EasyRuleFact> easyRuleFactList = new ArrayList<>();
        EasyRuleFact easyRuleFactRain = new EasyRuleFact();
        easyRuleFactRain.setBusinessNameKey(businessNameKey);
        easyRuleFactRain.setFact("rain");

        EasyRuleFact easyRuleFactSun = new EasyRuleFact();
        easyRuleFactSun.setBusinessNameKey(businessNameKey);
        easyRuleFactSun.setFact("sun");

        easyRuleFactList.add(easyRuleFactRain);
        easyRuleFactList.add(easyRuleFactSun);

        facts = new Facts();
        for (EasyRuleFact easyRuleFact : easyRuleFactList) {
            facts.put(easyRuleFact.getFact(), true);
        }
        rulesEngine.fire(rules, facts);
    }


    /**
     * 店铺买酒测试
     */
    @Test
    public void testShop() {
        // 创建一个实际事实的示例
        Person tom = new Person("Tom", 14);
        Facts facts = new Facts();
        facts.put("person", tom);

        // 创建规则
        Rule ageRule = EasyRuleUtil.getAgeRule();
        Rule alcoholRule = EasyRuleUtil.getAlcoholRule();
        Rule acceptedAuditRule = EasyRuleUtil.getAlcoholAuditRule();

        // 注册规则集合
        Rules rules = new Rules();
        rules.register(ageRule);
        rules.register(alcoholRule);
        rules.register(acceptedAuditRule);

        // 创建默认的规则引擎并执行规则
        RulesEngine rulesEngine = new DefaultRulesEngine();
        System.out.println("Tom: 你好! 我可以买些酒吗?");
        rulesEngine.fire(rules, facts);

        // 创建一个实际事实的示例
        Person jef = new Person("Jef", 20);
        facts.put("person", jef);

        System.out.println("Jef: 你好! 我可以买些酒吗?");
        rulesEngine.fire(rules, facts);
    }

    @Test
    public void testAirCondition() {
        // 定义事实
        Facts facts = new Facts();
        facts.put("temperature", 30);

        // 定义规则
        Rule airConditioningRule = EasyRuleUtil.getTemperatureRule();
        Rules rules = new Rules();
        rules.register(airConditioningRule);

        // 已知事实基础上执行规则
        RulesEngine rulesEngine = new InferenceRulesEngine();
        rulesEngine.fire(rules, facts);
    }
}