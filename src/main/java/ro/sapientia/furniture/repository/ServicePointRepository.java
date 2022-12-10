package ro.sapientia.furniture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.ServicePoint;

import java.util.List;

@Repository
public interface ServicePointRepository extends JpaRepository<ServicePoint, Long> {
    List<ServicePoint> findAllByRegion(final Region region);
}
