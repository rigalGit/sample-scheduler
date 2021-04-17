package com.example.scheduler;

import com.example.scheduler.service.SchedulerService;
import com.example.scheduler.service.SchedulerServiceImpl;

import java.io.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Test {

    public static void main(String[] args) throws InterruptedException, IOException {

        ExecutorService executorService = Executors.newFixedThreadPool(2);
//        SchedulerService schedulerService = new SchedulerServiceImpl(executorService);
//        schedulerService.schedule(getRunnable(),1, TimeUnit.SECONDS);
//        Thread.sleep(5*1000);
//        schedulerService.schedule(getRunnable(),2, TimeUnit.SECONDS);
//        schedulerService.scheduleAtFixedRate(getRunnable2(),2, TimeUnit.SECONDS);
//        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();
//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    Integer poll = blockingQueue.poll();
//                    System.out.println("poll = " + poll);
//                }
//            }
//        });
//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(10*1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    blockingQueue.offer(1);
//                    System.out.println("addd element");
//                }
//            }
//        });
//        Thread.sleep(40*1000);


        Instant now = Instant.now();
        System.out.println("now = " + now);
        Instant instant = now.truncatedTo(ChronoUnit.MINUTES);
        System.out.println("instant = " + instant);


        BufferedReader fileReader = new BufferedReader(new FileReader("/tmp/hello"));
        String s = fileReader.readLine();
        System.out.println("s = " + s);



//        File oFile = new File("/tmp/hello");
//        long init = oFile.length();
//        while (true){
//
//            RandomAccessFile file = new RandomAccessFile(oFile,"r");
//
//            long length = oFile.length();
//            System.out.println("length "+length);
//            System.out.println("init  "+init);
//            if ( length > init) {
//                file.seek(init);
//                String str = null;
//                while ((str = file.readLine()) != null){
//                    System.out.println(str);
//                }
//                init = file.getFilePointer();
//                file.close();
//            }else {
//                Thread.sleep(1*1000);
//            }
//        }


        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();
        ReadWriteLock lock = new ReentrantReadWriteLock();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("acuiring lock");
                    Lock lock1 = lock.readLock();
                    lock1.lock();
                    sleep(5);
                    lock1.unlock();
                    System.out.println("acuired lock");
                }
            }
        });
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    sleep(10);
                    Lock lock1 = lock.writeLock();

                    System.out.println("acquiring write lock");
                    lock1.lock();
                    System.out.println("acquired write lock");
                    sleep(10);
                    System.out.println("released write lock");
//                    Semaphore semaphore = new Semaphore(10);
//                    semaphore.acquire();
//                    semaphore.release();
                }
            }
        });
        Thread.sleep(40*1000);

    }

    private static void sleep(int i) {
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static Runnable getRunnable(){
        return  new Runnable() {
            @Override
            public void run() {
                System.out.println("I am running-1 "+Thread.currentThread().getId());
                sleep(10);

                System.out.println("finshed processing-1 "+Thread.currentThread().getId());
            }
        };
    }

    private static Runnable getRunnable2(){
        return  new Runnable() {
            @Override
            public void run() {
                System.out.println("I am running-2 "+Thread.currentThread().getId());
                sleep(10);

                System.out.println("finshed processing-2 "+Thread.currentThread().getId());
            }
        };
    }
}
