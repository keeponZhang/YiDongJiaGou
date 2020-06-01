package com.dongnao.dnglide2.glide.load.model;


import com.dongnao.dnglide2.glide.cache.Key;
import com.dongnao.dnglide2.glide.load.model.data.DataFetcher;

/**
 * @author Lance
 * @date 2018/4/20
 * 模型加载器
 */
public interface ModelLoader<Model, Data> {

    interface ModelLoaderFactory<Model, Data> {
        ModelLoader<Model, Data> build(ModelLoaderRegistry modelLoaderRegistry);
    }

    class LoadData<Data> {
        public final Key sourceKey;
        //负责加载数据
        public final DataFetcher<Data> fetcher;

        public LoadData(Key sourceKey, DataFetcher<Data> fetcher) {
            this.sourceKey = sourceKey;
            this.fetcher = fetcher;
        }

    }
    //构造一个LoadData
    LoadData<Data> buildLoadData(Model model);

    boolean handles(Model model);
}
