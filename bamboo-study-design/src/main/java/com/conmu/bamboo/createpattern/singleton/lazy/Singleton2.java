package com.conmu.bamboo.createpattern.singleton.lazy;

/**
 * 懒汉式，线程安全
 *
 * @author mucongcong
 * @date 2022/08/05 17:50
 * @since
 **/
public class Singleton2 {
    private static Singleton2 instance;
    private Singleton2 (){}
    public static synchronized Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }
}
