package com.example.scheduler;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MySampleClass {


    private ExecutorService executorService = Executors.newSingleThreadExecutor();



    public  MFuture<Integer> doSOme(String input) throws InterruptedException {


        MyFutureImpl myFuture = new MyFutureImpl(getCallable());
        executorService.submit(myFuture);
        return myFuture;
    }


    private Callable<Integer> getCallable(){
        return new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println(" called with input "+new Date());
                Thread.sleep(5*1000);
                System.out.println("Done with input "+new Date());
                return 10;
            }
        };
    }



    public static class MyFutureImpl implements MFuture<Integer>,Runnable {
//        private volatile boolean isDone = false;
        private  transient boolean isDone = false;
//        private  boolean isDone = false;
        private Callable<Integer> callable;
        private Integer result = null;

        public MyFutureImpl(Callable<Integer> callable) {
            this.callable = callable;
        }


        @Override
        public Integer get() throws InterruptedException {
            while (!isDone){
//                Thread.sleep(100);
            }
            return result;
        }

        @Override
        public boolean isDone() {
            return isDone;
        }

        @Override
        public void run()  {
            Integer call = null;
            try {
                call = callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.result = call;
            this.isDone = true;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MySampleClass sampleClass = new MySampleClass();
        MFuture<Integer> integerMFuture = sampleClass.doSOme("");
        integerMFuture.get();





        System.out.println("integerMFuture "+integerMFuture.isDone());




        System.out.println("integerMFuture "+integerMFuture.isDone());
        Thread.sleep(10*1000);

        System.out.println("integerMFuture "+integerMFuture.get());
        System.out.println("integerMFuture "+integerMFuture.isDone());

    }
}
