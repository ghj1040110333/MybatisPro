package com.fruit.plugin.idea.mybatis.smartjpa.common.command;



import com.fruit.plugin.idea.mybatis.smartjpa.common.SyntaxAppender;

import java.util.Optional;

/**
 * The type Field append type command.
 */
public class FieldAppendTypeCommand implements AppendTypeCommand {
    private final SyntaxAppender syntaxAppender;

    /**
     * Instantiates a new Field append type command.
     *
     * @param syntaxAppender the syntax appender
     */
    public FieldAppendTypeCommand(final SyntaxAppender syntaxAppender) {

        this.syntaxAppender = syntaxAppender;
    }

    @Override
    public Optional<SyntaxAppender> execute() {
        return Optional.of(this.syntaxAppender);
    }
}
