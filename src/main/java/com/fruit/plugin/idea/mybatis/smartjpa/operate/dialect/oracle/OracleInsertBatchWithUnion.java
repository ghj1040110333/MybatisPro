package com.fruit.plugin.idea.mybatis.smartjpa.operate.dialect.oracle;

import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.JdbcTypeUtils;
import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.operator.suffix.SuffixOperator;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxField;
import com.fruit.plugin.idea.mybatis.smartjpa.db.adaptor.DasTableAdaptor;
import com.fruit.plugin.idea.mybatis.smartjpa.exp.GenerateException;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.dialect.mysql.MysqlInsertBatch;
import com.intellij.database.model.DasTableKey;
import com.intellij.psi.PsiParameter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * oracle的批量插入
 */
public class OracleInsertBatchWithUnion extends MysqlInsertBatch {


    private DasTableAdaptor dasTable;
    private String tableName;

    /**
     * Instantiates a new Oracle insert batch with union.
     *
     * @param dasTable  the das table
     * @param tableName the table name
     */
    public OracleInsertBatchWithUnion(DasTableAdaptor dasTable, String tableName) {
        this.dasTable = dasTable;
        this.tableName = tableName;

    }

    @Override
    @NotNull
    protected SuffixOperator getSuffixOperator(List<TxField> mappingField) {
        return new InsertBatchSuffixOperator(mappingField);
    }

    @Override
    protected @NotNull String batchName() {
        return "BatchWithUnion";
    }

    /**
     * 批量插入
     */
    private class InsertBatchSuffixOperator implements SuffixOperator {
        @NotNull
        private List<TxField> mappingField;

        /**
         * Instantiates a new Insert batch suffix operator.
         *
         * @param mappingField the mapping field
         */
        public InsertBatchSuffixOperator(List<TxField> mappingField) {
            this.mappingField = mappingField;
        }

        @Override
        public String getTemplateText(String fieldName, LinkedList<PsiParameter> parameters) {
            Optional<String> sequenceName = dasTable.findSequenceName(tableName);

            StringBuilder stringBuilder = new StringBuilder();
            String itemName = "item";
            // 追加列名
            final String columns = mappingField.stream()
                .map(TxField::getColumnName)
                .collect(Collectors.joining(",\n"));
            stringBuilder.append("(").append(columns).append(")").append("\n");
            // values 连接符
            stringBuilder.append("(").append("\n");
            final PsiParameter collection = parameters.poll();
            if (collection == null) {
                throw new GenerateException("oracle insertBatch 生成失败, 无法获取集合名称");
            }
            final String collectionName = collection.getName();
            final String fields = mappingField.stream()
                .map(field -> {
                    String fieldStr = JdbcTypeUtils.wrapperField(itemName + "." + field.getFieldName(), field.getFieldType());
                    // 第一版写死字段变更, 后续重构
                    // 变更主键生成规则为自定义函数
                    if (sequenceName.isPresent() && dasTable != null) {
                        DasTableKey primaryKey = dasTable.getPrimaryKey();
                        // 当前字段是主键, 使用自定义函数替换主键
                        if (primaryKey != null && primaryKey.getColumnsRef().size() == 1) {
                            String pkFieldName = primaryKey.getColumnsRef().iterate().next();
                            if (pkFieldName.equals(field.getColumnName())) {
                                fieldStr = "GET_SEQ_NO('" + sequenceName.get() + "')";
                            }
                        }
                    }
                    //  变更指定字段为当前日期
                    if (field.getColumnName().equalsIgnoreCase("CREATE_TIME")
                        || field.getColumnName().equalsIgnoreCase("UPDATE_TIME")) {
                        fieldStr = "SYSDATE";
                    }
                    return fieldStr;
                })
                .collect(Collectors.joining(",\n"));

            stringBuilder.append("<foreach collection=\"").append(collectionName).append("\"");
            stringBuilder.append(" item=\"").append(itemName).append("\"");
            stringBuilder.append(" separator=\"union all\">").append("\n");
            stringBuilder.append("select").append("\n");
            stringBuilder.append(fields).append("\n").append("from dual").append("\n");
            stringBuilder.append("</foreach>").append("\n");
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

    }
}
