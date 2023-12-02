package com.fruit.plugin.idea.mybatis.provider;

import java.util.Collection;

import com.google.common.base.Function;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;

import com.fruit.plugin.idea.mybatis.dom.model.IdDomElement;
import com.fruit.plugin.idea.mybatis.service.JavaService;
import com.fruit.plugin.idea.mybatis.util.Icons;
import com.fruit.plugin.idea.mybatis.util.JavaUtils;
import com.google.common.collect.Collections2;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.CommonProcessors;

/**
 * The type Mapper line marker provider.
 *
 * @author yanglin
 */
public class MapperLineMarkerProvider extends RelatedItemLineMarkerProvider {

    /**
     * 暂时不要改动这里.
     */
    @SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef", "Guava"})
    private static final Function<DomElement, XmlTag> FUN = new Function<DomElement, XmlTag>() {
        @Override
        public XmlTag apply(DomElement domElement) {
            return domElement.getXmlTag();
        }
    };

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result) {
        if (element instanceof PsiNameIdentifierOwner
            && JavaUtils.isElementWithinInterface(element)
        && !(element instanceof PsiParameter)) {
            CommonProcessors.CollectProcessor<IdDomElement> processor = new CommonProcessors.CollectProcessor<>();
            JavaService.getInstance(element.getProject()).process(element, processor);
            Collection<IdDomElement> results = processor.getResults();
            if (!results.isEmpty()) {
                NavigationGutterIconBuilder<PsiElement> builder =
                    NavigationGutterIconBuilder.create(Icons.MAPPER_LINE_MARKER_ICON)
                        .setAlignment(GutterIconRenderer.Alignment.CENTER)
                        .setTargets(Collections2.transform(results, FUN))
                        .setTooltipTitle("Navigation to target in mapper xml");
                // TODO 智能跳转到合适的xml
                result.add(builder.createLineMarkerInfo(((PsiNameIdentifierOwner) element).getNameIdentifier()));
            }
        }
    }
}
