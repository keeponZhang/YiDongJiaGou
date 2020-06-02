package com.dongnao.dnglide2.glide2;

import com.dongnao.dnglide2.glide2.manager.Lifecycle;
import com.dongnao.dnglide2.glide2.manager.LifecycleListener;
import com.dongnao.dnglide2.glide2.manager.RequestTrack;
import com.dongnao.dnglide2.glide2.request.Request;

/**
 * Created by Administrator on 2018/5/9.
 */

public class RequestManager implements LifecycleListener {

    private final Lifecycle lifecycle;
    RequestTrack requestTrack;

    public RequestManager(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
        //注册生命周期回调监听
        lifecycle.addListener(this);
        requestTrack = new RequestTrack();
    }

    @Override
    public void onStart() {
        //继续请求
        resumeRequests();
    }

    @Override
    public void onStop() {
        //停止所有请求
        pauseRequests();
    }

    @Override
    public void onDestroy() {
        lifecycle.removeListener(this);
        requestTrack.clearRequests();
    }


    public void pauseRequests() {
        requestTrack.pauseRequests();
    }

    public void resumeRequests() {
        requestTrack.resumeRequests();
    }


    public RequestBuilder load(String string) {
        return new RequestBuilder(this).load(string);
    }

    /**
     * 管理Request
     */
    public void track(Request request) {
        requestTrack.runReuqest(request);
    }
}
