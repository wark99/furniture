package ro.sapientia.furniture.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ro.sapientia.furniture.model.Sale;
import ro.sapientia.furniture.model.SaledItem;

@Repository
public interface SaledItemRepository extends JpaRepository<SaledItem, Long> {

	List<SaledItem> findBySale(final Sale sale);

}
