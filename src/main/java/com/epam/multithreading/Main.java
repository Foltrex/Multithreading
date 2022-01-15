package com.epam.multithreading;

import com.epam.multithreading.entity.Participant;
import com.epam.multithreading.entity.Participants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private static final String PARTICIPANTS_PATH = "src/main/resources/participants.json";

    public static void main(String[] args) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        Participants participantsWrapper = objectMapper.readValue(new File(PARTICIPANTS_PATH), Participants.class);
        List<Participant> participants = participantsWrapper.getParticipants();

        ExecutorService executorService = Executors.newFixedThreadPool(participants.size());
        for (Participant participant : participants) {
            executorService.execute(participant);
        }

        LOGGER.info(participants);

        executorService.shutdown();
    }
}
