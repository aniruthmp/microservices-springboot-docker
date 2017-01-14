package com.carnival.venue.job;

import com.carnival.venue.db.domain.Venue;
import com.carnival.venue.db.repository.VenueRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Scheduled(fixedRate = 6000000)
    public void reportCurrentTime() {
        log.info("Data insert job started @ ", new Date());
        Gson gson = new GsonBuilder().create();

        try {
            Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/venue.json"), "UTF-8");
            Venue[] venArray = gson.fromJson(reader, Venue[].class);
            List<Venue> venueList = Arrays.asList(venArray);

            if(Objects.nonNull(venueList)){
                venueList.forEach(venue -> venueRepository.save(venue));
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
}
