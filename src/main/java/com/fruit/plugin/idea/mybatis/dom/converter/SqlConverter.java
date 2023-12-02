package com.fruit.plugin.idea.mybatis.dom.converter;

import com.fruit.plugin.idea.mybatis.dom.model.IdDomElement;
import com.fruit.plugin.idea.mybatis.dom.model.Mapper;
import com.intellij.util.xml.ConvertContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * The type Sql converter.
 *
 * @author yanglin
 */
public class SqlConverter extends IdBasedTagConverter {

    @NotNull
    @Override
    public Collection<? extends IdDomElement> getComparisons(@Nullable Mapper mapper, ConvertContext context) {
        return mapper.getSqls();
    }

}
