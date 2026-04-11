package com.weichat.api.test;

import com.weichat.api.util.MessageTemplateUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 群发消息功能测试类
 */
public class MassMessageTest {

    public static void main(String[] args) {
        // 测试消息模板功能
        testMessageTemplate();
    }

    /**
     * 测试消息模板渲染功能
     */
    public static void testMessageTemplate() {
        System.out.println("=== 测试消息模板功能 ===");
        
        // 测试简单变量替换
        String template = "你好，{{name}}！欢迎加入我们的团队。";
        String result = MessageTemplateUtil.renderTemplate(template, "张三");
        System.out.println("模板: " + template);
        System.out.println("结果: " + result);
        
        // 测试多变量替换
        template = "{{name}}，你的ID是{{id}}，欢迎使用我们的服务！";
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "李四");
        variables.put("id", 12345L);
        result = MessageTemplateUtil.renderTemplate(template, variables);
        System.out.println("\n模板: " + template);
        System.out.println("结果: " + result);
        
        // 测试包含特殊字符的情况
        template = "你好，{{name}}！今天是{{date}}。";
        variables.clear();
        variables.put("name", "Special\\Char$Tester");
        variables.put("date", "2023-01-01");
        result = MessageTemplateUtil.renderTemplate(template, variables);
        System.out.println("\n模板: " + template);
        System.out.println("结果: " + result);
        
        System.out.println("\n=== 消息模板功能测试完成 ===");
    }
}