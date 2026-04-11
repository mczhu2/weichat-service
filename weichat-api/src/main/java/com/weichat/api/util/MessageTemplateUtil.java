package com.weichat.api.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息模板工具类
 * 用于渲染消息模板，替换其中的变量
 *
 * @author weichat
 */
public class MessageTemplateUtil {

    /**
     * 渲染消息模板，将其中的变量替换为实际值
     *
     * @param templateContent 模板内容
     * @param variables 变量映射
     * @return 渲染后的消息内容
     */
    public static String renderTemplate(String templateContent, Map<String, Object> variables) {
        if (templateContent == null || variables == null || variables.isEmpty()) {
            return templateContent;
        }

        String result = templateContent;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            // 使用正则表达式匹配模板中的变量，格式为 {{variableName}}
            String variablePattern = "\\{\\{" + Pattern.quote(entry.getKey()) + "\\}\\}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            // 使用Matcher.quoteReplacement来正确处理替换字符串中的特殊字符
            result = result.replaceAll(variablePattern, java.util.regex.Matcher.quoteReplacement(value));
        }

        return result;
    }

    /**
     * 验证模板内容是否包含指定的变量
     *
     * @param templateContent 模板内容
     * @param variableNames 变量名列表
     * @return 是否包含所有变量
     */
    public static boolean containsVariables(String templateContent, String... variableNames) {
        if (templateContent == null || variableNames == null) {
            return false;
        }

        for (String variableName : variableNames) {
            String variablePattern = "\\{\\{" + Pattern.quote(variableName) + "\\}\\}";
            if (!Pattern.compile(variablePattern).matcher(templateContent).find()) {
                return false;
            }
        }

        return true;
    }
    
    /**
     * 使用接收者姓名渲染模板
     *
     * @param templateContent 模板内容
     * @param receiverName 接收者姓名
     * @return 渲染后的消息内容
     */
    public static String renderTemplate(String templateContent, String receiverName) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", receiverName);
        return renderTemplate(templateContent, variables);
    }
    
    /**
     * 使用接收者信息渲染模板
     *
     * @param templateContent 模板内容
     * @param receiverName 接收者姓名
     * @param receiverId 接收者ID
     * @return 渲染后的消息内容
     */
    public static String renderTemplate(String templateContent, String receiverName, Long receiverId) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", receiverName);
        variables.put("id", receiverId);
        return renderTemplate(templateContent, variables);
    }
}