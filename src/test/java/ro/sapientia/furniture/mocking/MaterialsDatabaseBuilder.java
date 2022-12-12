package ro.sapientia.furniture.mocking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.ServicePoint;
import ro.sapientia.furniture.repository.MaterialsRepository;
import ro.sapientia.furniture.repository.RegionRepository;
import ro.sapientia.furniture.repository.ServicePointRepository;

import java.util.List;


@Component
public class MaterialsDatabaseBuilder implements TestDataBuilder{
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ServicePointRepository servicePointRepository;
    @Autowired
    private MaterialsRepository materialsRepository;

    @Override
    public void build() {
        var regions = RegionDatabaseBuilder.buildTestRegions();
        var servicePoints = ServicePointDatabaseBuilder.buildTestServicePoints();
        var materials = buildTestMaterials();
        regionRepository.saveAllAndFlush(regions);
        servicePointRepository.saveAllAndFlush(servicePoints);
        materialsRepository.saveAllAndFlush(materials);
    }

    @Override
    public void clean() {
        materialsRepository.deleteAll();
        servicePointRepository.deleteAll();
        regionRepository.deleteAll();
    }

    public static List<Material> buildTestMaterials() {
        var servicePoints = ServicePointDatabaseBuilder.buildTestServicePoints();
        var material1 = buildTestMaterial(
                1L,
                servicePoints.get(0),
                "Material Name 1",
                "Material Origin 1",
                "Material Unit 1",
                10.0,
                1.0,
                "Material Quality 1"
        );
        var material2 = buildTestMaterial(
                2L,
                servicePoints.get(1),
                "Material Name 2",
                "Material Origin 2",
                "Material Unit 2",
                20.0,
                2.0,
                "Material Quality 2"
        );
        return List.of(material1, material2);
    }

    public static Material buildTestMaterial(final Long id, final ServicePoint servicePoint, final String name,
                                              final String origin, final String unit, final Double unitPrice,
                                              final Double quantity, final String quality) {
        return Material.builder()
                .id(id)
                .servicePoint(servicePoint)
                .name(name)
                .origin(origin)
                .unit(unit)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .quality(quality)
                .build();
    }

}
