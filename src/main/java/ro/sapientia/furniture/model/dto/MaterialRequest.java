package ro.sapientia.furniture.model.dto;

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
public class MaterialRequest {
    private Long id;
    private Long servicePointId;
    private String name;
    private String origin;
    private String unit;
    private Double unitPrice;
    private Double quantity;
    private String quality;
}
