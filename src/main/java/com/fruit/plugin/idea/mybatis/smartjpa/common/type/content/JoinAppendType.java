package com.fruit.plugin.idea.mybatis.smartjpa.common.type.content;




import com.fruit.plugin.idea.mybatis.smartjpa.common.type.AppendType;
import com.fruit.plugin.idea.mybatis.smartjpa.operate.model.AppendTypeEnum;

import java.util.List;

/**
 * 连接
 */
public class JoinAppendType implements AppendType {
    @Override
    public String getName() {
        return AppendTypeEnum.JOIN.name();
    }

    /**
     * 允许所有字段
     *
     * @return
     */
    @Override
    public List<String> getAllowAfter() {
        return AppendTypeEnum.JOIN.getAllowedAfterList();
    }

}
