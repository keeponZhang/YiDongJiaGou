package com.dongnao.json;

import com.alibaba.fastjson.util.IdentityHashMap;
import com.dongnao.json.fast.JSON;
import com.dongnao.json.fast.TypeReference;
import com.dongnao.json.fast.Utils;
import com.dongnao.json.fast2.serializer.FieldSerializer;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test.json, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    public static class Bean {
        private int i;
        public List<? super Number> list;
        public Map<String, Integer> map;

        public Bean() {

        }


        public void setI(int i) {
            this.i = i;
        }

        public int getI() {
            return i;
        }
    }

    @Test
    public void addition_isCorrect2() throws Exception {
        Map<String, Field> fieldCacheMap = new HashMap<>();
        com.dongnao.json.fast2.Utils.parserAllFieldToCache(fieldCacheMap, Child.class);
        for (Map.Entry<String, Field> stringFieldEntry : fieldCacheMap.entrySet()) {
            System.out.println(stringFieldEntry.getKey() + ":" + stringFieldEntry.getValue().getName());
        }
//
//        List<FieldSerializer> fieldSerializers = com.dongnao.json.fast2.Utils.computeGetters(Child.class, fieldCacheMap);
//        for (FieldSerializer fieldSerializer : fieldSerializers) {
//            System.out.println(fieldSerializer.getFieldInfo().name);
//        }
    }


    @Test
    public void addition_isCorrect4() throws Exception {
        List<List<Child>> childLists = new ArrayList<>();
        List<Child> child1 = new ArrayList<>();
        child1.add(new Child("T1", 100));
        child1.add(new Child("T2", 200));
        childLists.add(child1);
        List<Child> child2 = new ArrayList<>();
        Child t3 = new Child("T3", 300);
        t3.childs = new ArrayList<>();
        t3.childs.add(new Child("T3_1", 3100));
        t3.childs.add(new Child("T3_2", 3200));
        child2.add(t3);
        child2.add(new Child("T4", 400));
        childLists.add(child2);


        String s = com.dongnao.json.fast2.JSON.toJSONString(childLists);
        System.out.println(s);

        Object object = com.alibaba.fastjson.JSON.parseObject(s, new com.alibaba.fastjson.TypeReference<List<List<Child>>>() {
        }.getType());
        System.out.println(object);

    }
    public class B {
        A a;

        public A getA() {
            return a;
        }

        public void setA(A a) {
            this.a = a;
        }
    }

    public class A {
        B b;

        public void setB(B b) {
            this.b = b;
        }

        public B getB() {
            return b;
        }
    }
    @Test
    public void addition_isCorrect5() throws Exception {
        A a = new A();
        B b = new B();
        a.setB(b);
        b.setA(a);

//        String s = com.dongnao.json.fast2.JSON.toJSONString(a);

        String s1 = com.alibaba.fastjson.JSON.toJSONString(a);
    }

    @Test
    public void addition_isCorrect3() throws Exception {
        StringBuffer out = new StringBuffer();
        out.append("{");
        int i = 0;
        for (int j = 0; j < 2; j++) {
            if (i != 0) {
                out.append(",");
            }
            //"name":"testname" 、 "age":100
            // 如果遇到属性没有值 (null) 则返回 ""
            String serializer = "";
            if (j == 0) {
                serializer = "\"name\":\"testname\"";
            }
            out.append(serializer);
            if (serializer.isEmpty()) {
                i = 0;
            } else {
                i = 1;
            }

        }
        out.append("}");
        //{}
        System.out.println(out);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);


        Class<Bean> beanClass = Bean.class;
        System.out.println("所有public属性:");
        for (Field field : beanClass.getFields()) {
            System.out.println(field.getName());
        }

        System.out.println("所有非public属性:");
        for (Field field : beanClass.getDeclaredFields()) {
            System.out.println(field.getName());
        }

        System.out.println("所有public函数:");
        for (Method method : beanClass.getMethods()) {
            String methodName = method.getName();
            System.out.println(methodName);
        }

        System.out.println("所有非public函数:");
        for (Method method : beanClass.getDeclaredMethods()) {
            String methodName = method.getName();
            System.out.println(methodName);
        }

        //指定获得Bean中list属性
        Field list = Bean.class.getField("list");

        // 属性对应的Class如果是List或其子类
        if (List.class.isAssignableFrom(list.getType())) {
            //获得 Type
            Type genericType = list.getGenericType();
            //ParameterizedType
            if (genericType instanceof ParameterizedType) {
                Type type = ((ParameterizedType) genericType).getActualTypeArguments()[0];
                //WildcardType
                if (type instanceof WildcardType) {
                    WildcardType wildcardType = (WildcardType) type;
                    Type[] upperBounds = wildcardType.getUpperBounds();
                    if (upperBounds.length == 1) {
                        Type actualTypeArgument = upperBounds[0];
                        System.out.println("获得泛型上边界类型:" + actualTypeArgument);
                    }
                } else {
                    System.out.println("获得泛型类型:" + type);
                }
            }
        }
        System.out.println(new TypeReference<Object>() {
        }.getType());

    }


}