package com.example.venue.job;

import com.example.venue.db.domain.Venue;
import com.example.venue.db.repository.VenueRepository;
import com.example.venue.util.UuidGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by a.c.parthasarathy
 */
@Component
@Slf4j
public class ScheduledJob {
    @Autowired
    VenueRepository venueRepository;

    @Autowired
    UuidGenerator uuidGenerator;

    private final String SHIPWRIGHT = "SHIPWRIGHT";

    @Scheduled(fixedRate = 6000000)
    public void populateVenues() {
        log.info("Data insert job started @ " + new Date());
        Gson gson = new GsonBuilder().create();

        try {
            venueRepository.deleteAll();
        } catch (InvalidDataAccessResourceUsageException dataEx) {
            log.error(dataEx.getMessage());
        }

        try {

            Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/venue.json"), "UTF-8");
            Venue[] venArray = gson.fromJson(reader, Venue[].class);
            List<Venue> venueList = Arrays.asList(venArray);

            log.info("Total records from the venue.json file are " + venueList.size());
            if (Objects.nonNull(venueList)) {
                venueList.parallelStream().forEach(venue -> {
                    venue.setDocId(SHIPWRIGHT + ":" + venue.getDocType() + ":" + uuidGenerator.generateUuid());
                    //Set isActive to true
                    venue.setActive(true);
                    venueRepository.save(venue);
                });
            }
            log.info("Data insert job ended @ " + new Date());
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
    }
}
