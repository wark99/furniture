package ro.sapientia.furniture.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record SaleRequest(
		Long id,
	    Long servicePointId,
	    BigDecimal totalPrice,
		Timestamp saledDate
    ) {}