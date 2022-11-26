package ro.sapientia.furniture.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ro.sapientia.furniture.model.Sale;
import ro.sapientia.furniture.model.ServicePoint;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

	List<Sale> findByServicePoint(final ServicePoint servicePoint);

}
