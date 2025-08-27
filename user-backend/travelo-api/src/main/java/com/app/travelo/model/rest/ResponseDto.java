package com.app.travelo.model.rest;

import com.app.travelo.model.entity.BookingDetailsEntity;
import lombok.*;

import java.util.List;
import java.util.Objects;
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Data
public class ResponseDto<T> {
    private String errorCode;
    private String errorMessage;
    private String success;
    private T response;


}
