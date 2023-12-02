package com.fruit.plugin.idea.mybatis.smartjpa.operate.generate;

import com.fruit.plugin.idea.mybatis.smartjpa.common.SyntaxAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.AreaSequence;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.CustomFieldAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.iftest.ConditionFieldWrapper;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxField;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxParameter;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxParameterDescriptor;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TypeDescriptor;
import com.fruit.plugin.idea.mybatis.smartjpa.db.adaptor.DasTableAdaptor;
import com.fruit.plugin.idea.mybatis.smartjpa.db.adaptor.DbmsAdaptor;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.manager.AreaOperateManager;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.manager.AreaOperateManagerFactory;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.model.AppendTypeEnum;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 常用的生成器
 */
public class CommonGenerator implements PlatformGenerator {
    private @NotNull LinkedList<SyntaxAppender> jpaList;
    private List<TxField> mappingField;
    private String tableName;
    private PsiClass entityClass;
    /**
     * The Appender manager.
     */
    final AreaOperateManager appenderManager;
    private String text;


    private CommonGenerator(PsiClass entityClass,
                            String text,
                           DbmsAdaptor dbms,
                            DasTableAdaptor dasTable,
                            String tableName,
                            List<TxField> fields) {
        this.entityClass = entityClass;
        this.text = text;
        mappingField = fields;

        this.tableName = tableName;

        appenderManager = AreaOperateManagerFactory.getByDbms(dbms, mappingField, entityClass, dasTable, this.tableName);
        jpaList = appenderManager.splitAppenderByText(text);
    }

    /**
     * Create editor auto completion common generator.
     *
     * @param entityClass the entity class
     * @param text        the text
     * @param dbms        the dbms
     * @param dasTable    the das table
     * @param tableName   the table name
     * @param fields      the fields
     * @return common generator
     */
    public static CommonGenerator createEditorAutoCompletion(PsiClass entityClass, String text,
                                                             @NotNull DbmsAdaptor dbms,
                                                             DasTableAdaptor dasTable,
                                                             String tableName,
                                                             List<TxField> fields) {
        return new CommonGenerator(entityClass, text, dbms, dasTable, tableName, fields);
    }


    @Override
    public TypeDescriptor getParameter() {
        List<TxParameter> parameters = appenderManager.getParameters(entityClass, new LinkedList<>(jpaList));
        return new TxParameterDescriptor(parameters);
    }


    @Override
    public TypeDescriptor getReturn() {
        LinkedList<SyntaxAppender> linkedList = new LinkedList<>(jpaList);
        return appenderManager.getReturnWrapper(text, entityClass, linkedList);
    }

    @Override
    public void generateMapperXml(PsiMethod psiMethod, Generator mybatisXmlGenerator, ConditionFieldWrapper conditionFieldWrapper) {
        appenderManager.generateMapperXml(
            text,
            new LinkedList<>(jpaList),
            entityClass,
            psiMethod,
            tableName,
            mybatisXmlGenerator,
            conditionFieldWrapper);

    }

    @Override
    public List<String> getConditionFields() {
        return jpaList.stream()
            .filter(syntaxAppender->syntaxAppender.getAreaSequence() == AreaSequence.CONDITION
                && syntaxAppender.getType() == AppendTypeEnum.FIELD &&
                syntaxAppender instanceof CustomFieldAppender)
            .map(x-> ((CustomFieldAppender)x).getFieldName())
            .collect(Collectors.toList());
    }

    @Override
    public List<TxField> getAllFields(){
        return mappingField;
    }

    @Override
    public String getEntityClass(){
        return entityClass.getQualifiedName();
    }
}
