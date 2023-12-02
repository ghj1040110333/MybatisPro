package com.fruit.plugin.idea.mybatis.smartjpa.common.appender;




import com.fruit.plugin.idea.mybatis.smartjpa.common.appender.operator.suffix.SuffixOperator;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxParameter;

import java.util.List;

/**
 * The interface Mx parameter changer.
 */
public interface MxParameterChanger extends SuffixOperator {
    /**
     * Gets parameter.
     *
     * @param txParameter the tx parameter
     * @return the parameter
     */
    List<TxParameter> getParameter(TxParameter txParameter);
}
