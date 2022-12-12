package ro.sapientia.furniture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.UsedMaterial;
import java.util.List;

public interface UsedMaterialRepository extends JpaRepository<UsedMaterial, Long> {
    List<UsedMaterial> findByMaterial(final Material material);
}
