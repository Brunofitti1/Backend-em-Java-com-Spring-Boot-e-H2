package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para criação de nova leitura de sensor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReadingRequest {

    @NotBlank(message = "Sensor ID é obrigatório")
    @Size(min = 1, max = 50, message = "Sensor ID deve ter entre 1 e 50 caracteres")
    private String sensorId;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.0", message = "Valor deve ser maior ou igual a 0")
    @DecimalMax(value = "1000.0", message = "Valor deve ser menor ou igual a 1000")
    private Double value;

    private LocalDateTime timestamp;
}
