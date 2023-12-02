package com.fruit.plugin.idea.mybatis.smartjpa.operate;


import com.fruit.plugin.idea.mybatis.smartjpa.common.SyntaxAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.SyntaxAppenderFactory;
import com.fruit.plugin.idea.mybatis.smartjpa.common.iftest.ConditionFieldWrapper;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxField;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxParameter;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TypeDescriptor;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.manager.AreaOperateManager;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.manager.StatementBlock;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.manager.StatementBlockFactory;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Base operator manager.
 */
public abstract class BaseOperatorManager implements AreaOperateManager {

    private Set<String> operatorNameList = new HashSet<>();

    private final StatementBlockFactory syntaxAppenderFactoryManager = new StatementBlockFactory();

    /**
     * Gets operator name list.
     *
     * @return the operator name list
     */
    protected Set<String> getOperatorNameList() {
        return operatorNameList;
    }

    /**
     * Sets operator name list.
     *
     * @param nameSet the name set
     */
    protected void setOperatorNameList(final Set<String> nameSet) {
        operatorNameList = nameSet;
    }


    @Override
    public boolean support(String operatorText) {
        return operatorNameList.contains(operatorText);
    }

    @Override
    public TypeDescriptor getReturnWrapper(String text, PsiClass entityClass, LinkedList<SyntaxAppender> linkedList) {
        StatementBlock statementBlock = syntaxAppenderFactoryManager.findBlockByText(text);
        return statementBlock.getReturnDescriptor();
    }

    /**
     * Add operator name.
     *
     * @param operatorName the operator name
     */
    protected void addOperatorName(String operatorName){
        operatorNameList.add(operatorName);
    }
    @NotNull
    @Override
    public LinkedList<SyntaxAppender> splitAppenderByText(final String splitParam) {
        return this.syntaxAppenderFactoryManager.splitAppenderByText(splitParam);
    }

    /**
     * Can execute boolean.
     *
     * @param areaName the area name
     * @return the boolean
     */
    protected boolean canExecute(final String areaName) {
        return this.operatorNameList.contains(areaName);
    }


    @Override
    public List<String> getCompletionContent(final LinkedList<SyntaxAppender> jpaList) {
        SyntaxAppender firstAreaAppender = jpaList.peek();
        if (firstAreaAppender != null && !this.canExecute(firstAreaAppender.getText())) {
            return Collections.emptyList();
        }
        List<SyntaxAppenderFactory> areaListByJpa = syntaxAppenderFactoryManager.findAreaListByJpa(jpaList);
        return areaListByJpa.stream().flatMap(x -> x.getCompletionContent(jpaList).stream()).collect(Collectors.toList());
    }

    @Override
    public List<String> getCompletionContent() {
        Collection<StatementBlock> allBlock = syntaxAppenderFactoryManager.getAllBlock();
        return allBlock.stream().map(x -> x.getResultAppenderFactory())
            .filter(Objects::nonNull)
            .flatMap(x -> x.getCompletionContent(new ArrayList<>()).stream())
            .collect(Collectors.toList());
    }

    /**
     * 获取参数列表
     *
     * @param entityClass
     * @return
     */
    @Override
    public List<TxParameter> getParameters(PsiClass entityClass,
                                           LinkedList<SyntaxAppender> jpaList) {
        SyntaxAppender firstAreaAppender = jpaList.peek();
        if (firstAreaAppender != null && !this.canExecute(firstAreaAppender.getText())) {
            return Collections.emptyList();
        }

        List<SyntaxAppenderFactory> areaListByJpa = syntaxAppenderFactoryManager.findAreaListByJpa(jpaList);
        return areaListByJpa.stream().flatMap(x -> x.getMxParameter(entityClass, jpaList).stream()).collect(Collectors.toList());
    }



    private static final Logger logger = LoggerFactory.getLogger(BaseOperatorManager.class);


    /**
     * Register statement block.
     *
     * @param statementBlock the statement block
     */
    public void registerStatementBlock(StatementBlock statementBlock) {
        this.syntaxAppenderFactoryManager.registerStatementBlock(statementBlock);
    }

    /**
     * Gets tag name.
     *
     * @return the tag name
     */
    protected abstract String getTagName();

    /**
     * 生成xml
     *
     * @param jpaList               the jpa list
     * @param entityClass           the entity class
     * @param psiMethod             the psi method
     * @param tableName             the table name
     * @param conditionFieldWrapper the condition field wrapper
     * @return string
     */
    protected String generateXml(LinkedList<SyntaxAppender> jpaList,
                                 PsiClass entityClass,
                                 PsiMethod psiMethod,
                                 String tableName,
                                 ConditionFieldWrapper conditionFieldWrapper) {
        SyntaxAppender firstAreaAppender = jpaList.peek();
        if (firstAreaAppender != null && !this.canExecute(firstAreaAppender.getText())) {
            return null;
        }

        List<SyntaxAppenderFactory> areaListByJpa = syntaxAppenderFactoryManager.findAreaListByJpa(jpaList);

        LinkedList<PsiParameter> parameters = Arrays.stream(psiMethod.getParameterList().getParameters())
            .collect(Collectors.toCollection(LinkedList::new));

        return areaListByJpa.stream().map(syntaxAppenderFactory -> {
            // 区域生成的xml内容
            return syntaxAppenderFactory.getFactoryTemplateText(jpaList,
                entityClass,
                parameters,
                tableName,
                conditionFieldWrapper
            );
        }).filter(StringUtils::isNotBlank).collect(Collectors.joining("\n")) + "\n";

    }


    /**
     * Init custom area.
     *
     * @param areaName     the area name
     * @param mappingField the mapping field
     */
    protected void initCustomArea(String areaName, List<TxField> mappingField){

    }





}
