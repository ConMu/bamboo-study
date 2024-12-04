package com.conmu.concurrent;

import java.util.concurrent.*;

import static org.springframework.boot.Banner.Mode.LOG;

/**
 * 线程创建 普通线程、延迟队列线程、定时任务线程
 * CyclicBarrier
 * @author mucongcong
 * @date 2024/10/28 15:33
 * @since
 **/
public class Demo {

    public static void main(String[] args) throws InterruptedException {
        // 创建线程方式
        Executor executor = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(1000));
        executor.execute(()->{
            System.out.println("1");
        });

        // ExecutorService维持了一个队列，需要shutdown
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(() -> {
            System.out.println("2");
        });
        executorService.shutdown();
        ExecutorService executorService1 = Executors.newSingleThreadExecutor();
        executorService1.submit(() -> {
            System.out.println("3");
        });
        executorService1.shutdown();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 延迟任务
        scheduledExecutorService.schedule(() -> {
            System.out.println("4");
        },5, TimeUnit.SECONDS);
        // 周期任务
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println("5");
        },0,1,TimeUnit.SECONDS);
        // Future回调
        Future<?> future = scheduledExecutorService.schedule(() -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("6");
            return 6;
        }, 0, TimeUnit.SECONDS);
        if (future.isDone() && !future.isCancelled()) {
            try {
//                Integer o = (Integer)future.get();
                // future 设置超时时间
                Integer o = (Integer)future.get(5, TimeUnit.SECONDS);
                System.out.println(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Demo demo = new Demo();
        // CyclicBarrier线程相互等待
        demo.start();

        Thread.sleep(100000);

    }

    public void start() {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> {
            // ...
            System.out.println("All previous tasks are completed");
        });

        Thread t1 = new Thread(new Task(cyclicBarrier), "T1");
        Thread t2 = new Thread(new Task(cyclicBarrier), "T2");
        Thread t3 = new Thread(new Task(cyclicBarrier), "T3");

        if (!cyclicBarrier.isBroken()) {
            t1.start();
            t2.start();
            t3.start();
        }
    }
    class Task implements Runnable {
        private CyclicBarrier cyclicBarrier;

        public Task(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() +
                        " is waiting");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() +
                        " is released");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Description 创建线程
     *
     * @Param
     * @return
     **/
    class TreadFactoryUse implements ThreadFactory {
        private int threadId;
        private String name;

        public TreadFactoryUse(String name) {
            threadId = 1;
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, name + "-Thread_" + threadId);
            System.out.println("created new thread with id : " + threadId +
                    " and name : " + t.getName());
            threadId++;
            return t;
        }
    }
}