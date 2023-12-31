package com.fruit.plugin.idea.mybatis.core;

import com.fruit.plugin.idea.mybatis.core.impl.MapperManagerImpl;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiJavaFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Mapper管理
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018 /08/06 10:24
 */
public interface MapperManager {
    /**
     * 获取实例
     *
     * @param project 项目对象，非空
     * @return mapper管理实例对象 instance
     */
    static MapperManager getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, MapperManagerImpl.class);
    }

    /**
     * Init.
     */
    void init();

    /**
     * 通过module获取所有Mapper类文件
     *
     * @param module 模型对象
     * @return Mapper类文件集合 mapper cls file by module
     */
    List<PsiJavaFile> getMapperClsFileByModule(@NotNull Module module);

    /**
     * 获取项目中所有的Mapper类文件
     *
     * @return Mapper类文件集合 all mapper cls file
     */
    List<PsiJavaFile> getAllMapperClsFile();
}
