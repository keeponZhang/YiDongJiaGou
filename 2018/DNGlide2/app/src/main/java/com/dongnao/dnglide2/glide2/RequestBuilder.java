package com.dongnao.dnglide2.glide2;

import android.widget.ImageView;

import com.dongnao.dnglide2.glide2.request.Request;

/**
 * Created by Administrator on 2018/5/9.
 */

public class RequestBuilder {

    private RequestManager requestManager;
    private Object model;

    public RequestBuilder(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    public RequestBuilder load(String string) {
        model = string;
        return this;
    }

    /**
     * 加载图片并设置到ImageView当中
     *
     * @param view
     */
    public void into(ImageView view) {
        //将View交给Target
        Target target = new Target(view);
        //图片加载与设置
        Request request = new Request(model, target);
        //Request交给 RequestManager 管理
        requestManager.track(request);
    }
}
