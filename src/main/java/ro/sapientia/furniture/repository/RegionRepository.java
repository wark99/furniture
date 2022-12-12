package ro.sapientia.furniture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.sapientia.furniture.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

}
