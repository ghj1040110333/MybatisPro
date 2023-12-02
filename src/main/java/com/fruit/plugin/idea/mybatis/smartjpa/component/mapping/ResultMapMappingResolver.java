package com.fruit.plugin.idea.mybatis.smartjpa.component.mapping;

import com.fruit.plugin.idea.mybatis.dom.model.Id;
import com.fruit.plugin.idea.mybatis.dom.model.Mapper;
import com.fruit.plugin.idea.mybatis.dom.model.Result;
import com.fruit.plugin.idea.mybatis.dom.model.ResultMap;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxField;
import com.fruit.plugin.idea.mybatis.util.StringUtils;
import com.fruit.plugin.idea.mybatis.util.MapperUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 根据xml文件的 BaseResultMap 获取字段对应的 列名
 */
public class ResultMapMappingResolver extends JpaMappingResolver implements EntityMappingResolver {

    @Override
    public List<TxField> getFields() {
        return fieldList;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    private String tableName;

    private List<TxField> fieldList;

    @Override
    public Optional<PsiClass> findEntity(PsiClass mapperClass) {
        Optional<Mapper> firstMapper = MapperUtils.findFirstMapper(mapperClass.getProject(), mapperClass);
        if (firstMapper.isPresent()) {
            Mapper mapper = firstMapper.get();
            Optional<ResultMap> resultMapOpt = findResultMap(mapper.getResultMaps());
            if (resultMapOpt.isPresent()) {
                ResultMap resultMap = resultMapOpt.get();


                GenericAttributeValue<PsiClass> type = resultMap.getType();
                // 实体类的名字
                PsiClass entityClass = type.getValue();

                tableName = getTableNameByJpaOrCamel(entityClass);

                List<TxField> txFields = new ArrayList<>();
                txFields.addAll(determineIds(entityClass, resultMap.getIds()));
                txFields.addAll(determineResults(resultMap.getResults(), entityClass));
                addExtends(txFields, resultMap, entityClass, mapper.getResultMaps());
                this.fieldList = txFields;
                return Optional.of(entityClass);
            }
        }
        return Optional.empty();
    }

    /**
     * 添加所有父类的 ResultMap
     * @param txFields
     * @param currentResultMap
     * @param entityClass
     * @param resultMaps
     */
    private void addExtends(List<TxField> txFields, ResultMap currentResultMap, PsiClass entityClass, List<ResultMap> resultMaps) {
        GenericAttributeValue<XmlAttributeValue> anExtends = currentResultMap.getExtends();
        String stringValue = anExtends.getStringValue();
        if (StringUtils.isEmpty(stringValue)) {
            return;
        }
        ResultMap foundResultMap = null;
        for (ResultMap resultMap : resultMaps) {
            if (resultMap.getId().getStringValue().equals(stringValue)) {
                foundResultMap = resultMap;
                break;
            }
        }
        if (foundResultMap != null) {
            txFields.addAll(determineResults(foundResultMap.getResults(), entityClass));
            addExtends(txFields, foundResultMap, entityClass, resultMaps);
        }
    }

    private Collection<? extends TxField> determineResults(List<Result> results, PsiClass mapperClass) {
        return results.stream().map(result -> determineField(mapperClass, result.getProperty(), result.getXmlTag())).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Nullable
    private TxField determineField(PsiClass mapperClass, GenericAttributeValue<XmlAttributeValue> property, final XmlTag xmlTag) {
        String propertyValue = property.getStringValue();
        PsiField field = mapperClass.findFieldByName(propertyValue, true);

        String column = null;
        if (xmlTag != null) {
            XmlAttribute columnAttr = xmlTag.getAttribute("column");
            if (columnAttr != null) {
                column = columnAttr.getValue();
            }
        }
        if (field != null) {
            if (column == null) {
                column = getColumnNameByJpaOrCamel(field);
            }
            TxField txField = new TxField();
            txField.setColumnName(column);
            txField.setFieldName(field.getName());
            txField.setFieldType(field.getType().getCanonicalText());
            txField.setTipName(StringUtils.upperCaseFirstChar(field.getName()));
            return txField;
        }
        return null;
    }

    /**
     * resultMap 的子标签<id/>
     *
     * @param mapperClass
     * @param ids
     * @return
     */
    private Collection<? extends TxField> determineIds(PsiClass mapperClass, List<Id> ids) {
        return ids.stream().map(id -> getTxField(mapperClass, id)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * resultMap 的 id
     *
     * @param mapperClass
     * @param id
     * @return
     */
    @Nullable
    private TxField getTxField(PsiClass mapperClass, Id id) {
        return determineField(mapperClass, id.getProperty(), id.getXmlTag());
    }

    /**
     * 找到名字最短的 ResultMap
     *
     * @param resultMaps
     * @return
     */
    private Optional<ResultMap> findResultMap(List<ResultMap> resultMaps) {
        if (resultMaps.size() == 1) {
            return Optional.ofNullable(resultMaps.get(0));
        }
        ResultMap mostShortResultMap = null;
        for (ResultMap resultMap : resultMaps) {
            String currentMapId = resultMap.getId().getStringValue();
            if (mostShortResultMap == null) {
                mostShortResultMap = resultMap;
            } else {
                String shortName = mostShortResultMap.getId().getStringValue();
                assert shortName != null;
                assert currentMapId != null;
                if (shortName.length() >= currentMapId.length()) {
                    mostShortResultMap = resultMap;
                }
            }
        }
        return Optional.ofNullable(mostShortResultMap);
    }
}
