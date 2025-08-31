package com.app.travelo.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "finance")
public class FinanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="country")
    private String country;

    @Column(name="bank_name")
    private String bankName;

    @Column(name="swift_code")
    private String swiftCode;

    @Column(name="ifsc")
    private String ifsc;

    @Column(name="account_number")
    private String accountNumber;

    @Column(name="account_holder_name")
    private String accountHolderName;

    @Column(name="registered_for_gst")
    private String registeredForGst;

    @Column(name="trade_name")
    private String tradeName;

    @Column(name="gst_in")
    private String gstIn;

    @Column(name="pan")
    private String pan;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hotel_code", referencedColumnName = "hotel_code")
    private HotelEntity hotel;
}
