package ro.sapientia.furniture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.sapientia.furniture.model.UsedMaterials;

public interface UsedMaterialsRepository extends JpaRepository<UsedMaterials, Long> {
    UsedMaterials findUsedMaterialsById(final Long id);
}
