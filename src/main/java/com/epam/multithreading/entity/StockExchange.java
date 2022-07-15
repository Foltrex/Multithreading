package com.epam.multithreading.entity;

import com.epam.multithreading.CurrencyExchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StockExchange {
    private static StockExchange instance;
    private final List<Participant> participants = new ArrayList<>();
    private static final Lock LOCK = new ReentrantLock();
    private final CurrencyExchanger exchangeCurrency = new CurrencyExchanger();

    private static final int STOCK_EXCHANGE_PARTICIPANTS_CAPACITY = 4;

    private final Semaphore semaphore = new Semaphore(STOCK_EXCHANGE_PARTICIPANTS_CAPACITY);
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

    public void register(Participant newParticipant) throws InterruptedException {
        try {
            semaphore.acquire();
            participantsLock.lock();

            for (Participant participant: participants) {
                exchangeCurrency.exchangeCurrency(newParticipant, participant);
            }

            participants.add(newParticipant);

        } finally {
            participantsLock.unlock();
            semaphore.release();
        }
    }
}
