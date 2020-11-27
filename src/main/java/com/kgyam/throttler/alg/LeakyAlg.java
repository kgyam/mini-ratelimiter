package com.kgyam.throttler.alg;

import java.util.concurrent.atomic.AtomicInteger;

public class LeakyAlg implements LimitAlg {
    private int capacity;
    /*
    单位时间流出得水滴
     */
    private int rate;
    private AtomicInteger bucket = new AtomicInteger(0);
    private long time;

    public LeakyAlg(int capacity, int rate) {
        this.capacity = capacity;
        this.rate = rate;
        time = System.currentTimeMillis();
    }

    /**
     * @return
     */
    public boolean tryAcquire() {
        if (capacity <= 0) {
            return true;
        }

        //计算bucket里面剩余水滴数
        long _current = System.currentTimeMillis();
        int _left = bucket.get() - (int) ((_current - time) / 1000 * rate);
        bucket.set(Math.max(0, _left));
        time = _current;
        if (bucket.get() < capacity) {
            bucket.incrementAndGet();
            return true;
        } else {
            return false;
        }
    }

}
