package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de resposta para leitura de sensor
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingResponse {

    private Long id;
    private String sensorId;
    private Double value;
    private LocalDateTime timestamp;
    private LocalDateTime createdAt;
}
