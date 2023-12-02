package com.fruit.plugin.idea.mybatis.smartjpa.common.command;



import com.fruit.plugin.idea.mybatis.smartjpa.common.SyntaxAppender;

import java.util.Optional;

/**
 * The interface Append type command.
 */
public interface AppendTypeCommand {
    /**
     * Execute optional.
     *
     * @return the optional
     */
    Optional<SyntaxAppender> execute();
}
