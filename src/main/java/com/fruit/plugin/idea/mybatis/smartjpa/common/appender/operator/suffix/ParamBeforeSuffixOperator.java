package com.fruit.plugin.idea.mybatis.smartjpa.common.appender.operator.suffix;


import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.JdbcTypeUtils;
import com.intellij.psi.PsiParameter;

import java.util.LinkedList;

/**
 * 字段比较
 */
public class ParamBeforeSuffixOperator implements SuffixOperator{
    /**
     * 比较符号
     */
    private String operatorName;

    /**
     * Instantiates a new Param before suffix operator.
     *
     * @param operatorName the operator name
     */
    public ParamBeforeSuffixOperator(final String operatorName) {
        this.operatorName = operatorName;
    }

    public String getTemplateText(String fieldName, LinkedList<PsiParameter> parameters) {

        PsiParameter parameter = parameters.poll();
        return fieldName
                + " "
                + operatorName
                + " "
                + JdbcTypeUtils.wrapperField(parameter.getName(), parameter.getType().getCanonicalText());
    }
}
