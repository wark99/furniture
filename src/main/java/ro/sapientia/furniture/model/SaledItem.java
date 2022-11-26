package ro.sapientia.furniture.model;

import java.io.Serializable;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "saled_items")
public class SaledItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="pk_saled_items")
	@SequenceGenerator(name="pk_saled_items", sequenceName="pk_saled_items")
	@Column(name = "saled_item_id", nullable = false, updatable = false)
	private Long id;

	@NotNull
	@ManyToOne
	@ToString.Exclude
	@JoinColumn(name = "sale_id", nullable = false)
	private Sale sale;

	@NotNull
	@Column(name = "furniture_id")
	private Long furnitureId;

	private int quantity;

	private BigDecimal price;

	private Timestamp timestamp;
}
