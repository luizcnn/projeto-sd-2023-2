package com.example.projetosd.entrypoint.vo;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private int status;
    private LocalDateTime timestamp;
    private String errorMsg;
}
