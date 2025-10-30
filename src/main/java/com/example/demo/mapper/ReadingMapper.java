package com.example.demo.mapper;

import com.example.demo.dto.request.CreateReadingRequest;
import com.example.demo.dto.response.ReadingResponse;
import com.example.demo.entity.Reading;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Mapper para conversão entre Reading e DTOs
 * Utiliza MapStruct para geração automática de código
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReadingMapper {

    /**
     * Converte CreateReadingRequest para Reading entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Reading toEntity(CreateReadingRequest request);

    /**
     * Converte Reading entity para ReadingResponse
     */
    ReadingResponse toResponse(Reading reading);

    /**
     * Converte lista de Reading entities para lista de ReadingResponse
     */
    List<ReadingResponse> toResponseList(List<Reading> readings);
}
