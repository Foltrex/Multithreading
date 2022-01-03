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

//    private static final int STOCK_EXCHANGE_PARTICIPANTS_CAPACITY = 10;

//    private final Semaphore semaphore = new Semaphore(STOCK_EXCHANGE_PARTICIPANTS_CAPACITY);
    private final Queue<Participant> participants = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Lock queueLock = new ReentrantLock();
    private final Condition notEmpty = queueLock.newCondition();


    private StockExchange() {}


    public void process(Participant participant) throws InterruptedException, TransactionException {
//        semaphore.acquire();
        lock.lock();
        try {
            for (Participant otherParticipant: participants) {
                participant.exchange(otherParticipant);
            }

            participants.add(participant);
            //System.out.println(participants + "\n");

            // time between other deal in stock exchage
            TimeUnit.MILLISECONDS.sleep(800);
        } finally {
            lock.unlock();
//            semaphore.release();
        }
    }

    public static StockExchange getInstance(){
        return instance;
    }
}
