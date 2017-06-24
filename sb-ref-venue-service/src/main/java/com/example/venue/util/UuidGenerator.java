package com.example.venue.util;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by Aniruth Parthasarathy
 */
@Component
public class UuidGenerator {
    /**
     * Method to generate UUID
     * @return
     */
    public String generateUuid(){
        TimeBasedGenerator gen = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
        UUID uuid = gen.generate();
        return uuid.toString();
    }
}
