package ro.sapientia.furniture.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaledItemRequest {
	private Long id;
	private Long saleId;
	private Long furnitureId;
	private int quantity;
	private BigDecimal price;
	private Timestamp timestamp;
}