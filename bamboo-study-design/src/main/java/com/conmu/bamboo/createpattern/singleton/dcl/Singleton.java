package com.conmu.bamboo.createpattern.singleton.dcl;

/**
 * 双检锁/双重校验锁（DCL，即 double-checked locking）
 *
 * @author mucongcong
 * @date 2022/08/05 17:54
 * @since
 **/
public class Singleton {
    private volatile static Singleton singleton;
    private Singleton (){}
    public static Singleton getSingleton() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
