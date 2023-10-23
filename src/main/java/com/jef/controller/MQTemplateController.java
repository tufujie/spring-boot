package com.jef.controller;

import com.alibaba.fastjson.JSONObject;
import com.jef.constant.CommonConstant;
import com.jef.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

/**
 * @author tufujie
 * @date 2023/9/7
 */
@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mq")
public class MQTemplateController {

    @Autowired
    private RocketMQTemplate rocketmqTemplate;

    @ResponseBody
    @RequestMapping("/syncSend")
    public String syncSend() {
        User user = new User();
        user.setName(CommonConstant.USER_NAME);
        String msgStr = JSONObject.toJSONString(user);
        Message<String> msg = MessageBuilder.withPayload(msgStr).build();
        String topic = "topic_name_jef_test";
        rocketmqTemplate.send(topic, msg);
        log.info("实时消息发送成功，topic:{}，发送时间：{}", topic, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").format(new Date()));
        return "success";
    }

    @ResponseBody
    @RequestMapping("/asyncSend")
    public String asyncSend() {
        User user = new User();
        user.setName(CommonConstant.USER_NAME);
        String msgStr = JSONObject.toJSONString(user);
        Message<String> msgs = MessageBuilder.withPayload(msgStr).build();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String topic = "topic_name_jef_test_asyncSend";
/*        默认值为“1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h”，18个level。
        可以配置自定义messageDelayLevel。注意，messageDelayLevel是broker的属性，不属于某个topic。发消息时，设置delayLevel等级即可：

        msg.setDelayLevel(level)。level有以下三种情况
        level == 0，消息为非延迟消息
        1<=level<=maxLevel，消息延迟特定时间，例如level1，延迟1s
        level > maxLevel，则level maxLevel，例如level==20，延迟2h*/
        // 延迟5秒
        int delayLevel = 2;
        rocketmqTemplate.asyncSend(topic, msgs, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
               log.info("发送成功：msgId={}", sendResult.getMsgId());
            }

            @Override
            public void onException(Throwable throwable) {

            }
        }, Duration.ofSeconds(5).toMillis(), delayLevel);
        log.info("延迟消息发送成功，topic:{}，发送时间：{}", topic, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").format(new Date()));
        return "success";
    }

}