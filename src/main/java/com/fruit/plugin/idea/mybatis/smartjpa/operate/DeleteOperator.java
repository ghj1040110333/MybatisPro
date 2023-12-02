package com.fruit.plugin.idea.mybatis.smartjpa.operate;


import com.fruit.plugin.idea.mybatis.generate.AbstractStatementGenerator;
import com.fruit.plugin.idea.mybatis.smartjpa.common.SyntaxAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.AreaSequence;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.CompositeAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.CustomAreaAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.CustomFieldAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.CustomJoinAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.factory.ConditionAppenderFactory;
import com.fruit.plugin.idea.mybatis.smartjpa.common.factory.ResultAppenderFactory;
import com.fruit.plugin.idea.mybatis.smartjpa.common.iftest.ConditionFieldWrapper;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxField;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxReturnDescriptor;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.generate.Generator;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.manager.StatementBlock;
import com.fruit.plugin.idea.mybatis.smartjpa.util.SyntaxAppenderWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;

import java.util.LinkedList;
import java.util.List;

/**
 * The type Delete operator.
 */
public class DeleteOperator extends BaseOperatorManager {


    /**
     * Instantiates a new Delete operator.
     *
     * @param mappingField the mapping field
     */
    public DeleteOperator(final List<TxField> mappingField) {
        super.setOperatorNameList(AbstractStatementGenerator.DELETE_GENERATOR.getPatterns());
        this.init(mappingField);
    }


    /**
     * Init.
     *
     * @param mappingField the mapping field
     */
    public void init(final List<TxField> mappingField) {
        for (String areaName : getOperatorNameList()) {
            // 没有结果集字段
            final ResultAppenderFactory resultAppenderFactory = new DeleteResultAppenderFactory(areaName);
            ConditionAppenderFactory conditionAppenderFactory = new ConditionAppenderFactory(areaName, mappingField);
            for (TxField field : mappingField) {
                // 区域条件 : delete + By + field
                CompositeAppender areaByAppender = new CompositeAppender(
                    CustomAreaAppender.createCustomAreaAppender(areaName, ResultAppenderFactory.RESULT, AreaSequence.AREA, AreaSequence.RESULT, resultAppenderFactory),
                    CustomAreaAppender.createCustomAreaAppender("By", "By", AreaSequence.AREA, AreaSequence.CONDITION, conditionAppenderFactory),
                    new CustomFieldAppender(field, AreaSequence.CONDITION)
                );
                resultAppenderFactory.registerAppender(areaByAppender);

                // 区域条件 : delete  By : and + field
                CompositeAppender andAppender = new CompositeAppender(
                    new CustomJoinAppender("And", "AND", AreaSequence.CONDITION),
                    new CustomFieldAppender(field, AreaSequence.CONDITION)
                );
                resultAppenderFactory.registerAppender(andAppender);

                // 区域条件 : delete  By : or + field
                CompositeAppender orAppender = new CompositeAppender(
                    new CustomJoinAppender("Or", "OR", AreaSequence.CONDITION),
                    new CustomFieldAppender(field, AreaSequence.CONDITION)
                );
                resultAppenderFactory.registerAppender(orAppender);
            }

            StatementBlock statementBlock = new StatementBlock();
            statementBlock.setTagName(areaName);
            statementBlock.setResultAppenderFactory(resultAppenderFactory);
            statementBlock.setConditionAppenderFactory(conditionAppenderFactory);
            statementBlock.setReturnWrapper(TxReturnDescriptor.createByOrigin(null,"int"));
            this.registerStatementBlock(statementBlock);
        }

    }

    private class DeleteResultAppenderFactory extends ResultAppenderFactory {

        /**
         * Instantiates a new Delete result appender factory.
         *
         * @param pattern the pattern
         */
        public DeleteResultAppenderFactory(String pattern) {
            super(pattern);
        }

        @Override
        public String getTemplateText(String tableName, PsiClass entityClass,
                                      LinkedList<PsiParameter> parameters,
                                      LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper conditionFieldWrapper) {


            return "delete from " + tableName;
        }
    }


    @Override
    public String getTagName() {
        return "delete";
    }

    @Override
    public void generateMapperXml(String id,
                                  LinkedList<SyntaxAppender> jpaList,
                                  PsiClass entityClass,
                                  PsiMethod psiMethod,
                                  String tableName,
                                  Generator mybatisXmlGenerator,
                                  ConditionFieldWrapper conditionFieldWrapper) {
        String mapperXml = super.generateXml(jpaList, entityClass, psiMethod, tableName, conditionFieldWrapper);
        mybatisXmlGenerator.generateDelete(id, mapperXml);
    }
}
