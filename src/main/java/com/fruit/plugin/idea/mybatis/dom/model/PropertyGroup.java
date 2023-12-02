package com.fruit.plugin.idea.mybatis.dom.model;

import com.fruit.plugin.idea.mybatis.dom.converter.PropertyConverter;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * The interface Property group.
 *
 * @author yanglin
 */
public interface PropertyGroup extends DomElement {

    /**
     * Gets property.
     *
     * @return the property
     */
    @Attribute("property")
    @Convert(PropertyConverter.class)
    GenericAttributeValue<XmlAttributeValue> getProperty();

//    @Attribute("column")
//    GenericAttributeValue<XmlAttributeValue> getColumn();
//
//    @Attribute("jdbcType")
//    GenericAttributeValue<XmlAttributeValue> getJdbcType();
}
