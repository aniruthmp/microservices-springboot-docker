package com.example.reservation.job;

import com.example.reservation.db.domain.Reservation;
import com.example.reservation.db.repository.ReservationRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
@Profile({"local", "docker"})
public class ScheduledJob {
    @Autowired
    ReservationRepository reservationRepository;

    @Scheduled(fixedRate = 6000000)
    public void reportCurrentTime() {
        log.info("Data insert job started @ ", new Date());
        Gson gson = new GsonBuilder().create();

        try {
            Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/people.json"), "UTF-8");
            Reservation[] resArray = gson.fromJson(reader, Reservation[].class);
            List<Reservation> reservationList = Arrays.asList(resArray);

            if(Objects.nonNull(reservationList)){
                reservationList.forEach(reservation -> reservationRepository.save(reservation));
                long recordsCount = reservationRepository.count();
                log.info("Total records inserted : " + recordsCount);
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
}
