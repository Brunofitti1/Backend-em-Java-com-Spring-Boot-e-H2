package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ReadingController {

    @Autowired
    private ReadingRepository repository;

    // Endpoint para compatibilidade com frontend - lista sensores baseado nas leituras
    @GetMapping("/sensores")
    public List<Map<String, Object>> getSensores() {
        List<Reading> readings = repository.findAll();
        return readings.stream()
            .collect(Collectors.groupingBy(Reading::getSensorId))
            .entrySet().stream()
            .map(entry -> {
                String sensorId = entry.getKey();
                List<Reading> sensorReadings = entry.getValue();
                Reading latest = sensorReadings.get(sensorReadings.size() - 1);
                
                String status = "ok";
                if (latest.getValue() > 80) status = "alerta";
                else if (latest.getValue() > 60) status = "aviso";
                
                Map<String, Object> sensor = new java.util.HashMap<>();
                sensor.put("id", sensorId);
                sensor.put("nome", getSensorName(sensorId));
                sensor.put("status", status);
                sensor.put("valor", latest.getValue());
                return sensor;
            })
            .collect(Collectors.toList());
    }

    @GetMapping("/readings")
    public List<Reading> getAllReadings() {
        return repository.findAll();
    }

    @GetMapping("/readings/{sensorId}")
    public List<Reading> getBySensorId(@PathVariable String sensorId) {
        return repository.findBySensorId(sensorId);
    }

    @PostMapping("/readings")
    public Reading createReading(@RequestBody Reading reading) {
        if (reading.getTimestamp() == null) {
            reading.setTimestamp(LocalDateTime.now());
        }
        return repository.save(reading);
    }
    
    @PostMapping("/readings/generate")
    public String generateTestData(@RequestParam(defaultValue = "10") int count) {
        String[] sensorIds = {"1", "2", "3", "4", "5", "6", "7", "8"};
        java.util.Random random = new java.util.Random();
        int generated = 0;
        
        for (int i = 0; i < count; i++) {
            String sensorId = sensorIds[random.nextInt(sensorIds.length)];
            double value = 20 + (80 * random.nextDouble()); // Valores entre 20 e 100
            LocalDateTime timestamp = LocalDateTime.now().minusHours(random.nextInt(24));
            
            Reading reading = new Reading();
            reading.setSensorId(sensorId);
            reading.setValue(value);
            reading.setTimestamp(timestamp);
            
            repository.save(reading);
            generated++;
        }
        
        return "Gerados " + generated + " novos registros de teste";
    }
    
    private String getSensorName(String sensorId) {
        return switch (sensorId) {
            case "1" -> "Reed Switch";
            case "2" -> "Pressão Absoluta";
            case "3" -> "Pressão Diferencial";
            case "4" -> "Acelerômetro";
            case "5" -> "Temperatura";
            case "6" -> "Strain Gauge";
            case "7" -> "Contador de Ciclos";
            case "8" -> "Qualidade do Ar";
            default -> "Sensor " + sensorId;
        };
    }
}
