package com.fruit.plugin.idea.mybatis.smartjpa.common;


import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.CompositeAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.iftest.ConditionFieldWrapper;
import com.fruit.plugin.idea.mybatis.smartjpa.util.SyntaxAppenderWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiParameter;

import java.util.LinkedList;

/**
 * The type Template resolver.
 */
public class TemplateResolver {

    /**
     * Gets template text.
     *
     * @param current               the current
     * @param tableName             the table name
     * @param entityClass           the entity class
     * @param parameters            the parameters
     * @param collector             the collector
     * @param conditionFieldWrapper the condition field wrapper
     * @return the template text
     */
    public String getTemplateText(LinkedList<SyntaxAppender> current,
                                  String tableName, PsiClass entityClass,
                                  LinkedList<PsiParameter> parameters,
                                  LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper conditionFieldWrapper) {
        SyntaxAppender syntaxAppender = null;
        if (current.size() == 1) {
            syntaxAppender = current.poll();
        } else if (current.size() > 1) {
            syntaxAppender = new CompositeAppender(current.toArray(new SyntaxAppender[0]));
        } else {
            return "";
        }
        return syntaxAppender.getTemplateText(tableName, entityClass, parameters, collector, conditionFieldWrapper);

    }



}
