package com.fruit.plugin.idea.mybatis.smartjpa.common.appender.changer;


import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.MxParameterChanger;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxParameter;
import com.intellij.psi.PsiParameter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The type Boolean parameter changer.
 */
public class BooleanParameterChanger implements MxParameterChanger {

    private Boolean booleanValue;

    /**
     * Instantiates a new Boolean parameter changer.
     *
     * @param booleanValue the boolean value
     */
    public BooleanParameterChanger(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    @Override
    public List<TxParameter> getParameter(TxParameter txParameter) {
        return Collections.emptyList();
    }


    @Override
    public String getTemplateText(String fieldName, LinkedList<PsiParameter> parameters) {
        return fieldName + " = " +booleanValue.toString();
    }



}
