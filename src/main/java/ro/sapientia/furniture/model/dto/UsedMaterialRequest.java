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
public class UsedMaterialRequest{
    private Long id;
    private Long materialId;
    private Long furnitureId;
    private int quantity;
    private BigDecimal price;
    private Timestamp timestamp;

    public Long id() {
        return id;
    }

    public Long materialId() {

        return materialId;
    }

    public Long furnitureId() {

        return furnitureId;
    }

    public int quantity() {

        return quantity;
    }

    public BigDecimal price() {
        return price;
    }

    public Timestamp timestamp() {

        return timestamp;
    }
}