package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Aplicação Spring Boot - Festo Sensors Monitoring System
 * Sprint 4 - Escalabilidade e Experiência Completa
 * 
 * Funcionalidades:
 * - Persistência em PostgreSQL
 * - Autenticação JWT com Microsoft Entra ID
 * - API REST completa com Swagger
 * - Arquitetura em camadas (Controller -> Service -> Repository)
 */
@Slf4j
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
public class DemoApplication {

	public static void main(String[] args) {
		log.info("🚀 Iniciando Festo Sensors Monitoring API - Sprint 4");
		SpringApplication.run(DemoApplication.class, args);
		log.info("✅ Aplicação iniciada com sucesso!");
		log.info("📚 Documentação disponível em: http://localhost:8080/swagger-ui.html");
		log.info("🔍 API Docs JSON em: http://localhost:8080/api-docs");
	}
}
