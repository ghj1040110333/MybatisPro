package com.fruit.plugin.idea.mybatis.dom.model;

import com.intellij.psi.PsiClass;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import com.fruit.plugin.idea.mybatis.dom.converter.AliasConverter;
import com.fruit.plugin.idea.mybatis.dom.converter.ResultMapConverter;

import org.jetbrains.annotations.NotNull;

/**
 * The interface Result map.
 *
 * @author yanglin
 */
public interface ResultMap extends GroupFour, IdDomElement {

    /**
     * Gets extends.
     *
     * @return the extends
     */
    @NotNull
    @Attribute("extends")
    @Convert(ResultMapConverter.class)
    GenericAttributeValue<XmlAttributeValue> getExtends();

    /**
     * Gets type.
     *
     * @return the type
     */
    @NotNull
    @Attribute("type")
    @Convert(AliasConverter.class)
    GenericAttributeValue<PsiClass> getType();

}
