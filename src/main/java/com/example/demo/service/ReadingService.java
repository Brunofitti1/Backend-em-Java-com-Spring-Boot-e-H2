package com.example.demo.service;

import com.example.demo.dto.request.CreateReadingRequest;
import com.example.demo.dto.response.ReadingResponse;
import com.example.demo.dto.response.SensorResponse;
import com.example.demo.entity.Reading;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ReadingMapper;
import com.example.demo.repository.ReadingRepository;
import com.example.demo.tools.SensorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service para lógica de negócio de leituras de sensores
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReadingService {

    private final ReadingRepository readingRepository;
    private final ReadingMapper readingMapper;

    /**
     * Busca todas as leituras
     */
    @Transactional(readOnly = true)
    public List<ReadingResponse> getAllReadings() {
        log.debug("Buscando todas as leituras");
        List<Reading> readings = readingRepository.findAll();
        return readingMapper.toResponseList(readings);
    }

    /**
     * Busca leituras por sensor ID
     */
    @Transactional(readOnly = true)
    public List<ReadingResponse> getReadingsBySensorId(String sensorId) {
        log.debug("Buscando leituras do sensor: {}", sensorId);
        List<Reading> readings = readingRepository.findBySensorIdOrderByTimestampDesc(sensorId);

        if (readings.isEmpty()) {
            log.warn("Nenhuma leitura encontrada para o sensor: {}", sensorId);
        }

        return readingMapper.toResponseList(readings);
    }

    /**
     * Busca leitura por ID
     */
    @Transactional(readOnly = true)
    public ReadingResponse getReadingById(Long id) {
        log.debug("Buscando leitura com ID: {}", id);
        Reading reading = readingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reading", "id", id));
        return readingMapper.toResponse(reading);
    }

    /**
     * Cria nova leitura
     */
    @Transactional
    public ReadingResponse createReading(CreateReadingRequest request) {
        log.debug("Criando nova leitura para sensor: {}", request.getSensorId());

        Reading reading = readingMapper.toEntity(request);

        // Se o timestamp não foi fornecido, usa a data/hora atual
        if (reading.getTimestamp() == null) {
            reading.setTimestamp(LocalDateTime.now());
        }

        Reading savedReading = readingRepository.save(reading);
        log.info("Leitura criada com sucesso. ID: {}", savedReading.getId());

        return readingMapper.toResponse(savedReading);
    }

    /**
     * Gera dados de teste
     */
    @Transactional
    public String generateTestData(int count) {
        log.info("Gerando {} leituras de teste", count);

        String[] sensorIds = { "1", "2", "3", "4", "5", "6", "7", "8" };
        java.util.Random random = new java.util.Random();
        int generated = 0;

        for (int i = 0; i < count; i++) {
            String sensorId = sensorIds[random.nextInt(sensorIds.length)];
            double value = 20 + (80 * random.nextDouble());
            LocalDateTime timestamp = LocalDateTime.now().minusHours(random.nextInt(24));

            Reading reading = new Reading();
            reading.setSensorId(sensorId);
            reading.setValue(value);
            reading.setTimestamp(timestamp);

            readingRepository.save(reading);
            generated++;
        }

        log.info("{} leituras de teste geradas com sucesso", generated);
        return String.format("Gerados %d novos registros de teste", generated);
    }

    /**
     * Busca informações consolidadas dos sensores (compatível com frontend)
     */
    @Transactional(readOnly = true)
    public List<SensorResponse> getSensors() {
        log.debug("Buscando informações consolidadas dos sensores");

        List<Reading> readings = readingRepository.findAll();

        // Agrupa leituras por sensor
        Map<String, List<Reading>> readingsBySensor = readings.stream()
                .collect(Collectors.groupingBy(Reading::getSensorId));

        // Cria resposta para cada sensor
        return readingsBySensor.entrySet().stream()
                .map(entry -> {
                    String sensorId = entry.getKey();
                    List<Reading> sensorReadings = entry.getValue();

                    // Pega a leitura mais recente
                    Reading latest = sensorReadings.stream()
                            .max((r1, r2) -> r1.getTimestamp().compareTo(r2.getTimestamp()))
                            .orElse(sensorReadings.get(0));

                    // Determina status baseado no valor
                    String status = SensorUtils.determineStatus(latest.getValue());

                    return SensorResponse.builder()
                            .id(sensorId)
                            .nome(SensorUtils.getSensorName(sensorId))
                            .status(status)
                            .valor(latest.getValue())
                            .tipo(SensorUtils.getSensorType(sensorId))
                            .unidade(SensorUtils.getSensorUnit(sensorId))
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Deleta uma leitura
     */
    @Transactional
    public void deleteReading(Long id) {
        log.debug("Deletando leitura com ID: {}", id);

        if (!readingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reading", "id", id);
        }

        readingRepository.deleteById(id);
        log.info("Leitura deletada com sucesso. ID: {}", id);
    }
}
