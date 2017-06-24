package com.example.composite.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Created by Aniruth Parthasarathy
 */
@Data
@ToString
public class Venue {
    @ApiModelProperty(example = "Wunsch and Sons", required = true)
    private String venueName;
    @ApiModelProperty(example = "true", required = false)
    private boolean isAvailable;

    @ApiModelProperty(example = "47", required = true)
    private long reservationId;
    @ApiModelProperty(example = "2004-06-20 17:51:58", required = true)
    private String bookingDate;
    private String personName;
}
