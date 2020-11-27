package com.kgyam.throttler.alg;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 滑动窗口算法
 * <p>
 * <p>
 * 存在问题：
 * <p>
 * 1.精度问题：假设将1min切割成5个子窗口，每个子窗口20s，限制请求数在100r。
 * 在0:15-0:20的时候，打入100r，后面的的请求就会被掐断。
 * 关键是在当进入到1:15过后的时候，瞬间再次打入100r，这些请求是能够打入系统的。
 * 时间维度在0:15~1:15，其实非常有可能存在系统实际接收了200个request的情况。
 * 这种情况需要把子窗口切得更细可能才能避免这种问题，同时切割过细会占用更多的内存
 * 这里需要coder做一个trade-off
 * <p>
 *
 * 2.流量无法平滑接入：如果大量request在短时间内打入系统，后面的请求就会被掐断。
 * 时间窗口如果设置过大，这种问题会更加明显。限流框架的作用其实是为了让流量更加平滑
 * 接入系统防止大流量冲垮服务，而并非真的要掐断多少请求。
 *
 */
public class SlidingAlg implements LimitAlg {

    /*
    锁超时时间
     */
    private static final long TRY_LOCK_TIMEOUT = 200L;

    /*
    时间窗口指针
     */
    private Node pointer;

    /*
    时间窗口个数
     */
    private static final int SLOT = 5;

    /*

     */
    private int interval;
    /*
    限流值
     */
    private final int limit;

    private long slotTime;

    private final Lock lock;

    public SlidingAlg(int limit, int interval) {
        this.limit = limit;
        this.interval = interval;
        this.lock = new ReentrantLock();
        init();
    }


    /**
     * 时间窗口使用Node形成一个环形结构，每个节点就是滑动的最小单位
     */
    private void init() {
        if (limit <= 0) {
            return;
        }

        Node current = null;
        /*
        初始化时间窗口节点
         */
        for (int i = 0; i < SLOT; i++) {
            Node node = new Node();
            if (pointer == null) {
                pointer = node;
                current = pointer;
            } else {
                pointer.next = node;
                pointer = node;
            }
        }
        //tail 指向 head，形成一个环形结构
        pointer.next = current;
        //计算每个时间窗口的时间单位
        slotTime = interval / SLOT;
    }

    /**
     * 首先判断是否需要滑动，以及具体滑动多少个节点
     *
     * @return
     */
    public boolean tryAcquire() {
        if (limit <= 0) {
            return true;
        }
        try {
            if (lock.tryLock(TRY_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
                sliding();
                int count = getCount();
                if (count > limit) {
                    return false;
                } else {
                    pointer.getCounter().incrementAndGet();
                    return true;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return true;
    }


    private int getCount() {
        int sum = 0;
        Node node = pointer;
        for (int i = 0; i < SLOT; i++) {
            sum += node.getCounter().get();
            node = node.next;
        }
        return sum;
    }


    /**
     * 窗口滑动
     */
    private void sliding() {
        long currentTime = System.currentTimeMillis();
        // 指针指向窗口节点的时间
        long pt = pointer.getTime();
        //计算当前时间与指针指向节点的窗口时间相差值
        long count = (currentTime - pt) / slotTime;
        if (count > SLOT) {
            count = SLOT;
        }
        sliding(count, currentTime);
    }


    private void sliding(long count, long currentTime) {
        if (count <= 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            pointer = pointer.next;
            pointer.setTime(currentTime);
            pointer.getCounter().set(0);
        }
    }


    /**
     * 时间窗口节点
     */
    public class Node {
        /*
        当前窗口的时间
         */
        private long time;
        /*
        当前窗口的计数器
         */
        private AtomicInteger counter;
        /*
        下一个窗口节点
         */
        private Node next;

        public Node() {
            time = System.currentTimeMillis();
            counter = new AtomicInteger(0);
        }


        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public AtomicInteger getCounter() {
            return counter;
        }

        public void setCounter(AtomicInteger counter) {
            this.counter = counter;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

}
