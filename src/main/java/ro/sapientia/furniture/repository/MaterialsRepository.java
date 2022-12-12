package ro.sapientia.furniture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.sapientia.furniture.model.Material;

import java.util.List;

@Repository
public interface MaterialsRepository extends JpaRepository<Material, Long> {
    List<Material> getByServicePointId(Long servicePointId);
}
