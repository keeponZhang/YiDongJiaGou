package com.dongnao.dnglide2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.dongnao.dnglide2.glide.cache.LruArrayPool;
import com.dongnao.dnglide2.glide.load.codec.MarkInputStream;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.sql.SQLOutput;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.dongnao.dnglide2", appContext.getPackageName());

//        LruArrayPool arrayPool = new LruArrayPool(10);
//        byte[] bytes = arrayPool.get(10);
//        System.out.println(bytes.length);
//        arrayPool.put(new byte[3]);
//        System.out.println("加入3后大小:"+arrayPool.size());
//        arrayPool.put(new byte[4]);
//        System.out.println("加入4后大小:"+arrayPool.size());
//        arrayPool.put(new byte[5]);
//        System.out.println("加入5后大小:"+arrayPool.size());
//        arrayPool.put(new byte[6]);
//        System.out.println("加入6后大小:"+arrayPool.size());
//        arrayPool.put(new byte[7]);
//        System.out.println("加入7后大小:"+arrayPool.size());


        ByteArrayInputStream bos = new ByteArrayInputStream(new byte[]{1, 2, 3,
                4, 5});
        MarkInputStream is = new MarkInputStream(bos, new LruArrayPool());
        is.mark(0);
        byte[] bytes = new byte[5];
        is.read(bytes);
        System.out.println(Arrays.toString(bytes));
        is.reset();

        bytes = new byte[5];
        is.read(bytes);
        System.out.println(Arrays.toString(bytes));


        is.reset();


        for (int i = 0; i < 5; i++) {
            System.out.print(is.read());
        }
        System.out.println("");
        is.reset();


        for (int i = 0; i < 6; i++) {
            System.out.print(is.read());
        }
        System.out.println("");


        is.reset();

        bytes = new byte[10];
        is.read(bytes);
        System.out.println(Arrays.toString(bytes));
    }
}
