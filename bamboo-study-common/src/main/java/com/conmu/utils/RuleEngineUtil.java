package com.conmu.utils;



import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.util.Map;

/**
 * el表达式规则引擎
 * @author mucongcong
 * @date 2023/07/05 15:32
 * @since
 **/
public class RuleEngineUtil {

    public static boolean expressionResult(Map<String, Object> map, String expression) {
        Expression exp = AviatorEvaluator.compile(expression);
        final Object execute = exp.execute(map);
        return Boolean.parseBoolean(String.valueOf(execute));
    }
}
