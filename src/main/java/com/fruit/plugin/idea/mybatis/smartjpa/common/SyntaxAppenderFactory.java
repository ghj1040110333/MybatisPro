package com.fruit.plugin.idea.mybatis.smartjpa.common;


import com.fruit.plugin.idea.mybatis.smartjpa.common.iftest.ConditionFieldWrapper;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxParameter;
import com.fruit.plugin.idea.mybatis.smartjpa.util.SyntaxAppenderWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiParameter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

/**
 * 符号追加器工厂
 */
public interface SyntaxAppenderFactory {
    /**
     * Gets syntax appender list.
     *
     * @return the syntax appender list
     */
    List<SyntaxAppender> getSyntaxAppenderList();

    /**
     * Gets completion content.
     *
     * @param splitList the split list
     * @return the completion content
     */
    default List<String> getCompletionContent(final List<SyntaxAppender> splitList) {
        final List<String> result = new ArrayList<>();
        for (final SyntaxAppender syntaxAppender : this.getSyntaxAppenderList()) {
            this.mappingAppend(syntaxAppender, splitList).ifPresent(result::add);
        }
        return result;
    }

    /**
     * Gets factory template text.
     *
     * @param jpaStringList         the jpa string list
     * @param entityClass           the entity class
     * @param parameters            the parameters
     * @param tableName             the table name
     * @param conditionFieldWrapper the condition field wrapper
     * @return the factory template text
     */
    String getFactoryTemplateText(LinkedList<SyntaxAppender> jpaStringList,
                                  PsiClass entityClass,
                                  LinkedList<PsiParameter> parameters,
                                  String tableName,
                                  ConditionFieldWrapper conditionFieldWrapper);

    /**
     * Mapping append optional.
     *
     * @param syntaxAppender the syntax appender
     * @param splitList      the split list
     * @return the optional
     */
    Optional<String> mappingAppend(SyntaxAppender syntaxAppender, List<SyntaxAppender> splitList);

    /**
     * 动态提示文本
     *
     * @return tip text
     */
    String getTipText();


    /**
     * Gets mx parameter.
     *
     * @param entityClass   the entity class
     * @param jpaStringList the jpa string list
     * @return the mx parameter
     */
    List<TxParameter> getMxParameter(PsiClass entityClass, LinkedList<SyntaxAppender> jpaStringList);

    /**
     * Gets template text.
     *
     * @param tableName             the table name
     * @param entityClass           the entity class
     * @param parameters            the parameters
     * @param collector             the collector
     * @param conditionFieldWrapper the condition field wrapper
     * @return the template text
     */
    default String getTemplateText(String tableName,
                                   PsiClass entityClass,
                                   LinkedList<PsiParameter> parameters,
                                   LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper conditionFieldWrapper) {
        return "";
    }

    /**
     * Find priority.
     *
     * @param priorityQueue      the priority queue
     * @param syntaxAppenderList the syntax appender list
     * @param splitStr           the split str
     */
    void findPriority(PriorityQueue<SyntaxAppender> priorityQueue, LinkedList<SyntaxAppender> syntaxAppenderList, String splitStr);
}
