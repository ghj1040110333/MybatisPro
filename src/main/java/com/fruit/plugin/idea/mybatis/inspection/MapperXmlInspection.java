package com.fruit.plugin.idea.mybatis.inspection;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.highlighting.BasicDomElementsInspection;
import com.intellij.util.xml.highlighting.DomElementAnnotationHolder;
import com.intellij.util.xml.highlighting.DomHighlightingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Mapper XML 检查
 * </p>
 *
 * @author yanglin
 * @since 2018 -07-30
 */
public class MapperXmlInspection extends BasicDomElementsInspection<DomElement> {

    /**
     * Instantiates a new Mapper xml inspection.
     */
    public MapperXmlInspection() {
        super(DomElement.class);
    }

    /**
     * The Logger.
     */
    Logger logger = LoggerFactory.getLogger(MapperXmlInspection.class);

    @Override
    protected void checkDomElement(DomElement element, DomElementAnnotationHolder holder, DomHighlightingHelper helper) {
        logger.info("check xml start, element: {}",element);
        super.checkDomElement(element, holder, helper);
        logger.info("check xml end, element: {}",element);
    }

}
