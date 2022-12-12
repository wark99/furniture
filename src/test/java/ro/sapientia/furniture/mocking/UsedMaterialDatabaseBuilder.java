package ro.sapientia.furniture.mocking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.UsedMaterial;
import ro.sapientia.furniture.repository.MaterialsRepository;
import ro.sapientia.furniture.repository.RegionRepository;
import ro.sapientia.furniture.repository.ServicePointRepository;
import ro.sapientia.furniture.repository.UsedMaterialRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Component
public class UsedMaterialDatabaseBuilder implements TestDataBuilder{
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ServicePointRepository servicePointRepository;
    @Autowired
    private MaterialsRepository materialsRepository;

    @Autowired
    private UsedMaterialRepository usedMaterialRepository;

    @Override
    public void build() {
        var regions = RegionDatabaseBuilder.buildTestRegions();
        var servicePoints = ServicePointDatabaseBuilder.buildTestServicePoints();
        var materials = MaterialsDatabaseBuilder.buildTestMaterials();
        var usedMaterials = buildTestUsedMaterials();

        regionRepository.saveAllAndFlush(regions);
        servicePointRepository.saveAllAndFlush(servicePoints);
        materialsRepository.saveAllAndFlush(materials);
        usedMaterialRepository.saveAllAndFlush(usedMaterials);
    }

    @Override
    public void clean() {
        usedMaterialRepository.deleteAll();
        materialsRepository.deleteAll();
        servicePointRepository.deleteAll();
        regionRepository.deleteAll();
    }

    public static List<UsedMaterial> buildTestUsedMaterials() {
        var materials = MaterialsDatabaseBuilder.buildTestMaterials();

        var usedMaterial1 = buildTestUsedMaterial(
                1L,
                materials.get(0),
                1L,
                10,
                new BigDecimal(100),
                new Timestamp(System.currentTimeMillis())
        );
        var usedMaterial2 = buildTestUsedMaterial(
                2L,
                materials.get(0),
                1L,
                10,
                new BigDecimal(100),
                new Timestamp(System.currentTimeMillis())
        );
        return List.of(usedMaterial1, usedMaterial2);
    }

    public static UsedMaterial buildTestUsedMaterial(final Long id, final Material material, final Long furnitureId,
                                                     final int quantity, final BigDecimal price, final Timestamp timestamp) {
        return UsedMaterial.builder()
                .id(id)
                .material(material)
                .furnitureId(furnitureId)
                .quantity(quantity)
                .price(price)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
