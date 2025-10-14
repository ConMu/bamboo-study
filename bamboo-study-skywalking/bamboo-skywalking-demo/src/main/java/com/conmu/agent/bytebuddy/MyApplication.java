package com.conmu.agent.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.pool.TypePool;

import java.lang.reflect.Field;

/**
 * @author mucongcong
 * @date 2024/12/04 11:16
 * @since
 **/
public class MyApplication {
    public static void main(String[] args) throws NoSuchFieldException {
        TypePool typePool = TypePool.Default.ofSystemLoader();
        Class type = new ByteBuddy()
                .redefine(typePool.describe("foo.Bar").resolve(), // do not use 'Bar.class'
                        ClassFileLocator.ForClassLoader.ofPlatformLoader())
                .defineField("qux", String.class) // we learn more about defining fields later
                .make()
                .load(ClassLoadingStrategy.BOOTSTRAP_LOADER, ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        Field qux = type.getDeclaredField("qux");
        System.out.println(qux);
    }
}