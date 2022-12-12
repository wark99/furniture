package ro.sapientia.furniture.mocking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.repository.RegionRepository;
import ro.sapientia.furniture.repository.ServicePointRepository;

import java.util.List;

@Component
public class RegionDatabaseBuilder implements TestDataBuilder {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ServicePointRepository servicePointRepository;

    @Override
    public void build() {
        final var regions = buildTestRegions();
        regionRepository.saveAllAndFlush(regions);
    }

    @Override
    public void clean() {
        servicePointRepository.deleteAll();
        regionRepository.deleteAll();
    }

    public static List<Region> buildTestRegions() {
        return List.of(
            buildTestRegion(1L, "Test Region 1"),
            buildTestRegion(2L, "Test Region 2"));
    }

    public static Region buildTestRegion(final Long id, final String name) {
        return Region.builder()
            .id(id)
            .name(name)
            .build();
    }
}
