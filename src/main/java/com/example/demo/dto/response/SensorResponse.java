package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para sensor (compatível com frontend)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorResponse {

    private String id;
    private String nome;
    private String status; // "ok", "aviso", "alerta"
    private Double valor;
    private String tipo;
    private String unidade;
}
