package com.example.scheduler.service;

import com.example.scheduler.models.ScheduleTask;
import com.example.scheduler.models.TaskType;

import java.time.Instant;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ScheduleWorker {

    private PriorityBlockingQueue<ScheduleTask> blockingQueue;
    private ExecutorService worker;
    private ExecutorService executorService;

    public ScheduleWorker(PriorityBlockingQueue<ScheduleTask> blockingQueue,  ExecutorService executorService) {
        this.blockingQueue = blockingQueue;
        this.worker =Executors.newSingleThreadExecutor();;
        this.executorService = executorService;
    }

    public void start(){
        Object object = this;

        worker.submit(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        if ( !blockingQueue.isEmpty()){
                            ScheduleTask task = blockingQueue.peek();
                            if(task.getNextExecution().toEpochMilli() <= Instant.now().toEpochMilli()){
                                System.out.println("executing task  "+task.getDelay());
                                ScheduleTask task2 = blockingQueue.poll();
                                executorService.submit(getRunnableWrapper(task2));
                                if(task2.getTaskType().equals(TaskType.RECURRING)){
                                    putInQueueAgain(task2);
                                }
                            }else {
                                long time = task.getNextExecution().toEpochMilli() - Instant.now().toEpochMilli();
                                System.out.println("waiting for  "+time+" ms ");
                                synchronized (object) {
                                    object.wait(time);
                                }
                            }
                        }else {
                            System.out.println("waiting for 20 seconds start ");
                            synchronized (object) {
                                object.wait(20 * 1000);
                            }
                            System.out.println("waiting for 20 seconds done  ");
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }




    private Runnable getRunnableWrapper(ScheduleTask task2) {


        return task2.getRunnable();
    }

    private void putInQueueAgain(ScheduleTask task2) {

        Instant executeAt = Instant.ofEpochMilli(task2.getNextExecution().toEpochMilli() + task2.getTimeUnit().toMillis(task2.getDelay()));
        blockingQueue.offer(new ScheduleTask(task2.getTaskType(),task2.getRunnable(),task2.getDelay(),task2.getTimeUnit(),executeAt));
    }

    public void stop(){
        worker.shutdown();
    }
}
