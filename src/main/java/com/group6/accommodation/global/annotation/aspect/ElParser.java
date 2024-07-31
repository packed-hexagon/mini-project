package com.group6.accommodation.global.annotation.aspect;

import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class ElParser {
    public static Object getDynamicValue(String[] names, Object[] args, String key) {
        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < names.length; i++) {
            context.setVariable(names[i], args[i]);
        }

        return parser.parseExpression(key).getValue(context, Object.class);
    }
}
