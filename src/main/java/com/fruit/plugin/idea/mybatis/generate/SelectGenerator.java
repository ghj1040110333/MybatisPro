package com.fruit.plugin.idea.mybatis.generate;

import com.fruit.plugin.idea.mybatis.dom.model.GroupTwo;
import com.fruit.plugin.idea.mybatis.dom.model.Mapper;
import com.fruit.plugin.idea.mybatis.dom.model.Select;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * <p>
 * Select 代码生成器
 * </p>
 *
 * @author yanglin jobob
 * @since 2018 -07-30
 */
public class SelectGenerator extends AbstractStatementGenerator {

    /**
     * Instantiates a new Select generator.
     *
     * @param patterns the patterns
     */
    public SelectGenerator(@NotNull String... patterns) {
        super(patterns);
    }

    @NotNull
    @Override
    protected GroupTwo getTarget(@NotNull Mapper mapper, @NotNull PsiMethod method) {
        Select select = mapper.addSelect();
        setupResultType(method, select);
        return select;
    }

    private void setupResultType(PsiMethod method, Select select) {
        Optional<PsiClass> clazz = AbstractStatementGenerator.getSelectResultType(method);
        clazz.ifPresent(psiClass -> select.getResultType().setValue(psiClass));
    }

    @NotNull
    @Override
    public String getId() {
        return "SelectGenerator";
    }

    @NotNull
    @Override
    public String getDisplayText() {
        return "Select Statement";
    }
}
