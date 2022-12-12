package ro.sapientia.furniture.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.TestPropertySource;
import ro.sapientia.furniture.mocking.RegionDatabaseBuilder;
import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.service.RegionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ro.sapientia.furniture.mocking.RegionDatabaseBuilder.buildTestRegions;

//@DataJpaTest
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RegionRepositoryTest {

    @Autowired
    private RegionRepository underTest;

    @Autowired
    private RegionService regionService;

    @BeforeAll
    public static void setup(@Autowired final RegionDatabaseBuilder regionDatabaseBuilder) {
        regionDatabaseBuilder.build();
    }

    @AfterAll
    public static void dispose(@Autowired final RegionDatabaseBuilder regionDatabaseBuilder) {
        regionDatabaseBuilder.clean();
    }

    @Test
    @Order(1)
    public void testFindAll() {
        final var reginExpectedResult = buildTestRegions();

        final var regionList = underTest.findAll();

        assertEquals(2, regionList.size());
        for (int i = 0; i < regionList.size(); i++) {
            assertNotNull(regionList.get(i));
            assertRegionEquals(reginExpectedResult.get(i), regionList.get(i));
        }
    }

    @Test
    @Order(2)
    public void testFindByIdShouldFail() {
        final var regionResult = underTest.findById(99L);

        assertTrue(regionResult.isEmpty());
    }

    @Test
    @Order(3)
    public void testFindByIdShouldSucceed() {
        final var regionIdInDB = regionService.findRegions().get(0).getId();
        final var reginExpectedResult = buildTestRegions().get(0);

        final var regionResult = underTest.findById(regionIdInDB);

        assertTrue(regionResult.isPresent());
        final var region = regionResult.get();
        assertRegionEquals(reginExpectedResult, region);
    }

    @Test
    @Order(4)
    public void testDeleteByIdShouldFail() {
        assertThrows(EmptyResultDataAccessException.class, () -> underTest.deleteById(99L));
    }

    @Test
    @Order(5)
    public void testDeleteByIdShouldSucceed() {
        final var regionIdInDB = regionService.findRegions().get(0).getId();
        final var regionListBefore = underTest.findAll();

        underTest.deleteById(regionIdInDB);

        final var regionListAfter = underTest.findAll();
        assertEquals(1, regionListBefore.size() - regionListAfter.size());
        final var deletedItem = underTest.findById(regionIdInDB);
        assertTrue(deletedItem.isEmpty());
    }

    private static void assertRegionEquals(final Region reginExpectedResult, final Region regionResult) {
        assertNotNull(regionResult.getId());
        assertNotNull(regionResult.getName());
        assertEquals(reginExpectedResult.getName(), regionResult.getName());
    }
}
