package com.fruit.plugin.idea.mybatis.smartjpa.component.mapping;


import com.fruit.plugin.idea.mybatis.smartjpa.component.TxField;
import com.intellij.psi.PsiClass;

import java.util.List;
import java.util.Optional;

/**
 * 自定义的BaseMapper上面, 定义了一个实体. 实体上有jpa的@Table注解
 * <p>
 * 例如: public interface BlogCustomBaseMapper extends CustomBaseMapper<JpaBlog> {}
 * BlogCustomBaseMapper 有 mapper 文件, 但是什么也没配置
 */
public class JpaAnnotationMappingResolver extends JpaMappingResolver implements EntityMappingResolver {


    @Override
    public List<TxField> getFields() {
        return fieldList;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public Optional<PsiClass> findEntity(PsiClass mapperClass) {
        Optional<PsiClass> entityClassOption = findEntityClassByMapperClass(mapperClass);
        if (entityClassOption.isPresent()) {
            PsiClass entityClass = entityClassOption.get();
            fieldList = initDataByCamel(entityClass);
            tableName = getTableNameByJpaOrCamel(entityClass);
            return Optional.of(entityClass);
        }
        return Optional.empty();
    }

    /**
     * 字段列表
     */
    private List<TxField> fieldList;
    /**
     * 表名
     */
    private String tableName;




}
