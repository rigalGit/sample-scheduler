package com.example.scheduler.service;

import com.example.scheduler.models.ScheduleTask;
import com.example.scheduler.models.TaskType;

import java.time.Instant;
import java.util.concurrent.*;

public class SchedulerServiceImpl implements SchedulerService {


    private ExecutorService executorService;
    private PriorityBlockingQueue<ScheduleTask> blockingQueue;
    private ScheduleWorker scheduleWorker;
    public SchedulerServiceImpl(ExecutorService executorService) {
        this.executorService = executorService;
        blockingQueue = new PriorityBlockingQueue<>();
        scheduleWorker = new ScheduleWorker(blockingQueue,executorService);
        scheduleWorker.start();
    }






    @Override
    public void schedule(Runnable runnable, long delay, TimeUnit timeUnit) {
        validateArgs(runnable, delay);
        Instant executeAt = Instant.ofEpochMilli(Instant.now().toEpochMilli() + timeUnit.toMillis(delay));
        ScheduleTask scheduleTask  = new ScheduleTask(TaskType.ONE_TIME, runnable,delay,timeUnit,executeAt);
        blockingQueue.add(scheduleTask);
        synchronized (scheduleWorker){
            scheduleWorker.notifyAll();
        }

    }

    private void validateArgs(Runnable runnable, long delay) {
        if( runnable == null || delay < 0){
            throw new IllegalArgumentException("Not valid args");
        }
    }

    @Override
    public void scheduleAtFixedRate(Runnable runnable, long delay, TimeUnit timeUnit) {
        validateArgs(runnable, delay);
        Instant executeAt = Instant.ofEpochMilli(Instant.now().toEpochMilli() + timeUnit.toMillis(delay));
        ScheduleTask scheduleTask  = new ScheduleTask(TaskType.RECURRING, runnable,delay,timeUnit,executeAt);
        blockingQueue.add(scheduleTask);
        scheduleWorker.notify();
    }
}
