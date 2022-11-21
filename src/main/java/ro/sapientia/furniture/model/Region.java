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
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "regions")
public class Region implements Serializable {

    @Id
    @Column(name = "region_id", nullable = false)
    @SequenceGenerator(name = "pk_regions", sequenceName = "pk_regions")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_regions")
    Long id;

    @NotNull
    @Column(nullable = false)
    String name;
}
