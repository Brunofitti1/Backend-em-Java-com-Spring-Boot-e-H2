package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ReadingRepository repository;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Aguarda um pouco para garantir que a tabela foi criada
            Thread.sleep(1000);
            
            // Só adiciona dados se o banco estiver vazio
            if (repository.count() == 0) {
                Random random = new Random();
                
                // Criar leituras para cada sensor
                for (int sensorId = 1; sensorId <= 8; sensorId++) {
                    for (int i = 0; i < 10; i++) {
                        Reading reading = new Reading();
                        reading.setSensorId(String.valueOf(sensorId));
                        reading.setValue(20.0 + random.nextDouble() * 60); // valores entre 20 e 80
                        reading.setTimestamp(LocalDateTime.now().minusHours(10 - i));
                        repository.save(reading);
                    }
                }
                
                System.out.println("Dados de exemplo criados no banco H2!");
            } else {
                System.out.println("Dados já existem no banco H2.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao inicializar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}