package com.fruit.plugin.idea.mybatis.smartjpa.common.type.content;




import com.fruit.plugin.idea.mybatis.smartjpa.common.type.AppendType;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.model.AppendTypeEnum;

import java.util.List;

/**
 * 排序区
 */
public class SuffixAppendType implements AppendType {
    @Override
    public String getName() {
        return AppendTypeEnum.SUFFIX.name();
    }

    /**
     * 允许所有字段
     *
     * @return
     */
    @Override
    public List<String> getAllowAfter() {
        return AppendTypeEnum.SUFFIX.getAllowedAfterList();
    }
}
