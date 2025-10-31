package com.example.demo.tools;

/**
 * Classe utilitária para operações relacionadas a sensores
 */
public class SensorUtils {

    /**
     * Retorna o nome do sensor baseado no ID
     */
    public static String getSensorName(String sensorId) {
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

    /**
     * Retorna o tipo do sensor baseado no ID
     */
    public static String getSensorType(String sensorId) {
        return switch (sensorId) {
            case "1", "7" -> "Digital";
            case "2", "3", "4", "5", "6", "8" -> "Analógico";
            default -> "Desconhecido";
        };
    }

    /**
     * Retorna a unidade de medida do sensor baseado no ID
     */
    public static String getSensorUnit(String sensorId) {
        return switch (sensorId) {
            case "1", "7" -> "ciclos";
            case "2", "3" -> "bar";
            case "4" -> "g";
            case "5" -> "°C";
            case "6" -> "με";
            case "8" -> "ppm";
            default -> "";
        };
    }

    /**
     * Determina o status do sensor baseado no valor da leitura
     * 
     * @param value Valor da leitura
     * @return Status: "ok", "aviso" ou "alerta"
     */
    public static String determineStatus(Double value) {
        if (value == null) {
            return "desconhecido";
        }

        if (value > 80) {
            return "alerta";
        } else if (value > 60) {
            return "aviso";
        } else {
            return "ok";
        }
    }

    /**
     * Valida se um sensor ID é válido
     */
    public static boolean isValidSensorId(String sensorId) {
        if (sensorId == null || sensorId.trim().isEmpty()) {
            return false;
        }

        try {
            int id = Integer.parseInt(sensorId);
            return id >= 1 && id <= 8;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
