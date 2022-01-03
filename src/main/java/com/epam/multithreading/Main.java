package com.epam.multithreading;

import com.epam.multithreading.entity.Participant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("src/main/resources/participants.json");
            List<Participant> participants = objectMapper.readValue(file, new TypeReference<List<Participant>>() {
            });
            CyclicBarrier cyclicBarrier = new CyclicBarrier(participants.size());
            for (Participant participant : participants) {
                participant.setCyclicBarrier(cyclicBarrier);
            }
            System.out.println("Hello");
            ExecutorService executorService = Executors.newFixedThreadPool(8);
            for (Participant participant: participants) {
                executorService.execute(participant);
            }

            for (Participant participant: participants) {
                System.out.println(participants + "\n");
                TimeUnit.MILLISECONDS.sleep(800);
            }
            executorService.shutdown();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
