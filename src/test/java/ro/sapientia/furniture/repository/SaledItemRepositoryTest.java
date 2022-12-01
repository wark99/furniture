package ro.sapientia.furniture.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.Sale;
import ro.sapientia.furniture.model.SaledItem;
import ro.sapientia.furniture.model.ServicePoint;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:test.properties")
public class SaledItemRepositoryTest {

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private ServicePointRepository servicePointRepository;

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private SaledItemRepository saledItemRepository;

	@AfterEach
	public void tearDown() {
		saledItemRepository.deleteAll();
		saleRepository.deleteAll();
		servicePointRepository.deleteAll();
		regionRepository.deleteAll();
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
	public void itShouldCreateOneSaledItem() {
		// given
		Region region = Region.builder().name("Europe").build();
		region = regionRepository.saveAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = servicePointRepository.saveAndFlush(servicePoint);

		Sale sale = Sale.builder().servicePoint(servicePoint).build();
		sale = saleRepository.saveAndFlush(sale);

		final long saledItemId = 1;
		SaledItem saledItem = SaledItem.builder().id(saledItemId).furnitureId((long) 1).sale(sale).build();

		// when
		saledItem = saledItemRepository.saveAndFlush(saledItem);

		// then
		assertEquals(saledItem, saledItemRepository.findById(saledItem.getId()).get());
	}

	@Test
	public void itShouldFindSaledItemsBySale() {
		// given
		Region region = Region.builder().name("Europe").build();
		region = regionRepository.saveAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = servicePointRepository.saveAndFlush(servicePoint);

		Sale sale = Sale.builder().servicePoint(servicePoint).build();
		sale = saleRepository.saveAndFlush(sale);

		SaledItem saledItem = SaledItem.builder().sale(sale).furnitureId((long) 1).build();
		saledItem = saledItemRepository.saveAndFlush(saledItem);

		// when
		List<SaledItem> expectedSaledItems = saledItemRepository.findBySale(sale);

		// then
		assertEquals(1, expectedSaledItems.size());
		assertEquals(saledItem, expectedSaledItems.get(0));
	}

}
