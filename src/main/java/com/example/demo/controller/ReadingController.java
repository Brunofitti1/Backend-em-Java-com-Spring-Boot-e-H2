package com.example.demo.controller;

import com.example.demo.dto.request.CreateReadingRequest;
import com.example.demo.dto.response.ReadingResponse;
import com.example.demo.dto.response.SensorResponse;
import com.example.demo.service.ReadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de leituras de sensores
 * Implementa endpoints para CRUD completo e funcionalidades adicionais
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Readings", description = "API para gerenciamento de leituras de sensores")
public class ReadingController {

    private final ReadingService readingService;

    @Operation(summary = "Lista todos os sensores", description = "Retorna informações consolidadas de todos os sensores com suas últimas leituras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensores listados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/sensores")
    public ResponseEntity<List<SensorResponse>> getSensors() {
        log.info("GET /api/sensores - Buscando todos os sensores");
        List<SensorResponse> sensors = readingService.getSensors();
        return ResponseEntity.ok(sensors);
    }

    @Operation(summary = "Lista todas as leituras", description = "Retorna todas as leituras de todos os sensores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leituras listadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/readings")
    public ResponseEntity<List<ReadingResponse>> getAllReadings() {
        log.info("GET /api/readings - Buscando todas as leituras");
        List<ReadingResponse> readings = readingService.getAllReadings();
        return ResponseEntity.ok(readings);
    }

    @Operation(summary = "Busca leituras por sensor", description = "Retorna todas as leituras de um sensor específico ordenadas por data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leituras encontradas"),
            @ApiResponse(responseCode = "404", description = "Sensor não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/readings/{sensorId}")
    public ResponseEntity<List<ReadingResponse>> getReadingsBySensorId(
            @Parameter(description = "ID do sensor", example = "1") @PathVariable String sensorId) {
        log.info("GET /api/readings/{} - Buscando leituras do sensor", sensorId);
        List<ReadingResponse> readings = readingService.getReadingsBySensorId(sensorId);
        return ResponseEntity.ok(readings);
    }

    @Operation(summary = "Busca leitura por ID", description = "Retorna uma leitura específica pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leitura encontrada"),
            @ApiResponse(responseCode = "404", description = "Leitura não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/readings/id/{id}")
    public ResponseEntity<ReadingResponse> getReadingById(
            @Parameter(description = "ID da leitura", example = "1") @PathVariable Long id) {
        log.info("GET /api/readings/id/{} - Buscando leitura", id);
        ReadingResponse reading = readingService.getReadingById(id);
        return ResponseEntity.ok(reading);
    }

    @Operation(summary = "Cria nova leitura", description = "Registra uma nova leitura de sensor no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Leitura criada com sucesso", content = @Content(schema = @Schema(implementation = ReadingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/readings")
    public ResponseEntity<ReadingResponse> createReading(
            @Valid @RequestBody CreateReadingRequest request) {
        log.info("POST /api/readings - Criando nova leitura para sensor {}", request.getSensorId());
        ReadingResponse reading = readingService.createReading(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reading);
    }

    @Operation(summary = "Gera dados de teste", description = "Gera leituras aleatórias para testes (apenas desenvolvimento)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados gerados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/readings/generate")
    public ResponseEntity<String> generateTestData(
            @Parameter(description = "Quantidade de leituras a gerar", example = "10") @RequestParam(defaultValue = "10") int count) {
        log.info("POST /api/readings/generate - Gerando {} leituras de teste", count);
        String result = readingService.generateTestData(count);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Deleta uma leitura", description = "Remove uma leitura específica do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Leitura deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Leitura não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/readings/{id}")
    public ResponseEntity<Void> deleteReading(
            @Parameter(description = "ID da leitura", example = "1") @PathVariable Long id) {
        log.info("DELETE /api/readings/{} - Deletando leitura", id);
        readingService.deleteReading(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Health Check", description = "Verifica se a API está funcionando")
    @ApiResponse(responseCode = "200", description = "API está funcionando")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("API Festo Sensors está funcionando! 🚀");
    }
}
