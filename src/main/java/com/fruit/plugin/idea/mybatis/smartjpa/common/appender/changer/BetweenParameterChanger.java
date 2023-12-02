package com.fruit.plugin.idea.mybatis.smartjpa.common.appender.changer;




import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.JdbcTypeUtils;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.MxParameterChanger;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxParameter;
import com.github.hypfvieh.util.StringUtil;
import com.intellij.psi.PsiParameter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The type Between parameter changer.
 */
public class BetweenParameterChanger implements MxParameterChanger {

    /**
     * The constant SPACE.
     */
    public static final String SPACE = " ";

    @Override
    public List<TxParameter> getParameter(TxParameter txParameter) {
        TxParameter beginParameter = TxParameter.createByOrigin(
                "begin" + StringUtil.upperCaseFirstChar(txParameter.getName()),
                txParameter.getTypeText(),
                txParameter.getCanonicalTypeText());

        TxParameter endParameter = TxParameter.createByOrigin(
                "end" + StringUtil.upperCaseFirstChar(txParameter.getName()),
                txParameter.getTypeText(),
                txParameter.getCanonicalTypeText());

        return Arrays.asList(beginParameter, endParameter);
    }

    @Override
    public String getTemplateText(String fieldName, LinkedList<PsiParameter> parameters) {
        final PsiParameter begin = parameters.poll();
        final PsiParameter end = parameters.poll();
        final String beginStr = JdbcTypeUtils.wrapperField(begin.getName(), begin.getType().getCanonicalText());
        final String endStr = JdbcTypeUtils.wrapperField(end.getName(), end.getType().getCanonicalText());
        return fieldName + SPACE + "between" + SPACE + beginStr  + " and " + endStr;
    }
}
