package com.fruit.plugin.idea.mybatis.smartjpa.operate.dialect;

import com.fruit.plugin.idea.mybatis.smartjpa.operate.manager.StatementBlock;

/**
 * The interface Custom statement.
 */
public interface CustomStatement {
    /**
     * Gets statement block.
     *
     * @return the statement block
     */
    StatementBlock getStatementBlock();

    /**
     * Operator name string.
     *
     * @return the string
     */
    String operatorName();
}
