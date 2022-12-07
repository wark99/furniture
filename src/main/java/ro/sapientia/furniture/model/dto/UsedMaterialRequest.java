package ro.sapientia.furniture.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

@JsonSerialize
@Builder
public record UsedMaterialRequest(
        Long id,
        Long materialId,
        Long furnitureId,
        int quantity,
        BigDecimal price,
        Timestamp timestamp
) {}