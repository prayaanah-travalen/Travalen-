package com.app.travelo.model.entity;

import com.app.travelo.util.CommonUtil;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
//@Builder
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

//    public BaseEntity() {
//        this.lastUpdatedBy = CommonUtil.getUserId().toString();
//        this.lastUpdatedDateTime = new Date();
//        this.registrantBy =  CommonUtil.getUserId().toString();
//        this.registrationDateTime =  new Date();
//    }
}
