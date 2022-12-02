package ro.sapientia.furniture.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record UsedMaterialRequest(
        Long id,
        Long materialId,
        Long furnitureId,
        int quantity,
        BigDecimal price,
        Timestamp timestamp
) {}