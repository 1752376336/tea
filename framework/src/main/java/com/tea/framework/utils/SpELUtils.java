package com.tea.framework.utils;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

public class SpELUtils {

    /**
     * 格式化内容 字符串拼接的形式返回
     *
     * @param template
     * @param templateParams
     * @return
     */
    public static String format(String template, Map<String, Object> templateParams) {
        StrSubstitutor sub = new StrSubstitutor(templateParams);
        return sub.replace(template);
    }

    /**
     * 格式化内容 参与运算
     * @param template
     * @param templateParams
     * @return
     */
    public static String parse(String template, Map<String, Object> templateParams) {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        if (templateParams == null) {
            return template;
        }
        for (Map.Entry<String, Object> entry : templateParams.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        Expression parseResult = parser.parseExpression(template);
        String content = parseResult.getValue(context, String.class);
        return content;
    }

//    public static void main(String[] args) {
//        System.out.println(parse("#value * 1024", new HashMap() {{put("value", 2191376d);}}));
//        System.out.println(format("${value} * 1024", new HashMap() {{put("value", 100);}}));
//    }
}