package com.kgyam.ratelimiter.alg;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 滑动窗口
 */
public class SlidingAlg implements LimitAlg {

    private static final long TRY_LOCK_TIMEOUT = 200L;  // 200ms.
    private Node pointer;
    private static final int SLOT = 5;
    private long span;
    private final int limit;
    private long slotTime;
    private ReentrantLock lock = new ReentrantLock();

    public SlidingAlg(int limit, long span) {
        this.limit = limit;
        this.span = span;
        init();
    }


    private void init() {
        if (limit <= 0) {
            return;
        }

        Node _current = null;
        for (int i = 0; i < SLOT; i++) {
            Node _n = new Node();
            if (pointer == null) {
                pointer = _n;
                _current = pointer;
            } else {
                pointer.next = _n;
                pointer = _n;
            }
        }
        pointer.next = _current;
        slotTime = span / SLOT;
    }

    public boolean tryAcquire() {
        if (limit <= 0) {
            return true;
        }
        try {
            if (lock.tryLock(TRY_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
                sliding();
                long _count = getCount();
                if (_count > limit) {
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


    private long getCount() {
        long _sum = 0L;
        Node _n = pointer;
        for (int i = 0; i < SLOT; i++) {
            _sum += _n.getCounter().get();
            _n = _n.next;
        }
        return _sum;
    }


    private void sliding() {
        long _currentTime = System.currentTimeMillis();
        long _pt = pointer.getTime();
        long _count = (_currentTime - _pt) / slotTime;
        if (_count > SLOT) {
            _count = SLOT;
        }
        sliding(_count, _currentTime);
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


    public class Node {
        private long time;
        private AtomicInteger counter;
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
