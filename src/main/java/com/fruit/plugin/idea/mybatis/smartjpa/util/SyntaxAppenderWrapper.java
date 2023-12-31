package com.fruit.plugin.idea.mybatis.smartjpa.util;

import com.fruit.plugin.idea.mybatis.smartjpa.common.SyntaxAppender;

import java.util.LinkedList;

/**
 * The type Syntax appender wrapper.
 */
public class SyntaxAppenderWrapper {
    /**
     * 缓存不可变更的
     */
    private SyntaxAppender syntaxAppender;
    /**
     * 为了扩展符号追加器的内容的集合
     */
    private LinkedList<SyntaxAppenderWrapper> collector;

    /**
     * Instantiates a new Syntax appender wrapper.
     *
     * @param syntaxAppender the syntax appender
     */
    public SyntaxAppenderWrapper(SyntaxAppender syntaxAppender) {
        this.syntaxAppender = syntaxAppender;
        this.collector = new LinkedList<>();
    }


    /**
     * Instantiates a new Syntax appender wrapper.
     *
     * @param syntaxAppender the syntax appender
     * @param collector      the collector
     */
    public SyntaxAppenderWrapper(SyntaxAppender syntaxAppender, LinkedList<SyntaxAppenderWrapper> collector) {
        this.syntaxAppender = syntaxAppender;
        this.collector = collector;
    }

    /**
     * Add wrapper.
     *
     * @param syntaxAppenderWrapper the syntax appender wrapper
     */
    public void addWrapper(SyntaxAppenderWrapper syntaxAppenderWrapper) {
        this.collector.add(syntaxAppenderWrapper);
    }

    /**
     * Gets appender.
     *
     * @return the appender
     */
    public SyntaxAppender getAppender() {
        return syntaxAppender;
    }

    /**
     * Gets collector.
     *
     * @return the collector
     */
    public LinkedList<SyntaxAppenderWrapper> getCollector() {
        return collector;
    }
}
