package com.example.scheduler.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public interface SchedulerService {
    void schedule(Runnable runnable, long delay, TimeUnit timeUnit);
    void scheduleAtFixedRate(Runnable runnable, long delay,TimeUnit timeUnit);

}

