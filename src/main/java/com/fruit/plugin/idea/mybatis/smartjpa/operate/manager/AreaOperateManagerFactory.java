package com.fruit.plugin.idea.mybatis.smartjpa.operate.manager;

import com.fruit.plugin.idea.mybatis.smartjpa.component.TxField;
import com.fruit.plugin.idea.mybatis.smartjpa.db.adaptor.DasTableAdaptor;
import com.fruit.plugin.idea.mybatis.smartjpa.db.adaptor.DbmsAdaptor;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.dialect.mysql.MysqlManager;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.dialect.oracle.OracleManager;
import com.intellij.psi.PsiClass;

import java.util.List;

/**
 * The type Area operate manager factory.
 */
public class AreaOperateManagerFactory {

    /**
     * Gets by dbms.
     *
     * @param dbms         the dbms
     * @param mappingField the mapping field
     * @param entityClass  the entity class
     * @param dasTable     the das table
     * @param tableName    the table name
     * @return the by dbms
     */
    public static AreaOperateManager getByDbms(DbmsAdaptor dbms,
                                               List<TxField> mappingField,
                                               PsiClass entityClass,
                                               DasTableAdaptor dasTable,
                                               String tableName) {
        if (dbms == DbmsAdaptor.ORACLE) {
            return new OracleManager(mappingField, entityClass, dasTable, tableName);
        }
        return new MysqlManager(mappingField, entityClass);
    }
}
