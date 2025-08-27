package com.app.travelo.model.rest.payment.callback;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class CallBackResponse {
    public boolean success;
    public String code;
    public String message;
    public Data data;
}
