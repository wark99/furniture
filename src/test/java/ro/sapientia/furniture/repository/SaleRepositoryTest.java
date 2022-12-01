package ro.sapientia.furniture.repository;

import java.util.List;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.Sale;
import ro.sapientia.furniture.model.ServicePoint;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:test.properties")
public class SaleRepositoryTest {

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private ServicePointRepository servicePointRepository;

	@Autowired
	private SaleRepository saleRepository;

	@AfterEach
	public void tearDown() {
		saleRepository.deleteAll();
		servicePointRepository.deleteAll();
		regionRepository.deleteAll();
	}

	@Test
	public void itShouldBeEmpty() {
		// given

		// when
		final List<Sale> expectedSales = saleRepository.findAll();

		// then
		assertEquals(0, expectedSales.size());
	}

	@Test
	public void itShouldCreateOneSale() {
		// given
		Region region = Region.builder().name("Europe").build();
		region = regionRepository.saveAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = servicePointRepository.saveAndFlush(servicePoint);

		Sale sale = Sale.builder().servicePoint(servicePoint).build();

		// when
		sale = saleRepository.saveAndFlush(sale);

		// then
		assertEquals(sale, saleRepository.findById(sale.getId()).get());
	}

	@Test
	public void itShouldFindSalesByServicePoint() {
		// given
		Region region = Region.builder().name("Europe").build();
		region = regionRepository.saveAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = servicePointRepository.saveAndFlush(servicePoint);

		Sale sale = Sale.builder().servicePoint(servicePoint).build();
		sale = saleRepository.saveAndFlush(sale);

		// when
		List<Sale> expectedSales = saleRepository.findByServicePoint(servicePoint);

		// then
		assertEquals(1, expectedSales.size());
		assertEquals(sale, expectedSales.get(0));
	}
}
