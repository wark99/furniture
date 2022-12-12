package ro.sapientia.furniture.repository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ro.sapientia.furniture.mocking.MaterialsDatabaseBuilder;
import ro.sapientia.furniture.utils.CheckComponentsEquality;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MaterialsRepositoryTest {
    @Autowired
    private MaterialsRepository materialsRepository;

    @BeforeAll
    public static void setUp(@Autowired MaterialsDatabaseBuilder materialsDatabaseBuilder) {
        materialsDatabaseBuilder.build();
    }

    @AfterAll
    public static void cleanUp(@Autowired MaterialsDatabaseBuilder materialsDatabaseBuilder) {
        materialsDatabaseBuilder.clean();
    }

    @Test
    public void testFindAll() {
        var expectedMaterials = MaterialsDatabaseBuilder.buildTestMaterials();
        var materialsFromRepository = materialsRepository.findAll();

        Assertions.assertNotNull(materialsFromRepository);
        Assertions.assertEquals(expectedMaterials.size(), materialsFromRepository.size());
        Assertions.assertNotNull(materialsFromRepository.get(0));
        Assertions.assertNotNull(materialsFromRepository.get(1));
        CheckComponentsEquality.assertMaterialEquals(expectedMaterials.get(0), materialsFromRepository.get(0));
        CheckComponentsEquality.assertMaterialEquals(expectedMaterials.get(1), materialsFromRepository.get(1));
    }

    @Test
    public void testFindByIdWithUnexistingMaterialId() {
        var material = materialsRepository.findById(10L).orElse(null);

        Assertions.assertNull(material);
    }

    @Test
    public void testFindByIdWithExistingMaterialId() {
        var expectedMaterials = MaterialsDatabaseBuilder.buildTestMaterials();
        var materialFromRepository = materialsRepository.findById(1L);

        Assertions.assertTrue(materialFromRepository.isPresent());
        Assertions.assertNotNull(expectedMaterials.get(0));
        CheckComponentsEquality.assertMaterialEquals(expectedMaterials.get(0), materialFromRepository.get());
    }

    @Test
    public void testDeleteByIdWithUnexistingId() {
        assertThrows(EmptyResultDataAccessException.class, () -> materialsRepository.deleteById(10L));
    }

    @Test
    public void testDeleteByIdWithExistingId() {
        var materialsBeforeDelete = materialsRepository.findAll();

        materialsRepository.deleteById(2L);
        assertTrue(materialsRepository.findById(2L).isEmpty());

        var materialsAfterDelete = materialsRepository.findAll();
        assertEquals(materialsAfterDelete.size(), materialsBeforeDelete.size() - 1);
    }

    @Test
    public void testGetAllByServicePointIdWithUnexistingServicePoint() {
        var materials = materialsRepository.getByServicePointId(10L);
        assertTrue(materials.isEmpty());
    }

    @Test
    public void testGetAllByServicePointIdWithExistingServicePoint() {
        var materialFromRepository = materialsRepository.getByServicePointId(1L);
        assertEquals(1, materialFromRepository.size());
    }
}
