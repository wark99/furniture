package ro.sapientia.furniture.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.sapientia.furniture.exception.RecordNotFoundException;
import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.dto.RegionRequest;
import ro.sapientia.furniture.repository.RegionRepository;
import ro.sapientia.furniture.repository.ServicePointRepository;
import ro.sapientia.furniture.service.RegionService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    private final ServicePointRepository servicePointRepository;

    @Override
    public List<Region> findRegions() {
        final var regions = regionRepository.findAll();
        if (Objects.equals(regions.size(), 0)) {
            throw new RecordNotFoundException(Region.class);
        }
        return regions;
    }

    @Override
    public Region findRegionBy(final Long id) {
        return regionRepository.findById(id).orElseThrow(
            () -> new RecordNotFoundException(Region.class, Map.of("id", id.toString())));
    }

    @Override
    public Region create(final RegionRequest regionRequest) {
        final var region = Region.builder().name(regionRequest.name()).build();
        return regionRepository.saveAndFlush(region);
    }

    @Override
    public void update(final Region region) {
        findRegionBy(region.getId());
        regionRepository.saveAndFlush(region);
    }

    @Override
    public void deleteRegionBy(final Long id) {
        final var existingRegion = findRegionBy(id);
        final var servicePointList = servicePointRepository.findAllByRegion(existingRegion);
        servicePointRepository.deleteAll(servicePointList);
        regionRepository.deleteById(id);
    }
}
