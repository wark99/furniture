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

import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.Sale;
import ro.sapientia.furniture.model.SaledItem;
import ro.sapientia.furniture.model.ServicePoint;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SaledItemRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private SaledItemRepository saledItemRepository;

	@AfterEach
	public void tearDown() {
		entityManager.clear();
	}

	@Test
	public void itShouldBeEmpty() {
		// given

		// when
		List<SaledItem> expectedSaledItems = saledItemRepository.findAll();

		// then
		assertEquals(0, expectedSaledItems.size());
	}

	@Test
	public void itShouldContainsOneItem() {
		// given
		Region region = Region.builder().name("Europe").build();
		region = entityManager.persistAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = entityManager.persistAndFlush(servicePoint);

		Sale sale = Sale.builder().servicePoint(servicePoint).build();
		sale = entityManager.persistAndFlush(sale);

		SaledItem saledItem = SaledItem.builder().sale(sale).furnitureId((long) 1).build();
		saledItem = entityManager.persistAndFlush(saledItem);

		// when
		List<SaledItem> expectedSaledItems = saledItemRepository.findAll();


		// then
		assertEquals(1, expectedSaledItems.size());
	}

	@Test
	public void itShouldCreateOneSaledItem() {
		// given
		Region region = Region.builder().name("Europe").build();
		region = entityManager.persistAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = entityManager.persistAndFlush(servicePoint);

		Sale sale = Sale.builder().servicePoint(servicePoint).build();
		sale = entityManager.persistAndFlush(sale);

		SaledItem saledItem = SaledItem.builder().sale(sale).furnitureId((long) 1).build();

		// when
		saledItem = saledItemRepository.saveAndFlush(saledItem);

		// then
		assertEquals(saledItem, saledItemRepository.findById(saledItem.getId()).get());
	}

	@Test
	public void itShouldFindSaledItemsBySale() {
		// given
		Region region = Region.builder().name("Europe").build();
		region = entityManager.persistAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = entityManager.persistAndFlush(servicePoint);

		Sale sale = Sale.builder().servicePoint(servicePoint).build();
		sale = entityManager.persistAndFlush(sale);

		SaledItem saledItem = SaledItem.builder().sale(sale).furnitureId((long) 1).build();
		saledItem = entityManager.persistAndFlush(saledItem);

		// when
		List<SaledItem> expectedSaledItems = saledItemRepository.findBySale(sale);

		// then
		assertEquals(1, expectedSaledItems.size());
		assertEquals(saledItem, expectedSaledItems.get(0));
	}

}
