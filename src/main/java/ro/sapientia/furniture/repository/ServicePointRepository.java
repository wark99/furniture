package ro.sapientia.furniture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.ServicePoint;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicePointRepository extends JpaRepository<ServicePoint, Long> {
    Optional<List<ServicePoint>> findAllByRegion(final Region region);
}