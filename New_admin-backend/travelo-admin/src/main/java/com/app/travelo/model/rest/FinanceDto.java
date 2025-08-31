package com.app.travelo.model.rest;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class FinanceDto {

    private Long id;

    private String country;

    private String bankName;

    private String swiftCode;

    private String ifsc;

    private String accountNumber;

    private String accountHolderName;

    private String registeredForGst;

    private String tradeName;

    private String gstIn;

    private Long hotelCode;

    private String pan;
}
