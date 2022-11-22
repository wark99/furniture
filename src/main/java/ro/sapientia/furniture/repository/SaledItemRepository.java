package ro.sapientia.furniture.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia.furniture.model.SaledItem;

public interface SaledItemRepository extends JpaRepository<SaledItem, Long> {
	
	
	
}
