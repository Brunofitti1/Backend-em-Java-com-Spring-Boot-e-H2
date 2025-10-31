package com.example.demo.config;

import com.example.demo.entity.Reading;
import com.example.demo.repository.ReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Inicializador de dados para desenvolvimento
 * Popula o banco com leituras de exemplo
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(ReadingRepository repository) {
        return args -> {
            try {
                // Verifica se já existem dados
                long count = repository.count();
                if (count > 0) {
                    log.info("Banco de dados já contém {} leituras. Pulando inicialização.", count);
                    return;
                }

                log.info("Inicializando banco de dados com dados de exemplo...");

                String[] sensorIds = { "1", "2", "3", "4", "5", "6", "7", "8" };
                Random random = new Random();
                int totalReadings = 0;

                // Gera 10 leituras para cada sensor nas últimas 24 horas
                for (String sensorId : sensorIds) {
                    for (int i = 0; i < 10; i++) {
                        double value = 20 + (80 * random.nextDouble());
                        LocalDateTime timestamp = LocalDateTime.now().minusHours(24 - i * 2);

                        Reading reading = new Reading();
                        reading.setSensorId(sensorId);
                        reading.setValue(value);
                        reading.setTimestamp(timestamp);

                        repository.save(reading);
                        totalReadings++;
                    }
                }

                log.info("✅ Dados de exemplo criados com sucesso! Total: {} leituras", totalReadings);

            } catch (Exception e) {
                log.error("❌ Erro ao inicializar dados: {}", e.getMessage());
            }
        };
    }
}
