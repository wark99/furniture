package ro.sapientia.furniture.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record SaledItemRequest(
		Long id,
	    Long saleId,
	    Long furnitureId,
	    int quantity,
	    BigDecimal price,
		Timestamp timestamp
    ) {}