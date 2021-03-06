package com.dongnao.dnglide2.glide.load.model;

import android.net.Uri;

import java.io.File;
import java.io.InputStream;

/**
 * @author Lance
 * @date 2018/4/21
 */

public class StringLoader<Data> implements ModelLoader<String, Data> {

    /**
     * 代理(在这里是MutilLoader)
     */
    private final ModelLoader<Uri, Data> uriLoader;

    public StringLoader(ModelLoader<Uri, Data> uriLoader) {
        this.uriLoader = uriLoader;
    }

    @Override
    public LoadData<Data> buildLoadData(String model) {
        //本地文件和在线地址都是uri，这里把String转成Uri，真正干活的uriLoader
        Uri uri;
        if (model.startsWith("/")) {
            //这需要包装一层
            uri = Uri.fromFile(new File(model));
        } else {
            uri = Uri.parse(model);
        }
        //注意参数，这里是传的uri，因为是uriLoader处理的，在里面也会调用handle方法，决定真正处理的loader
        return uriLoader.buildLoadData(uri);
    }

    @Override
    public boolean handles(String s) {
        return true;
    }


    public static class StreamFactory implements ModelLoaderFactory<String, InputStream> {

        /**
         * 将String 交给 Uri 的组件处理
         *
         * @param modelLoaderRegistry
         * @return
         */
        @Override
        public ModelLoader<String, InputStream> build(ModelLoaderRegistry modelLoaderRegistry) {
            //class com.dongnao.dnglide2.glide.load.model.MultiModelLoader
            return new StringLoader<>(modelLoaderRegistry.build(Uri.class, InputStream.class));
        }
    }

}
