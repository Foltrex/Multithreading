package com.epam.multithreading;

import com.epam.multithreading.entity.Participant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private static final String PARTICIPANTS_PATH = "src/main/resources/participants.json";

    public static void main(String[] args) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Participant> participants = objectMapper.readValue(new File(PARTICIPANTS_PATH),
                    new TypeReference<List<Participant>>() {});

            ExecutorService executorService = Executors.newFixedThreadPool(participants.size());
            for (Participant participant : participants) {
                executorService.execute(participant);
            }

            for (int i = 0; i < participants.size(); ++i) {
                System.out.println("\n" + participants + "\n");
                TimeUnit.MILLISECONDS.sleep(800);
            }


            executorService.shutdown();

        } catch (IOException | InterruptedException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

}
