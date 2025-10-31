package com.example.demo.repository;

import com.example.demo.entity.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para operações de banco de dados com Reading
 */
@Repository
public interface ReadingRepository extends JpaRepository<Reading, Long> {

    /**
     * Busca todas as leituras de um sensor específico
     */
    List<Reading> findBySensorIdOrderByTimestampDesc(String sensorId);

    /**
     * Busca leituras de um sensor em um período específico
     */
    @Query("SELECT r FROM Reading r WHERE r.sensorId = :sensorId " +
            "AND r.timestamp BETWEEN :startDate AND :endDate " +
            "ORDER BY r.timestamp DESC")
    List<Reading> findBySensorIdAndDateRange(
            @Param("sensorId") String sensorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Busca a leitura mais recente de um sensor
     */
    Reading findTopBySensorIdOrderByTimestampDesc(String sensorId);

    /**
     * Busca todos os sensor IDs distintos
     */
    @Query("SELECT DISTINCT r.sensorId FROM Reading r ORDER BY r.sensorId")
    List<String> findAllDistinctSensorIds();

    /**
     * Conta o número de leituras por sensor
     */
    Long countBySensorId(String sensorId);
}
