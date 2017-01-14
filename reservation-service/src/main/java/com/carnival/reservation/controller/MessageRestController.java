package com.carnival.reservation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by a.c.parthasarathy
 */
@RefreshScope
@RestController
@Slf4j
public class MessageRestController {

    @Value("${message}")
    private String message;

    @GetMapping("/message")
    String message() {
        log.info("Ani came inside message method");
        return this.message;
    }
}
