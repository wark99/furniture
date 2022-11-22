package ro.sapientia.furniture.model;

import org.elasticsearch.search.DocValueFormat.Decimal;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "used_materials")
public class UsedMaterials implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="pk_used_material_id")
    @SequenceGenerator(name="pk_used_material_id",sequenceName="pk_used_material_id")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    // TODO: create foreign key connections

    @Column(name = "quantity")
    private Decimal quantity;

    @Column(name = "price")
    private Decimal price;

    @Column(name = "timestamp")
    private DateTime timestamp;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public Decimal getQuantity() {

        return quantity;
    }

    public void setQuantity(Decimal quantity) {

        this.quantity = quantity;
    }

    public Decimal getPrice() {

        return price;
    }

    public void setPrice(Decimal price) {

        this.price = price;
    }

    public DateTime getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {

        this.timestamp = timestamp;
    }

    public static long getSerialversionuid() {

        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "UsedMaterials [id=" + id + ", quantity=" + quantity + ", price=" + price + ", timestamp=" + timestamp + "]";
    }
}
