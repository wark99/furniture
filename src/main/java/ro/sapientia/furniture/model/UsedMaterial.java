package ro.sapientia.furniture.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "used_materials")
public class UsedMaterial implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="pk_used_material_id")
    @SequenceGenerator(name="pk_used_material_id",sequenceName="pk_used_material_id")
    @Column(name = "used_material_id", nullable = false, updatable = false)
    private Long id;

    @NotNull
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @NotNull
    @Column(name = "furniture_id")
    private Long furnitureId;

    private int quantity;

    private BigDecimal price;

    private Timestamp timestamp;
}
