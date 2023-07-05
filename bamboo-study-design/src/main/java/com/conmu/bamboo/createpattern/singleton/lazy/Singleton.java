package com.conmu.bamboo.createpattern.singleton.lazy;

/**
 * 懒汉式，线程不安全
 *
 * @author mucongcong
 * @date 2022/08/05 17:49
 * @since
 **/
public class Singleton {
    private Singleton(){}

    private static Singleton instance;

    public static Singleton getInstance(){
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

}
