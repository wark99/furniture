package ro.sapientia.furniture.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.ServicePoint;
import ro.sapientia.furniture.model.UsedMaterial;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UsedMaterialRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsedMaterialRepository usedMaterialRepository;

    @AfterEach
    public void tearDown() {
        entityManager.clear();
    }

    @Test
    public void shouldBeEmpty() {
        List<UsedMaterial> expectedUsedMaterials = usedMaterialRepository.findAll();

        assertEquals(0, expectedUsedMaterials.size());
    }

    @Test
    public void shouldContainOneUsedMaterial() {
        Region region = Region.builder().name("Romania").build();
        region = entityManager.persistAndFlush(region);

        ServicePoint servicePoint = ServicePoint.builder().region(region).build();
        servicePoint = entityManager.persistAndFlush(servicePoint);

        Material material = Material.builder().servicePoint(servicePoint).build();
        material = entityManager.persistAndFlush(material);

        UsedMaterial usedMaterial = UsedMaterial.builder().material(material).furnitureId((long) 1).build();
        usedMaterial = entityManager.persistAndFlush(usedMaterial);

        List<UsedMaterial> expectedUsedMaterials = usedMaterialRepository.findAll();

        assertEquals(1, expectedUsedMaterials.size());
    }

    @Test
    public void shouldCreateOneUsedMaterial() {
        Region region = Region.builder().name("Romania").build();
        region = entityManager.persistAndFlush(region);

        ServicePoint servicePoint = ServicePoint.builder().region(region).build();
        servicePoint = entityManager.persistAndFlush(servicePoint);

        Material material = Material.builder().servicePoint(servicePoint).build();
        material = entityManager.persistAndFlush(material);

        UsedMaterial usedMaterial = UsedMaterial.builder().material(material).furnitureId((long) 1).build();

        usedMaterial = usedMaterialRepository.saveAndFlush(usedMaterial);

        assertEquals(usedMaterial, usedMaterialRepository.findById(usedMaterial.getId()).get());
    }

    @Test
    public void shouldFindUsedMaterialsByMaterial() {
        Region region = Region.builder().name("Romania").build();
        region = entityManager.persistAndFlush(region);

        ServicePoint servicePoint = ServicePoint.builder().region(region).build();
        servicePoint = entityManager.persistAndFlush(servicePoint);

        Material material = Material.builder().servicePoint(servicePoint).build();
        material = entityManager.persistAndFlush(material);

        UsedMaterial usedMaterial = UsedMaterial.builder().material(material).furnitureId((long) 1).build();
        usedMaterial = entityManager.persistAndFlush(usedMaterial);

        List<UsedMaterial> expectedUsedMaterials = usedMaterialRepository.findByMaterial(material);

        assertEquals(1, expectedUsedMaterials.size());
        assertEquals(usedMaterial, expectedUsedMaterials.get(0));
    }
}
