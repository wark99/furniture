package ro.sapientia.furniture.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
//import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "materials")
public class Material implements Serializable {
    //@Serial
    //private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "material_id", nullable = false)
    @SequenceGenerator(name = "pk_materials", sequenceName = "pk_materials")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_materials")
    private Long id;

    @NotNull
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "service_point_id", nullable = false)
    private ServicePoint servicePoint;

    private String name;

    private String origin;

    private String unit;

    @Column(name = "unit_price")
    private Double unitPrice;

    private Double quantity;

    private String quality;
}
