package ro.sapientia.furniture.mocking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.ServicePoint;
import ro.sapientia.furniture.repository.RegionRepository;
import ro.sapientia.furniture.repository.ServicePointRepository;

import java.util.List;

import static ro.sapientia.furniture.mocking.RegionDatabaseBuilder.buildTestRegions;

@Component
public class ServicePointDatabaseBuilder implements TestDataBuilder {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ServicePointRepository servicePointRepository;

    @Override
    public void build() {
        final var regions = buildTestRegions();
        final var servicePoints = buildTestServicePointsWithRegions(regions);
        regionRepository.saveAllAndFlush(regions);
        servicePointRepository.saveAllAndFlush(servicePoints);
    }

    @Override
    public void clean() {
        servicePointRepository.deleteAll();
        regionRepository.deleteAll();
    }

    public static List<ServicePoint> buildTestServicePointsWithRegions(final List<Region> regions) {
        return List.of(
            buildTestServicePoint(
                1L,
                regions.get(0),
                "Test Country 1",
                "Test County 1",
                "Test City 1",
                "Test Street 1",
                "Test Number 1",
                "Test Zip Code 1"),
            buildTestServicePoint(
                2L,
                regions.get(1),
                "Test Country 2",
                "Test County 2",
                "Test City 2",
                "Test Street 2",
                "Test Number 2",
                "Test Zip Code 2")
        );
    }

    public static List<ServicePoint> buildTestServicePoints() {
        final var regions = buildTestRegions();
        return List.of(
            buildTestServicePoint(
                1L,
                regions.get(0),
                "Test Country 1",
                "Test County 1",
                "Test City 1",
                "Test Street 1",
                "Test Number 1",
                "Test Zip Code 1"),
            buildTestServicePoint(
                2L,
                regions.get(1),
                "Test Country 2",
                "Test County 2",
                "Test City 2",
                "Test Street 2",
                "Test Number 2",
                "Test Zip Code 2")
        );
    }

    private static ServicePoint buildTestServicePoint(
        final Long id,
        final Region region,
        final String country,
        final String county,
        final String city,
        final String street,
        final String number,
        final String zipCode
    ) {
        return ServicePoint.builder()
            .id(id)
            .region(region)
            .country(country)
            .county(county)
            .city(city)
            .street(street)
            .number(number)
            .zipCode(zipCode)
            .build();
    }
}
