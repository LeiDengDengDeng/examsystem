package com.exam.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by deng on 2017/10/21.
 */
public class ThreadPool {
    private static ExecutorService cachedThreadPool;

    static {
        cachedThreadPool = Executors.newCachedThreadPool();
    }

    public static void execute(Runnable command) {
        cachedThreadPool.execute(command);
    }
}
