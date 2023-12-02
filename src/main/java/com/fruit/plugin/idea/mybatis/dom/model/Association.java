package com.fruit.plugin.idea.mybatis.dom.model;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import com.fruit.plugin.idea.mybatis.dom.converter.AliasConverter;

import org.jetbrains.annotations.NotNull;

/**
 * The interface Association.
 *
 * @author yanglin
 */
public interface Association extends GroupFour, ResultMapGroup, PropertyGroup {

    /**
     * Gets java type.
     *
     * @return the java type
     */
    @NotNull
    @Attribute("javaType")
    @Convert(AliasConverter.class)
    GenericAttributeValue<PsiClass> getJavaType();
}
