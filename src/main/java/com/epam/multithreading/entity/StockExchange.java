package com.epam.multithreading.entity;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StockExchange {
    private static StockExchange instance;
    private final Queue<Participant> participants = new ArrayDeque<>();
    private static final Lock LOCK = new ReentrantLock();

    private static final int MAX_NUMBER_OF_PARTICIPANTS_TO_EXCHANGE = 4;

    private final Semaphore semaphore = new Semaphore(MAX_NUMBER_OF_PARTICIPANTS_TO_EXCHANGE);
    private final Lock participantsLock = new ReentrantLock();

    private StockExchange() {}

    public static StockExchange getInstance(){
        StockExchange localInstance = instance;
        if (localInstance == null) {
            LOCK.lock();
            localInstance = instance;
            try {
                if (localInstance == null) {
                    localInstance = new StockExchange();
                    instance = localInstance;
                }
            } finally {
                LOCK.unlock();
            }
        }

        return localInstance;
    }

    public void process(Participant newParticipant) throws InterruptedException {
        semaphore.acquire();
        participantsLock.lock();
        try {
            for (Participant participant: participants) {
                newParticipant.exchangeCurrency(participant);
            }

            participants.add(newParticipant);

        } finally {
            participantsLock.unlock();
            semaphore.release();
        }
    }
}
