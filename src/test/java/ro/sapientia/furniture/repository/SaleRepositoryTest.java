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
		regionRepository.deleteAll();
		servicePointRepository.deleteAll();
		saleRepository.deleteAll();
	}

	@Test
	public void itShouldBeEmpty() {
		// given

		// when
		final int expected = saleRepository.findAll().size();

		// then
		assertEquals(0, expected);
	}

	@Test
	public void itShouldCreateOneSale() {
		// given
		Region region = new Region((long) 0, "Europe");
		region = regionRepository.saveAndFlush(region);

		ServicePoint servicePoint = new ServicePoint(
			(long) 0,
			region,
			"Romania",
			"Mures",
			"Targu Mures",
			"Gheorghe Doja",
			"23/A",
			"123456"
		);
		servicePoint = servicePointRepository.saveAndFlush(servicePoint);

		Sale sale = new Sale(
			(long) 0,
			servicePoint,
			new BigDecimal(1045.9),
			new Timestamp(System.currentTimeMillis())
		);

		// when
		sale = saleRepository.saveAndFlush(sale);

		// then
		assertEquals(sale, saleRepository.findById(sale.getId()).get());
	}

	@Test
	public void itShouldFindSalesByServicePoint() {
		// given
		Region region = new Region((long) 0, "Europe");
		region = regionRepository.saveAndFlush(region);

		ServicePoint servicePoint = new ServicePoint(
			(long) 0,
			region,
			"Romania",
			"Mures",
			"Targu Mures",
			"Gheorghe Doja",
			"23/A",
			"123456"
		);
		servicePoint = servicePointRepository.saveAndFlush(servicePoint);

		Sale sale = new Sale(
			(long) 0,
			servicePoint,
			new BigDecimal(1045.9),
			new Timestamp(System.currentTimeMillis())
		);
		sale = saleRepository.saveAndFlush(sale);

		// when
		List<Sale> sales = saleRepository.findByServicePoint(servicePoint);

		// then
		assertEquals(1, sales.size());
		assertEquals(sale, sales.get(0));
	}
}
