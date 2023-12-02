package com.fruit.plugin.idea.mybatis.smartjpa.operate.generate;

import com.fruit.plugin.idea.mybatis.smartjpa.common.iftest.ConditionFieldWrapper;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TxField;
import com.fruit.plugin.idea.mybatis.smartjpa.component.TypeDescriptor;
import com.intellij.psi.PsiMethod;

import java.util.List;

/**
 * 平台生成器, 为后续生成 注解, springjpa注解等方式预留
 */
public interface PlatformGenerator {
    /**
     * 获取参数
     *
     * @return parameter
     */
    TypeDescriptor getParameter();

    /**
     * 返回值描述符
     *
     * @return return
     */
    TypeDescriptor getReturn();

    /**
     * 生成mapper方法
     *
     * @param psiMethod             PSI 方法描述
     * @param methodGenerator       方法生成操作,  自定义实现可以生成 spring jpa, mybatis xml, mybatis 注解
     * @param conditionFieldWrapper the condition field wrapper
     */
    void generateMapperXml(PsiMethod psiMethod, Generator methodGenerator, ConditionFieldWrapper conditionFieldWrapper);

    /**
     * Gets condition fields.
     *
     * @return the condition fields
     */
    List<String> getConditionFields();

    /**
     * Gets all fields.
     *
     * @return the all fields
     */
    List<TxField> getAllFields();

    /**
     * Gets entity class.
     *
     * @return the entity class
     */
    String getEntityClass();
}
