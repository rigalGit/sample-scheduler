package com.example.scheduler;

public interface MFuture<T> {
    T get() throws InterruptedException;
    boolean isDone();
}
