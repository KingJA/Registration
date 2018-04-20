package com.tdr.registration.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Linus_Xie on 2016/12/16.
 */

public class PoolManager {
    private ExecutorService cachedThreadPool;
    private static PoolManager mPoolManager;

    private PoolManager() {
        cachedThreadPool = Executors.newCachedThreadPool();
    }

    public static PoolManager getInstance() {
        if (mPoolManager == null) {
            synchronized (PoolManager.class) {
                if (mPoolManager == null) {
                    mPoolManager = new PoolManager();
                }
            }
        }
        return mPoolManager;
    }

    public void execute(Runnable r) {
        cachedThreadPool.execute(r);
    }

    public boolean isOver() {
        return cachedThreadPool.isTerminated();
    }
}
