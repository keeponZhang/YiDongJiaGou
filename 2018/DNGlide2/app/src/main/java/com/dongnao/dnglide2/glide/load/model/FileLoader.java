package com.dongnao.dnglide2.glide.load.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.InputStream;

/**
 * @author Lance
 * @date 2018/4/21
 */

public class FileLoader<Data> implements ModelLoader<File, Data> {
    private final ModelLoader<Uri, Data> loader;


    public FileLoader(ModelLoader<Uri, Data> loader) {
        this.loader = loader;
    }


    @Override
    public LoadData<Data> buildLoadData(File file) {
        return loader.buildLoadData(Uri.fromFile(file));
    }

    @Override
    public boolean handles(File file) {
        return true;
    }

    public static class Factory implements ModelLoaderFactory<File, InputStream> {

        @NonNull
        @Override
        public ModelLoader<File, InputStream> build(ModelLoaderRegistry modelLoaderRegistry) {
            return new FileLoader(modelLoaderRegistry.build(Uri.class, InputStream
                    .class));
        }

    }

}
