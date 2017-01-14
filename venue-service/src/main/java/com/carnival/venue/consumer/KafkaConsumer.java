package com.carnival.venue.consumer;

import com.carnival.venue.controller.service.VenueService;
import com.carnival.venue.model.ConsumerModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Created by a.c.parthasarathy on 10/25/16.
 */
@Slf4j
@Component
public class KafkaConsumer {

    @Autowired
    VenueService venueService;

    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(id = "${spring.cloud.stream.bindings.input.group}",
            topics = "${spring.cloud.stream.bindings.input.destination}")
    public void onMessage(String message) {
        log.info("received message='{}'", message);

        //Get Object from Json
        Gson gson = new GsonBuilder().create();
        ConsumerModel consumerModel = gson.fromJson(message, ConsumerModel.class);
        venueService.updateByReservationId(consumerModel.getId(), consumerModel.getReservationName());

        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}