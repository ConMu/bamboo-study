package com.conmu.bamboo.createpattern.singleton.hungry;

/**
 * 饿汉式
 * 描述：这种方式比较常用，但容易产生垃圾对象。
 * 没有加锁，执行效率会提高。
 *
 * @author mucongcong
 * @date 2022/08/05 17:52
 * @since
 **/
public class Singleton {
    private static Singleton instance = new Singleton();
    private Singleton (){}
    public static Singleton getInstance() {
        return instance;
    }
}
