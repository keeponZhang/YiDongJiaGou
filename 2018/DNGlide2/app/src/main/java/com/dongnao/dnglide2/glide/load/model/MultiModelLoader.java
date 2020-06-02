package com.dongnao.dnglide2.glide.load.model;

import com.dongnao.dnglide2.glide.cache.Key;
import com.dongnao.dnglide2.glide.load.model.data.DataFetcher;
import com.dongnao.dnglide2.glide.load.model.data.MultiFetcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lance
 * @date 2018/4/21
 */
// 这个MultiModelLoader中存在一个集合，只要集合中存在一个Loader能够处理对应的Model，那么这个MultiModelLoader就可以处理对应的Model。
public class MultiModelLoader<Model, Data> implements ModelLoader<Model, Data> {
    //代理多个modelloader
    // 所以当需要处理String类型的来源的时候，会创建一个MultiModelLoader，
    // 这个MultiModelLoader中包含了一个HttpUriLoader与一个UriFileLoader。
    // 当字符串是以http或者https开头则能由HttpUriLoader处理，否则交给UriFileLoader来加载。
    private final List<ModelLoader<Model, Data>> modelLoaders;

    MultiModelLoader(List<ModelLoader<Model, Data>> modelLoaders) {
        this.modelLoaders = modelLoaders;
    }

    @Override
    public LoadData<Data> buildLoadData(Model model) {
        Key sourceKey = null;
        int size = modelLoaders.size();
        List<DataFetcher<Data>> fetchers = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ModelLoader<Model, Data> modelLoader = modelLoaders.get(i);
            //这里会确定HttpUriLoader处理，否则交给UriFileLoader来加载。
            if (modelLoader.handles(model)) {
                LoadData<Data> loadData = modelLoader.buildLoadData(model);
                if (loadData != null) {
                    sourceKey = loadData.sourceKey;
                    fetchers.add(loadData.fetcher);
                }
            }
        }
        return !fetchers.isEmpty() && sourceKey != null
                ? new LoadData<>(sourceKey, new MultiFetcher<>(fetchers)) : null;
    }

    @Override
    public boolean handles(Model model) {
        for (ModelLoader<Model, Data> modelLoader : modelLoaders) {
            if (modelLoader.handles(model)) {
                return true;
            }
        }
        return false;
    }


}
