package com.fruit.plugin.idea.mybatis.smartjpa.common.type.content;




import com.fruit.plugin.idea.mybatis.smartjpa.common.type.AppendType;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.model.AppendTypeEnum;

import java.util.List;

/**
 * 字段
 */
public class FieldAppendType implements AppendType {
    @Override
    public String getName() {
        return AppendTypeEnum.FIELD.name();
    }

    /**
     * 允许所有区域
     *
     * @return
     */
    @Override
    public List<String> getAllowAfter() {
        return AppendTypeEnum.FIELD.getAllowedAfterList();
    }

}
