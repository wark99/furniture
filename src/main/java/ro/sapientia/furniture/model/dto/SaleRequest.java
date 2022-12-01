package ro.sapientia.furniture.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record SaleRequest(
		Long id,
	    Long servicePointId,
	    BigDecimal totalPrice,
		Timestamp saledDate
    ) {}