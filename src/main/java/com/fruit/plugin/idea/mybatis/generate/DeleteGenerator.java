package com.fruit.plugin.idea.mybatis.generate;

import com.intellij.psi.PsiMethod;
import com.fruit.plugin.idea.mybatis.dom.model.GroupTwo;
import com.fruit.plugin.idea.mybatis.dom.model.Mapper;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * 删除生成器
 * </p>
 *
 * @author yanglin jobob
 * @since 2018 -07-30
 */
public class DeleteGenerator extends AbstractStatementGenerator {

    /**
     * Instantiates a new Delete generator.
     *
     * @param patterns the patterns
     */
    public DeleteGenerator(@NotNull String... patterns) {
        super(patterns);
    }

    @NotNull
    @Override
    protected GroupTwo getTarget(@NotNull Mapper mapper, @NotNull PsiMethod method) {
        return mapper.addDelete();
    }

    @NotNull
    @Override
    public String getId() {
        return "DeleteGenerator";
    }

    @NotNull
    @Override
    public String getDisplayText() {
        return "Delete Statement";
    }

}
