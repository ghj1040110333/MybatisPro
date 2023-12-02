package com.fruit.plugin.idea.mybatis.dom.model;

import com.fruit.plugin.idea.mybatis.dom.converter.PropertyConverter;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * The interface Id.
 *
 * @author yanglin
 */
public interface Id extends PropertyGroup {

    @Attribute("property")
    @Convert(PropertyConverter.class)
    GenericAttributeValue<XmlAttributeValue> getProperty();

//    @Attribute("column")
//    GenericAttributeValue<XmlAttributeValue> getColumn();
//
//    @Attribute("jdbcType")
//    GenericAttributeValue<XmlAttributeValue> getJdbcType();

}
