package com.example.reservation.db.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by a.c.parthasarathy
 */
@Entity
@Data
@EqualsAndHashCode
@Audited
@EntityListeners(AuditingEntityListener.class)
public class Reservation implements Serializable {
    @Id
    @ApiModelProperty(example = "7", required = true)
    private Long id;

    @NotNull
    @ApiModelProperty(example = "Patrick", required = true)
    private String firstName;

    @NotNull
    @ApiModelProperty(example = "Adams", required = true)
    private String lastName;

    @ApiModelProperty(example = "padams6@t.co")
    private String email;

    @NotNull
    @ApiModelProperty(example = "Male", required = true)
    private String gender;

    @NotNull
    @ApiModelProperty(example = "Y", required = true)
    private String isActive = "Y";

    @NotAudited
    @CreatedDate
    private long createdDate;

    @LastModifiedDate
    private long modifiedDate;

    @NotAudited
    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;
}
