package com.carnival.venue.db.domain;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import lombok.Data;
import org.springframework.data.couchbase.core.mapping.Document;

import java.io.Serializable;


/**
 * Created by a.c.parthasarathy
 */
@Data
@Document(expiry = 0)
public class Venue implements Serializable {
	private static final long serialVersionUID = -3771132905521308681L;

	@Id
    private Long id;

    @Field
    private String venueName;
    @Field
    private boolean isAvailable;
    @Field
    private Long reservationId;
    @Field
    private String bookingDate;

    @Field
    private String personName;

}
