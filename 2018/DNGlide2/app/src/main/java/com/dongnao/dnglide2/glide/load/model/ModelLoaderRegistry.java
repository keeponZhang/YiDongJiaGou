package com.dongnao.dnglide2.glide.load.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lance
 * @date 2018/4/21
 */
// ModelLoaderRegistry负责注册所有支持的ModelLoader
public class ModelLoaderRegistry {
    private final List<Entry<?, ?>> entries = new ArrayList<>();
    /**
     * 注册一个对应model与data类型的加载器
     *
     * @param modelClass 数据来源模型类型
     * @param dataClass  加载后数据类型
     * @param factory    创建Loader工厂
     * @param <Model>
     * @param <Data>
     */
    public synchronized <Model, Data> void add(
            Class<Model> modelClass,
            Class<Data> dataClass,
            ModelLoader.ModelLoaderFactory<? extends Model, ? extends Data> factory) {
        Entry<Model, Data> entry = new Entry<>(modelClass, dataClass, factory);
        entries.add(entry);
    }

    /**
     * 通过对应类型(模型与数据)获得所有对应ModerLoader的组装-MultiModelLoader
     *
     * @param modelClass
     * @param dataClass
     * @param <Model>
     * @param <Data>
     * @return
     */
    public synchronized <Model, Data> ModelLoader<Model, Data> build(Class<Model> modelClass,
                                                                     Class<Data> dataClass) {
        List<ModelLoader<Model, Data>> loaders = new ArrayList<>();
        //从注册的modelLoader寻找符合条件的
        for (Entry<?, ?> entry : entries) {
            if (entry.handles(modelClass, dataClass)) {
                loaders.add((ModelLoader<Model, Data>) entry.factory.build(this));
            }
        }
        if (loaders.size() > 1) {
            return new MultiModelLoader<>(loaders);
        } else if (loaders.size() == 1) {
            return loaders.get(0);
        }
        throw new RuntimeException("No Have:" + modelClass.getName() + " Model Match " +
                dataClass.getName() + " Data");
    }


    /**
     * 获得符合model类型的loader集合
     *
     * @param modelClass
     * @param <Model>
     * @return
     */
    public <Model> List<ModelLoader<Model, ?>> getModelLoaders(Class<Model> modelClass) {
        List<ModelLoader<Model, ?>> modelLoaders = new ArrayList<>();
        for (Entry<?, ?> entry : entries) {
            //model 符合的加入集合，model通过handles方法筛选
            if (entry.handles(modelClass)) {
                modelLoaders.add((ModelLoader<Model, ?>) entry.factory.build(this));
            }
        }
        //StringLoader
        return modelLoaders;
    }


    private static class Entry<Model, Data> {
        private final Class<Model> modelClass;
        final Class<Data> dataClass;
        final ModelLoader.ModelLoaderFactory<? extends Model, ? extends Data> factory;

        public Entry(
                Class<Model> modelClass,
                Class<Data> dataClass,
                ModelLoader.ModelLoaderFactory<? extends Model, ? extends Data> factory) {
            this.modelClass = modelClass;
            this.dataClass = dataClass;
            this.factory = factory;
        }

        public boolean handles(@NonNull Class<?> modelClass, @NonNull Class<?> dataClass) {
            return handles(modelClass) && this.dataClass.isAssignableFrom(dataClass);
        }

        public boolean handles(@NonNull Class<?> modelClass) {
            return this.modelClass.isAssignableFrom(modelClass);
        }
    }

}
