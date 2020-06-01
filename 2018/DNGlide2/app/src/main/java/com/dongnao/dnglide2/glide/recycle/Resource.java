package com.dongnao.dnglide2.glide.recycle;

import android.graphics.Bitmap;

import com.dongnao.dnglide2.glide.cache.Key;

/**
 * @author Lance
 * @date 2018/4/21
 */

public class Resource {
    private Bitmap bitmap;
    private int acquired;
    private Key key;
    private ResourceListener listener;

    public interface ResourceListener {
        void onResourceReleased(Key key, Resource resource);
    }

    public Resource(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void recycle() {
        if (acquired > 0) {
            return;
        }
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public void setResourceListener(Key key, ResourceListener listener) {
        this.key = key;
        this.listener = listener;
    }

    public void acquire() {
        if (bitmap.isRecycled()) {
            throw new IllegalStateException("Cannot acquire a recycled resource");
        }
        ++acquired;
    }

    public void release() {
        if (--acquired == 0) {
            listener.onResourceReleased(key, this);
        }
    }

}
