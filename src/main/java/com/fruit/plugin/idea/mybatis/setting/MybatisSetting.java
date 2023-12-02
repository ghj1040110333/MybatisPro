package com.fruit.plugin.idea.mybatis.setting;

import com.fruit.plugin.idea.mybatis.generate.AbstractStatementGenerator;
import com.fruit.plugin.idea.mybatis.generate.GenerateModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * The type Mybatis setting.
 *
 * @author yanglin
 */
@State(
        name = "MybatisSettings",
        storages = @Storage(value = "$APP_CONFIG$/mybatis.xml"))
public class MybatisSetting implements PersistentStateComponent<Element> {

    private GenerateModel statementGenerateModel;

    private Gson gson = new Gson();

    private Type gsonTypeToken = new TypeToken<Set<String>>() {
    }.getType();

    /**
     * Instantiates a new Mybatis setting.
     */
    public MybatisSetting() {
        statementGenerateModel = GenerateModel.START_WITH_MODEL;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static MybatisSetting getInstance() {
        return ServiceManager.getService(MybatisSetting.class);
    }

    @Nullable
    @Override
    public Element getState() {
        Element element = new Element("MybatisSettings");
        element.setAttribute(AbstractStatementGenerator.INSERT_GENERATOR.getId(), gson.toJson(AbstractStatementGenerator.INSERT_GENERATOR.getPatterns()));
        element.setAttribute(AbstractStatementGenerator.DELETE_GENERATOR.getId(), gson.toJson(AbstractStatementGenerator.DELETE_GENERATOR.getPatterns()));
        element.setAttribute(AbstractStatementGenerator.UPDATE_GENERATOR.getId(), gson.toJson(AbstractStatementGenerator.UPDATE_GENERATOR.getPatterns()));
        element.setAttribute(AbstractStatementGenerator.SELECT_GENERATOR.getId(), gson.toJson(AbstractStatementGenerator.SELECT_GENERATOR.getPatterns()));
        element.setAttribute("statementGenerateModel", String.valueOf(statementGenerateModel.getIdentifier()));
        return element;
    }

    @Override
    public void loadState(Element state) {
        loadState(state, AbstractStatementGenerator.INSERT_GENERATOR);
        loadState(state, AbstractStatementGenerator.DELETE_GENERATOR);
        loadState(state, AbstractStatementGenerator.UPDATE_GENERATOR);
        loadState(state, AbstractStatementGenerator.SELECT_GENERATOR);
        statementGenerateModel = GenerateModel.getInstance(state.getAttributeValue("statementGenerateModel"));
    }

    private void loadState(Element state, AbstractStatementGenerator generator) {
        String attribute = state.getAttributeValue(generator.getId());
        if (null != attribute) {
            generator.setPatterns(gson.fromJson(attribute, gsonTypeToken));
        }
    }

    /**
     * Gets statement generate model.
     *
     * @return the statement generate model
     */
    public GenerateModel getStatementGenerateModel() {
        return statementGenerateModel;
    }

    /**
     * Sets statement generate model.
     *
     * @param statementGenerateModel the statement generate model
     */
    public void setStatementGenerateModel(GenerateModel statementGenerateModel) {
        this.statementGenerateModel = statementGenerateModel;
    }
}
