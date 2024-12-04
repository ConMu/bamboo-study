package com.conmu.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author mucongcong
 * @date 2024/10/28 17:42
 * @since
 **/
public class SynchonizeDemo {


    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        SynchronizedMethods synchronizedMethods = new SynchronizedMethods();
        IntStream.range(0, 1000).forEach(s -> executorService.submit(synchronizedMethods::cal));
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        System.out.println(synchronizedMethods.getSum());

        IntStream.range(0, 1000).forEach(s -> executorService.submit(SynchronizedMethods::staticCal));
        executorService.shutdown();
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        System.out.println(SynchronizedMethods.getStaticSum());
    }

    public static class SynchronizedMethods {
        private int sum = 0;

        private static int staticSum = 0;

        public int getSum() {
            return sum;
        }

        public static int getStaticSum() {
            return staticSum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        // 锁方法1
//        public synchronized void cal() {
//            setSum(getSum() + 1);
//        }

        // 锁方法2 blocks
        public void cal() {
            synchronized (this) {
                setSum(getSum() + 1);
            }
        }

        // 静态锁对象
//        public synchronized static void staticCal(){
//            staticSum++;
//        }

        // 静态锁对象2,block方法
        public synchronized static void staticCal() {
            synchronized (SynchronizedMethods.class) {
                staticSum++;
            }
        }
    }




}