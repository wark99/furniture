package ro.sapientia.furniture.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import lombok.ToString;

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
	@JoinColumn(name = "furniture_id", nullable = false)
	private Furniture furniture;
	
	private int quantity;
	
	private BigDecimal price;
	
	private Timestamp timestamp;
}
