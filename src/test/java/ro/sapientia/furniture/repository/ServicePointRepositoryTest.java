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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ro.sapientia.furniture.mocking.ServicePointDatabaseBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ro.sapientia.furniture.mocking.RegionDatabaseBuilder.buildTestRegion;
import static ro.sapientia.furniture.mocking.ServicePointDatabaseBuilder.buildTestServicePoints;
import static ro.sapientia.furniture.utils.CheckComponentsEquality.assertServicePointEquals;

//@DataJpaTest
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ServicePointRepositoryTest {

    @Autowired
    private ServicePointRepository underTest;

    @BeforeAll
    public static void setupAll(@Autowired final ServicePointDatabaseBuilder servicePointDatabaseBuilder) {
        servicePointDatabaseBuilder.build();
    }


    @AfterAll
    public static void dispose(@Autowired final ServicePointDatabaseBuilder servicePointDatabaseBuilder) {
        servicePointDatabaseBuilder.clean();
    }

    @Test
    @Order(1)
    public void testFindAll() {
        final var servicePointExpectedResult = buildTestServicePoints();

        final var servicePointList = underTest.findAll();

        assertEquals(2, servicePointList.size());
        for (int i = 0; i < servicePointList.size(); i++) {
            assertNotNull(servicePointList.get(i));
            assertServicePointEquals(servicePointExpectedResult.get(i), servicePointList.get(i));
        }
    }

    @Test
    @Order(2)
    public void testFindByIdShouldFail() {
        final var servicePoint = underTest.findById(3L);

        assertTrue(servicePoint.isEmpty());
    }

    @Test
    @Order(3)
    public void testFindByIdShouldSucceed() {
        final var servicePointExpectedResult = buildTestServicePoints().get(0);

        final var servicePointResult = underTest.findById(1L);

        assertTrue(servicePointResult.isPresent());
        final var servicePoint = servicePointResult.get();
        assertServicePointEquals(servicePointExpectedResult, servicePoint);
    }

    @Test
    @Order(4)
    public void testDeleteByIdShouldFail() {
        assertThrows(EmptyResultDataAccessException.class, () -> underTest.deleteById(3L));
    }

    @Test
    @Order(5)
    public void testDeleteByIdShouldSucceed() {
        final var servicePointListBefore = underTest.findAll();

        underTest.deleteById(2L);

        final var servicePointListAfter = underTest.findAll();
        assertEquals(1, servicePointListBefore.size() - servicePointListAfter.size());
        final var deletedItem = underTest.findById(2L);
        assertTrue(deletedItem.isEmpty());
    }

    @Test
    @Order(6)
    public void testFindAllByRegionShouldFail() {
        final var region = buildTestRegion(9L, "Test Region 9");

        final var servicePointList = underTest.findAllByRegion(region);

        assertEquals(0, servicePointList.size());
    }

    @Test
    @Order(7)
    public void testFindAllByRegionShouldSucceed() {
        final var region = buildTestRegion(1L, "Test Region 1");

        final var servicePointList = underTest.findAllByRegion(region);

        assertEquals(1, servicePointList.size());
        final var regionResult = servicePointList.get(0).getRegion();
        assertNotNull(regionResult.getId());
        assertEquals(region.getId(), regionResult.getId());
        assertNotNull(regionResult.getName());
        assertEquals(region.getName(), regionResult.getName());
    }

}
