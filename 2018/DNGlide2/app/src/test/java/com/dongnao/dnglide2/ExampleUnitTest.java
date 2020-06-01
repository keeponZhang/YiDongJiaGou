package com.dongnao.dnglide2;

import com.dongnao.dnglide2.glide2.cache.recycle.Resource;

import org.junit.Test;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

//        Resource resource = new Resource();
//        resource.setResourceListener(new Resource.ResourceListener(){
//
//            @Override
//            public void onResourceReleased() {
//                //当前这个resource没有在使用了
//            }
//        });
//        resource.acquire();//有1个地方在使用这个resource
//        resource.release();
        //

        //强引用
        String a = new String("1");
        //弱引用
        final ReferenceQueue<String> queue = new ReferenceQueue<>();
        new Thread(){
            @Override
            public void run() {
                try {
                    Reference<? extends String> remove = queue.remove();
                    System.out.println("回收掉:"+remove);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        WeakReference<String> weakReference = new WeakReference<>(a,queue);
        System.out.println("若引用1:"+weakReference.get());
        a = null;
        System.gc();
        System.out.println("若引用2:"+weakReference.get());


        Thread.sleep(10_000);
    }
}