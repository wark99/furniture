package ro.sapientia.furniture.model.dto;

import lombok.Getter;

@Getter
public class MaterialDTO {
    private Long servicePointId;
    private String name;
    private String origin;
    private String unit;
    private Double unitPrice;
    private Double quantity;
    private String quality;
}
