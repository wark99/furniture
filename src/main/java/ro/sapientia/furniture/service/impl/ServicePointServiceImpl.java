package ro.sapientia.furniture.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.sapientia.furniture.exception.RecordNotFoundException;
import ro.sapientia.furniture.model.ServicePoint;
import ro.sapientia.furniture.model.dto.ServicePointRequest;
import ro.sapientia.furniture.repository.ServicePointRepository;
import ro.sapientia.furniture.service.RegionService;
import ro.sapientia.furniture.service.ServicePointService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ServicePointServiceImpl implements ServicePointService {

    private final ServicePointRepository servicePointRepository;
    private final RegionService regionService;

    @Override
    public List<ServicePoint> findServicePoints() {
        if (Objects.equals(servicePointRepository.findAll().size(), 0)) {
            throw new RecordNotFoundException(ServicePoint.class);
        }
        return servicePointRepository.findAll();
    }

    @Override
    public ServicePoint findServicePointBy(final Long id) {
        return servicePointRepository.findById(id).orElseThrow(
            () -> new RecordNotFoundException(ServicePoint.class, Map.of("id", id.toString())));
    }

    @Override
    public ServicePoint create(final ServicePointRequest servicePointRequest) {
        final var servicePoint = ServicePoint.builder()
            .region(regionService.findRegionBy(servicePointRequest.regionId()))
            .country(servicePointRequest.country())
            .county(servicePointRequest.county())
            .city(servicePointRequest.city())
            .street(servicePointRequest.street())
            .number(servicePointRequest.number())
            .zipCode(servicePointRequest.zipCode())
            .build();
        return servicePointRepository.saveAndFlush(servicePoint);
    }

    @Override
    public void update(final ServicePoint servicePoint) {
        final var existingServicePoint = findServicePointBy(servicePoint.getId());
        final var updatedServicePoint = ServicePoint.builder()
            .id(existingServicePoint.getId())
            .region(regionService.findRegionBy(servicePoint.getRegion().getId()))
            .country(servicePoint.getCountry())
            .county(servicePoint.getCounty())
            .city(servicePoint.getCity())
            .street(servicePoint.getStreet())
            .number(servicePoint.getNumber())
            .zipCode(servicePoint.getZipCode())
            .build();
        servicePointRepository.saveAndFlush(updatedServicePoint);
    }

    @Override
    public void deleteServicePointBy(final Long id) {
        findServicePointBy(id);
        servicePointRepository.deleteById(id);
    }
}
