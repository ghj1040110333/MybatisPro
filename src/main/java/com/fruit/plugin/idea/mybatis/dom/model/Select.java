package com.fruit.plugin.idea.mybatis.dom.model;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import com.fruit.plugin.idea.mybatis.dom.converter.AliasConverter;

import org.jetbrains.annotations.NotNull;

/**
 * The interface Select.
 *
 * @author yanglin
 */
public interface Select extends GroupTwo, ResultMapGroup {

    /**
     * Gets result type.
     *
     * @return the result type
     */
    @NotNull
    @Attribute("resultType")
    @Convert(AliasConverter.class)
    GenericAttributeValue<PsiClass> getResultType();
}
