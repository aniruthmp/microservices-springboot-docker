package com.example.reservation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by a.c.parthasarathy
 */
@RefreshScope
@RestController
@Slf4j
@Api(value = "/message", tags = "Message", description = "API to display run-time config change")
public class MessageRestController {

    @Value("${message}")
    private String message;

    @GetMapping(value = "/message", produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiOperation(
            value = "Display value of message property",
            notes = "This API is used to demonstrate how config values can be changed at run-time " +
                    "without the need to restart the application (using Spring's RefreshScope)",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = String.class),
            @ApiResponse(code = 400, message = "failed operation", response = String.class) })
    public ResponseEntity<String> message() {
        log.info("Ani came inside message method");
        return new ResponseEntity<String>(this.message, HttpStatus.OK);
    }
}