package ro.sapientia.furniture.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "service_points")
public class ServicePoint implements Serializable {

    @Id
    @Column(name = "service_point_id", nullable = false)
    @SequenceGenerator(name = "pk_service_points", sequenceName = "pk_service_points")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_service_points")
    Long id;

    @NotNull
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "region_id", nullable = false)
    Region region;

    String country;

    String county;

    String city;

    String street;

    String number;

    @Column(name = "zip_code")
    String zipCode;
}
