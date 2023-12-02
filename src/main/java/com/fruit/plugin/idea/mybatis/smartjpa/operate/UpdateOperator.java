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
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxParameter;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxReturnDescriptor;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.generate.Generator;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.manager.StatementBlock;
import com.fruit.plugin.idea.mybatis.util.StringUtils;
import com.fruit.plugin.idea.mybatis.smartjpa.util.SyntaxAppenderWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Update operator.
 */
public class UpdateOperator extends BaseOperatorManager {


    /**
     * Instantiates a new Update operator.
     *
     * @param mappingField the mapping field
     */
    public UpdateOperator(final List<TxField> mappingField) {
        this.setOperatorNameList(AbstractStatementGenerator.UPDATE_GENERATOR.getPatterns());
        this.init(mappingField);
    }

    /**
     * Init.
     *
     * @param mappingField the mapping field
     */
    public void init(final List<TxField> mappingField) {
        for (final String areaName : this.getOperatorNameList()) {
            final ResultAppenderFactory updateFactory = new UpdateResultAppenderFactory(areaName);
            this.initResultAppender(updateFactory, mappingField, areaName);

            StatementBlock statementBlock = new StatementBlock();
            statementBlock.setTagName(areaName);
            statementBlock.setResultAppenderFactory(updateFactory);
            statementBlock.setConditionAppenderFactory(new ConditionAppenderFactory(areaName, mappingField));
            statementBlock.setReturnWrapper(TxReturnDescriptor.createByOrigin(null,"int"));
            this.registerStatementBlock(statementBlock);
        }

    }

    private class UpdateResultAppenderFactory extends ResultAppenderFactory {

        /**
         * Instantiates a new Update result appender factory.
         *
         * @param areaPrefix the area prefix
         */
        public UpdateResultAppenderFactory(String areaPrefix) {
            super(areaPrefix);
        }

        @Override
        public String getTemplateText(String tableName,
                                      PsiClass entityClass,
                                      LinkedList<PsiParameter> parameters,
                                      LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper conditionFieldWrapper) {
            String operatorXml = "update " + tableName + "\n set ";

            return operatorXml + collector.stream().map(syntaxAppenderWrapper -> {
                return syntaxAppenderWrapper.getAppender().getTemplateText(tableName, entityClass, parameters, collector, conditionFieldWrapper);
            }).collect(Collectors.joining());
        }
    }


    private void initResultAppender(final ResultAppenderFactory updateFactory, final List<TxField> mappingField, final String areaName) {
        for (final TxField field : mappingField) {
            // field
            // and + field
            final CompositeAppender andAppender = new CompositeAppender(
                new CustomJoinAppender("And", ",", AreaSequence.RESULT),
                new CustomFieldAppender(field, AreaSequence.RESULT));
            updateFactory.registerAppender(andAppender);

            // update + field
            final CompositeAppender areaAppender =
                new CompositeAppender(
                    CustomAreaAppender.createCustomAreaAppender(areaName, ResultAppenderFactory.RESULT, AreaSequence.AREA, AreaSequence.RESULT, updateFactory),
                    new CustomFieldAppender(field, AreaSequence.RESULT)
                );
            updateFactory.registerAppender(areaAppender);

        }
    }


    @Override
    public List<TxParameter> getParameters(PsiClass entityClass, LinkedList<SyntaxAppender> jpaStringList) {
        List<TxParameter> parameters = super.getParameters(entityClass, jpaStringList);
        Set<String> collection = new HashSet<>();
        for (TxParameter parameter : parameters) {
            String name = parameter.getName();
            if (!collection.add(name)) {
                String newName = "old" + StringUtils.upperCaseFirstChar(name);
                parameter.setName(newName);
            }
        }
        return parameters;
    }


    @Override
    public String getTagName() {
        return "update";
    }

    @Override
    public void generateMapperXml(String id, LinkedList<SyntaxAppender> jpaList, PsiClass entityClass, PsiMethod psiMethod, String tableName, Generator mybatisXmlGenerator, ConditionFieldWrapper conditionFieldWrapper) {
        String mapperXml = super.generateXml(jpaList, entityClass, psiMethod, tableName, conditionFieldWrapper);
        mybatisXmlGenerator.generateUpdate(id, mapperXml);
    }
}
