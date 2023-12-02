package com.fruit.plugin.idea.mybatis.alias;

import com.fruit.plugin.idea.mybatis.dom.model.TypeAlias;
import com.fruit.plugin.idea.mybatis.util.MapperUtils;
import com.google.common.collect.Sets;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.Processor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * The type Single alias resolver.
 *
 * @author yanglin
 */
public class SingleAliasResolver extends AliasResolver {

    /**
     * Instantiates a new Single alias resolver.
     *
     * @param project the project
     */
    public SingleAliasResolver(Project project) {
        super(project);
    }

    @NotNull
    @Override
    public Set<AliasDesc> getClassAliasDescriptions(@Nullable PsiElement element) {
        final Set<AliasDesc> result = Sets.newHashSet();
        MapperUtils.processConfiguredTypeAliases(project, new Processor<TypeAlias>() {
            @Override
            public boolean process(TypeAlias typeAlias) {
                addAliasDesc(result, typeAlias.getType().getValue(), typeAlias.getAlias().getStringValue());
                return true;
            }
        });
        return result;
    }

}
