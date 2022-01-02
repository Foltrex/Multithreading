package com.epam.multithreading;

import com.epam.multithreading.entity.Participant;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StockExchange {
    private static final StockExchange instance = new StockExchange();
    private Queue<Participant> participants = new LinkedList<>();
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();

    private StockExchange() {}


    private void put(Participant participant) {
        lock.lock();
        try {
            participants.add(participant);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public void process(Participant participant) {

    }

    private Participant take() throws InterruptedException {
        lock.lock();
        try {
            while (participants.isEmpty()) {
                notEmpty.await();
            }
            return participants.remove();
        } finally {
            lock.unlock();
        }
    }

    public static StockExchange getInstance(){
        return instance;
    }
}
