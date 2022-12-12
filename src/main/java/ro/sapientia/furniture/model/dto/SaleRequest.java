package ro.sapientia.furniture.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonSerialize
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequest {
	private Long id;
	private Long servicePointId;
	private BigDecimal totalPrice;
	private Timestamp saledDate;

	public Long id() {
		return id;
	}

	public Long servicePointId() {
		return servicePointId;
	}

	public BigDecimal totalPrice() {
		return totalPrice;
	}

	public Timestamp saledDate() {
		return saledDate;
	}
}