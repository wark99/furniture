package ro.sapientia.furniture;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.Sale;
import ro.sapientia.furniture.model.ServicePoint;
import ro.sapientia.furniture.model.dto.SaleRequest;

//@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestEntityManager
@TestPropertySource(locations = "classpath:eetest.properties")
public class SaleEETests {

	@Autowired
	private MockMvc mockMvc;

    @Autowired
    private TestEntityManager entityManager;

	@AfterEach
	public void tearDown() {
		entityManager.clear();
	}

	@Test
	public void itShouldReturnAnEmptyArray() throws Exception {
		// given

		// when

		// then
		this.mockMvc.perform(get("/sales/all"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void itShouldReturnTwoSales() throws Exception {
		// given
		Region region = Region.builder().name("Europe").build();
		region = entityManager.persistAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = entityManager.persistAndFlush(servicePoint);

		Sale sale1 = Sale.builder().servicePoint(servicePoint).build();
		sale1 = entityManager.persistAndFlush(sale1);

		Sale sale2 = Sale.builder().servicePoint(servicePoint).build();
		sale2 = entityManager.persistAndFlush(sale2);

		// when

		// then
		this.mockMvc.perform(get("/sales/all"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[*].id", containsInAnyOrder(sale1.getId().intValue(), sale2.getId().intValue())));
	}

	@Test
	public void itShouldGetOneSaleById() throws Exception {
		// given
		Region region = Region.builder().name("Europe").build();
		region = entityManager.persistAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = entityManager.persistAndFlush(servicePoint);

		Sale sale = Sale.builder().servicePoint(servicePoint).build();
		sale = entityManager.persistAndFlush(sale);

		// when

		// then
		this.mockMvc.perform(get("/sales/get/"+sale.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(sale.getId().intValue())));
	}

	@Test
	public void itShouldGet404ErrorForNonExistentSale() throws Exception {
		// given

		// when

		// then
		this.mockMvc.perform(get("/sales/get/123"))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void itShouldCreateOneSale() throws Exception {
		// given
		final ObjectMapper objectMapper = new ObjectMapper();

		Region region = Region.builder().name("Europe").build();
		region = entityManager.persistAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = entityManager.persistAndFlush(servicePoint);

		final BigDecimal totalPrice = new BigDecimal(23);
		final Timestamp saledDate = new Timestamp(System.currentTimeMillis());
		final SaleRequest saleRequest = SaleRequest.builder()
					.id(0L)
					.servicePointId(servicePoint.getId())
					.totalPrice(totalPrice)
					.saledDate(saledDate)
					.build();

		// when

		// then
		this.mockMvc.perform(
				post("/sales/create")
					.content(objectMapper.writeValueAsString(saleRequest))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.servicePoint.id", is(servicePoint.getId().intValue())))
				.andExpect(jsonPath("$.totalPrice").exists())
				.andExpect(jsonPath("$.saledDate").exists());
	}

	@Test
	public void itShouldUpdateOneSale() throws Exception {
		// given
		final ObjectMapper objectMapper = new ObjectMapper();

		Region region = Region.builder().name("Europe").build();
		region = entityManager.persistAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = entityManager.persistAndFlush(servicePoint);

		Sale sale = Sale.builder().servicePoint(servicePoint).build();
		sale = entityManager.persistAndFlush(sale);

		final BigDecimal totalPrice = new BigDecimal(23);
		final Timestamp saledDate = new Timestamp(System.currentTimeMillis());
		final SaleRequest saleRequest = SaleRequest.builder()
					.id(sale.getId())
					.servicePointId(servicePoint.getId())
					.totalPrice(totalPrice)
					.saledDate(saledDate)
					.build();

		// when

		// then
		this.mockMvc.perform(
				post("/sales/update")
					.content(objectMapper.writeValueAsString(saleRequest))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(sale.getId().intValue())))
				.andExpect(jsonPath("$.servicePoint.id", is(servicePoint.getId().intValue())))
				.andExpect(jsonPath("$.totalPrice").exists())
				.andExpect(jsonPath("$.saledDate").exists());
	}

}
