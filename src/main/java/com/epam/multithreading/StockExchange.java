package com.epam.multithreading;

import com.epam.multithreading.entity.Participant;
import com.epam.multithreading.exception.TransactionException;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StockExchange {
    private static final StockExchange instance = new StockExchange();

    private static final int STOCK_EXCHANGE_PARTICIPANTS_CAPACITY = 10;

    private final Semaphore semaphore = new Semaphore(STOCK_EXCHANGE_PARTICIPANTS_CAPACITY);
    private final Queue<Participant> participants = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Lock queueLock = new ReentrantLock();
    private final Condition notEmpty = queueLock.newCondition();


    private StockExchange() {}


    public void process(Participant participant) throws InterruptedException, TransactionException {
        semaphore.acquire();
        lock.lock();
        try {
            if (!isEmpty()) {
                for (Participant otherParticipant: participants) {
                    participant.exchange(otherParticipant);
                }

                TimeUnit.MILLISECONDS.sleep(100);
            }

            put(participant);
        } finally {
            lock.unlock();
            semaphore.release();
        }
    }


    private void put(Participant participant) throws InterruptedException {
        queueLock.lock();
        try {
            participants.add(participant);
            notEmpty.signal();
        } finally {
            queueLock.unlock();
        }
    }

    private Participant take() throws InterruptedException {
        queueLock.lock();
        try {
            while (participants.isEmpty()) {
                notEmpty.await();
            }

            return participants.remove();
        } finally {
            queueLock.unlock();
        }
    }

    private int size() {
        queueLock.lock();
        try {
            return participants.size();
        } finally {
            queueLock.unlock();
        }
    }

    private boolean isEmpty() {
        queueLock.lock();
        try {
            return participants.isEmpty();
        } finally {
            queueLock.unlock();
        }
    }

    private Participant element() throws InterruptedException {
        queueLock.lock();
        try {
            while (participants.isEmpty()) {
                notEmpty.await();
            }

            return participants.element();
        } finally {
            queueLock.unlock();
        }
    }

    public static StockExchange getInstance(){
        return instance;
    }
}
