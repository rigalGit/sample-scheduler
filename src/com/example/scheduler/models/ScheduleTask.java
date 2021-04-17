package com.example.scheduler.models;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class ScheduleTask implements Comparable{
    private TaskType taskType;
    private Runnable runnable;
    private long delay;
    private TimeUnit timeUnit;
    private Instant nextExecution;


    public ScheduleTask(TaskType taskType, Runnable runnable, long delay, TimeUnit timeUnit, Instant nextExecution) {
        this.taskType = taskType;
        this.runnable = runnable;
        this.delay = delay;
        this.timeUnit = timeUnit;
        this.nextExecution = nextExecution;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public long getDelay() {
        return delay;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public Instant getNextExecution() {
        return nextExecution;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public int compareTo(Object o) {
        ScheduleTask task2 = (ScheduleTask) o;
        return (int) (this.getNextExecution().toEpochMilli() - task2.getNextExecution().toEpochMilli());

    }
}
