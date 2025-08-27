package com.app.travelo.model.entity;

import com.app.travelo.util.CommonUtil;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass

public class BaseEntity {
    @Column(name="registration_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDateTime;

    @Column(name="registrant_by", updatable = false)
    private String registrantBy;

    @Column(name="last_updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedDateTime;

    @Column(name="last_updated_by")
    private String lastUpdatedBy;


}


