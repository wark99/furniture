package ro.sapientia.furniture.service;

import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.dto.RegionRequest;

import java.util.List;

public interface RegionService {

    List<Region> findRegions();

    Region findRegionBy(final Long id);

    Region create(final RegionRequest region);

    void update(final Region region);

    void deleteRegionBy(Long id);
}
