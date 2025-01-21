package com.nttdata.bootcoin.model.response;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String error;
    private String message;
}
