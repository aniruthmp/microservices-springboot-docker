package com.example.reservation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home redirection to swagger api documentation
 */
@Controller
@Api(value = "/", tags = "Swagger")
public class HomeController {

    @GetMapping(value = "/")
    @ApiOperation(
            value = "Swagger Docs",
            notes = "Swagger Docs",
            response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Venues Not Found")})
    public String index() {
        return "redirect:swagger-ui.html";
    }
}
