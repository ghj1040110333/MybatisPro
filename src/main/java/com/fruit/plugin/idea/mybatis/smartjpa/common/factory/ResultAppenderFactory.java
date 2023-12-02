package com.fruit.plugin.idea.mybatis.smartjpa.common.factory;






import com.fruit.plugin.idea.mybatis.smartjpa.common.BaseAppenderFactory;
import com.fruit.plugin.idea.mybatis.smartjpa.common.SyntaxAppender;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.AreaSequence;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxParameter;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.model.AppendTypeEnum;
import com.intellij.psi.PsiClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 结果集追加
 */
public class ResultAppenderFactory extends BaseAppenderFactory {
    /**
     * The constant RESULT.
     */
// 区域类型
    public static final String RESULT = "Result";
    /**
     * 区域前缀
     */
    private final String areaPrefix;

    private final List<SyntaxAppender> syntaxAppenderList = new ArrayList<>();

    /**
     * Instantiates a new Result appender factory.
     *
     * @param areaPrefix the area prefix
     */
    public ResultAppenderFactory(final String areaPrefix) {
        this.areaPrefix = areaPrefix;
    }

    /**
     * Register appender.
     *
     * @param syntaxAppender the syntax appender
     */
    public void registerAppender(final SyntaxAppender syntaxAppender) {
        this.syntaxAppenderList.add(syntaxAppender);
    }


    @Override
    public List<TxParameter> getMxParameter(PsiClass entityClass, LinkedList<SyntaxAppender> jpaStringList) {
        SyntaxAppender peek = jpaStringList.poll();
        if (peek == null) {
            return Collections.emptyList();
        }

        LinkedList<TxParameter> txParameters = new LinkedList<>();

        while ((peek = jpaStringList.peek()) != null && peek.getType() != AppendTypeEnum.AREA) {
            txParameters.addAll(peek.getMxParameter(jpaStringList, entityClass));
        }
        return txParameters;
    }

    @Override
    public List<SyntaxAppender> getSyntaxAppenderList() {
        return this.syntaxAppenderList;
    }


    @Override
    public String getTipText() {
        return this.areaPrefix;
    }


    @Override
    protected AreaSequence getAreaSequence() {
        return AreaSequence.RESULT;
    }
}
