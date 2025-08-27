package com.app.travelo.model.rest;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class GuestDetails {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
