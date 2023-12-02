package com.fruit.plugin.idea.mybatis.smartjpa.common.factory;


import com.fruit.plugin.idea.mybatis.smartjpa.common.BaseAppenderFactory;
import com.fruit.plugin.idea.mybatis.smartjpa.common.SyntaxAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.AreaSequence;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.CompositeAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.CustomAreaAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.CustomFieldAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.CustomJoinAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.CustomSuffixAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.iftest.ConditionFieldWrapper;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxField;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxParameter;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.model.AppendTypeEnum;
import com.fruit.plugin.idea.mybatis.smartjpa.util.SyntaxAppenderWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiParameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 排序区
 */
public class SortAppenderFactory extends BaseAppenderFactory {
    private final List<TxField> mappingField;

    /**
     * Instantiates a new Sort appender factory.
     *
     * @param mappingField the mapping field
     */
    public SortAppenderFactory(final List<TxField> mappingField) {
        this.mappingField = mappingField;
    }

    @Override
    public List<SyntaxAppender> getSyntaxAppenderList() {
        final List<SyntaxAppender> syntaxAppenderArrayList = new ArrayList<>();
        // order by field : desc
        syntaxAppenderArrayList.add(CustomSuffixAppender.createByFixed("Desc", "desc", AreaSequence.SORT));
        for (final TxField field : this.mappingField) {
            // order by: field
            final SyntaxAppender appender = new CompositeAppender(
                    CustomAreaAppender.createCustomAreaAppender(this.getTipText(), getTipText(), AreaSequence.AREA, AreaSequence.SORT, this),
                    new SortCustomFieldAppender(field, AreaSequence.SORT));
            syntaxAppenderArrayList.add(appender);
            // order by: and field
            final CompositeAppender andAppender = new CompositeAppender(new CustomJoinAppender("And", ",", AreaSequence.SORT),
                    new SortCustomFieldAppender(field, AreaSequence.SORT));
            syntaxAppenderArrayList.add(andAppender);
        }
        return syntaxAppenderArrayList;
    }

    @Override
    public String getTipText() {
        return "OrderBy";
    }

    @Override
    public List<TxParameter> getMxParameter(PsiClass entityClass, LinkedList<SyntaxAppender> jpaStringList) {
        return Collections.emptyList();
    }


    private class SortCustomFieldAppender extends CustomFieldAppender{

        /**
         * Instantiates a new Sort custom field appender.
         *
         * @param field the field
         * @param sort  the sort
         */
        public SortCustomFieldAppender(TxField field, AreaSequence sort) {
            super(field, sort);
        }

        @Override
        public String getTemplateText(String tableName,
                                      PsiClass entityClass,
                                      LinkedList<PsiParameter> parameters,
                                      LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper conditionFieldWrapper) {
            return getFieldName();
        }
    }

    @Override
    public String getTemplateText(String tableName, PsiClass entityClass, LinkedList<PsiParameter> parameters, LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper conditionFieldWrapper) {
        StringBuilder stringBuilder = new StringBuilder();
        for (SyntaxAppenderWrapper syntaxAppender : collector) {
            String templateText = syntaxAppender.getAppender().getTemplateText(tableName, entityClass, parameters, syntaxAppender.getCollector(), conditionFieldWrapper);
            stringBuilder.append(templateText).append(" ");
        }
        return "order by " + stringBuilder.toString();
    }


    @Override
    public void appendDefault(SyntaxAppender syntaxAppender, LinkedList<SyntaxAppender> current) {
        if (syntaxAppender.getType() == AppendTypeEnum.FIELD) {
            current.addLast(CustomSuffixAppender.createByFixed("Asc", "asc", AreaSequence.SORT));
        }
    }


    @Override
    protected AreaSequence getAreaSequence() {
        return AreaSequence.SORT;
    }
}
