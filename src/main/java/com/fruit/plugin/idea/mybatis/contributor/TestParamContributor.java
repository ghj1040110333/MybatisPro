package com.fruit.plugin.idea.mybatis.contributor;

import com.fruit.plugin.idea.mybatis.annotation.Annotation;
import com.fruit.plugin.idea.mybatis.dom.model.IdDomElement;
import com.fruit.plugin.idea.mybatis.util.Icons;
import com.fruit.plugin.idea.mybatis.util.JavaUtils;
import com.fruit.plugin.idea.mybatis.util.MapperUtils;
import com.fruit.plugin.idea.mybatis.util.MybatisConstants;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * The type Test param contributor.
 *
 * @author yanglin
 */
public class TestParamContributor extends CompletionContributor {

    private static final Logger logger = LoggerFactory.getLogger(TestParamContributor.class);

    /**
     * Instantiates a new Test param contributor.
     */
    public TestParamContributor() {
        extend(CompletionType.BASIC,
                XmlPatterns.psiElement().inside(XmlPatterns.xmlAttributeValue().inside(XmlPatterns.xmlAttribute().withName("test"))),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
                        PsiElement position = parameters.getPosition();
                        addElementForPsiParameter(position.getProject(), result, MapperUtils.findParentIdDomElement(position).orElse(null));
                    }
                });
    }

    /**
     * Add element for psi parameter.
     *
     * @param project the project
     * @param result  the result
     * @param element the element
     */
    public static void addElementForPsiParameter(@NotNull Project project, @NotNull CompletionResultSet result, @Nullable IdDomElement element) {
        if (null == element) {
            return;
        }
        PsiMethod psiMethod= JavaUtils.findMethod(project, element).orElse(null);
        if(null == psiMethod ) {
            logger.info("psiMethod null");
            return;

        }
        logger.info("CompletionContributor xml start");
        PsiParameter[] psiParameters = psiMethod.getParameterList().getParameters();

        for (PsiParameter parameter : psiParameters ) {
            Optional<String> valueText = JavaUtils.getAnnotationValueText(parameter, Annotation.PARAM);
            if (valueText.isPresent()) {
                LookupElementBuilder builder = LookupElementBuilder.create(valueText.get()).withIcon(Icons.PARAM_COMPLETION_ICON);
                result.addElement(PrioritizedLookupElement.withPriority(builder, MybatisConstants.PRIORITY));
            }
        }
        logger.info("CompletionContributor xml end");

    }
}
