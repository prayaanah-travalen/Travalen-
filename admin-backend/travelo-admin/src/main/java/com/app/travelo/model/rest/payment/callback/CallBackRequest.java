package com.app.travelo.model.rest.payment.callback;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class CallBackRequest {
    private String response;
}
