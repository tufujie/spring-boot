package com.jef.controller;


import com.jef.constant.CommonConstant;
import com.jef.entity.User;
import com.jef.service.BCmp;
import com.jef.util.ThreadLocalUtil;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.core.FlowExecutorHolder;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.property.LiteflowConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author Jef
 * @date 2023/08/16
 */
@Controller
@RequestMapping("/liteFlow")
public class LiteFlowController {
    @Resource
    private FlowExecutor flowExecutor;

    @ResponseBody
    @RequestMapping("/testConfig")
    public String testConfig() {
        LiteflowResponse response = flowExecutor.execute2Resp("chain1", "arg");
        return "success";
    }

    @ResponseBody
    @RequestMapping("/checkProcessSeq")
    public String checkProcessSeq(@RequestParam String chainId) {
        String[] chainIdArray = chainId.split(",");
        String result = "";
        for (String chanIdTemp : chainIdArray) {
            LiteflowResponse response = flowExecutor.execute2Resp(chanIdTemp, "arg");
            result += getProcessSeq(response, "验证") + "<br/>";
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/loadRuleSource")
    public String loadRuleSource(@RequestParam String fileName, @RequestParam String chainId) {
        LiteflowConfig config = new LiteflowConfig();
        config.setRuleSource("config/" + fileName + ".xml");
        FlowExecutor flowExecutor = FlowExecutorHolder.loadInstance(config);
        LiteflowResponse response = flowExecutor.execute2Resp(chainId, "arg");
        return getProcessSeq(response, "加载文件验证");
    }

    private String getProcessSeq(LiteflowResponse response, String business) {
        StringBuffer sb = new StringBuffer(business + "执行顺序：执行顺序为：");
        int size = response.getExecuteStepQueue().size();
        for (int i = 0; i < size; i++) {
            sb.append(response.getExecuteStepQueue().poll().getNodeId());
            if (i < size - 1) {
                sb.append("->");
            }
        }
        return sb.toString();
    }

    @ResponseBody
    @RequestMapping("/balanceLoadRuleSource")
    public String balanceLoadRuleSource(@RequestParam String fileName, @RequestParam String fileNameTwo, @RequestParam String chainId) {
        LiteflowConfig config = new LiteflowConfig();
        // 变更前的规则
        config.setRuleSource("config/" + fileName + ".xml");
        FlowExecutor flowExecutorOne = FlowExecutorHolder.loadInstance(config);
        LiteflowResponse response = flowExecutorOne.execute2Resp(chainId, "arg");
        String before = getProcessSeq(response, "变更前");
        FlowExecutorHolder.clean();
        // 变更后的规则
        LiteflowConfig configTwo = new LiteflowConfig();
        configTwo.setRuleSource("config/" + fileNameTwo + ".xml");
        FlowExecutor flowExecutorTwo = FlowExecutorHolder.loadInstance(configTwo);
        LiteflowResponse responseTwo = flowExecutorTwo.execute2Resp(chainId, "arg");
        String after = getProcessSeq(responseTwo, "变更后");
        return before + "<br/>" + after;
    }

    @ResponseBody
    @RequestMapping("/outManyProperties")
    public String outManyProperties(@RequestParam String fileName, @RequestParam String chainId) {
        LiteflowConfig config = new LiteflowConfig();
        config.setRuleSource("config/" + fileName + ".xml");
        FlowExecutor flowExecutor = FlowExecutorHolder.loadInstance(config);
        LiteflowResponse response = flowExecutor.execute2Resp(chainId, "arg");
        return getOutOfProperties(response, "输出文件属性");
    }

    private String getOutOfProperties(LiteflowResponse response, String business) {
        StringBuffer sb = new StringBuffer(business + "<br/>");
        response.getExecuteStepQueue().stream().forEach(obj -> {
            sb.append("nodeId=" + obj.getNodeId() + ";");
            sb.append("nodeName=" + obj.getNodeName() + ";");
            sb.append("tag=" + obj.getTag() + ";");
            sb.append("class=" + obj.getClass() + ";");
            sb.append("timeSpent=" + obj.getTimeSpent() + ";");
            sb.append("stepType=" + obj.getStepType() + ";");
            sb.append("<br/>");
        });
        try {
            BCmp bCmp = response.getContextBean(BCmp.class);
            sb.append("chainId=" + bCmp.getChainId());
            sb.append("name=" + bCmp.getName());
            sb.append("chainName=" + bCmp.getChainName());
            sb.append("requestData=" + bCmp.getRequestData());
            sb.append("tag=" + bCmp.getTag());
        } catch (Exception e) {

        }
        return sb.toString();
    }

    @ResponseBody
    @RequestMapping("/businessUse")
    public String businessUse(@RequestParam String fileName, @RequestParam String chainId) {
        for (int i = 1; i < 3; i++) {
            int finalI = i;
            Thread thread1 = new Thread(() -> {
                // 设置上下文开始
                User user = new User();
                user.setPhone(CommonConstant.PHONE);
                user.setName(CommonConstant.NAME);
                user.setId(Long.valueOf(finalI));
                user.setDescrription("设置时的线程名称=" + Thread.currentThread().getName() + "，值为=" + CommonConstant.NAME + finalI);
                ThreadLocalUtil.setThreadLocalUser(user);
                // 设置上下文结束
                LiteflowConfig config = new LiteflowConfig();
                config.setRuleSource("config/" + fileName + ".xml");
                FlowExecutor flowExecutor = FlowExecutorHolder.loadInstance(config);
                LiteflowResponse response = flowExecutor.execute2Resp(chainId, "arg");
            });
            thread1.start();
        }
        return "success";
    }
}