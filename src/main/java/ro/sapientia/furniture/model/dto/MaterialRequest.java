package ro.sapientia.furniture.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaterialRequest {
    private Long servicePointId;
    private String name;
    private String origin;
    private String unit;
    private Double unitPrice;
    private Double quantity;
    private String quality;
}
