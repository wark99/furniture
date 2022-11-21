package ro.sapientia.furniture.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia.furniture.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
	
	List<Sale> findByServicePoint(final ServicePoint servicePoint);
	
}
