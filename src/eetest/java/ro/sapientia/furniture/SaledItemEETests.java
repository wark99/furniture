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
import ro.sapientia.furniture.model.SaledItem;
import ro.sapientia.furniture.model.ServicePoint;
import ro.sapientia.furniture.model.dto.SaledItemRequest;

//@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestEntityManager
@TestPropertySource(locations = "classpath:eetest.properties")
public class SaledItemEETests {

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
		this.mockMvc.perform(get("/saled-items/all"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void itShouldReturnTwoSaledItems() throws Exception {
		// given
		Region region = Region.builder().name("Europe").build();
		region = entityManager.persistAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = entityManager.persistAndFlush(servicePoint);

		Sale sale = Sale.builder().servicePoint(servicePoint).build();
		sale = entityManager.persistAndFlush(sale);

		SaledItem saledItem1 = SaledItem.builder().sale(sale).furnitureId(1L).build();
		saledItem1 = entityManager.persistAndFlush(saledItem1);

		SaledItem saledItem2 = SaledItem.builder().sale(sale).furnitureId(1L).build();
		saledItem2 = entityManager.persistAndFlush(saledItem2);

		// when

		// then
		this.mockMvc.perform(get("/saled-items/all"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[*].id", containsInAnyOrder(saledItem1.getId().intValue(), saledItem2.getId().intValue())));
	}

	@Test
	public void itShouldGetOneSaledItemById() throws Exception {
		// given
		Region region = Region.builder().name("Europe").build();
		region = entityManager.persistAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = entityManager.persistAndFlush(servicePoint);

		Sale sale = Sale.builder().servicePoint(servicePoint).build();
		sale = entityManager.persistAndFlush(sale);

		SaledItem saledItem = SaledItem.builder().sale(sale).furnitureId(1L).build();
		saledItem = entityManager.persistAndFlush(saledItem);

		// when

		// then
		this.mockMvc.perform(get("/saled-items/get/"+saledItem.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(saledItem.getId().intValue())));
	}

	@Test
	public void itShouldGet404ErrorForNonExistentSaledItem() throws Exception {
		// given

		// when

		// then
		this.mockMvc.perform(get("/saled-items/get/123"))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void itShouldCreateOneSaledItem() throws Exception {
		// given
		final ObjectMapper objectMapper = new ObjectMapper();

		Region region = Region.builder().name("Europe").build();
		region = entityManager.persistAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = entityManager.persistAndFlush(servicePoint);

		Sale sale = Sale.builder().servicePoint(servicePoint).build();
		sale = entityManager.persistAndFlush(sale);

		final int quantity = 2;
		final BigDecimal price = new BigDecimal(23);
		final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		final SaledItemRequest saledItemRequest = SaledItemRequest.builder()
					.id(0L)
					.saleId(sale.getId())
					.furnitureId(1L)
					.quantity(quantity)
					.price(price)
					.timestamp(timestamp)
					.build();

		// when

		// then
		this.mockMvc.perform(
				post("/saled-items/create")
					.content(objectMapper.writeValueAsString(saledItemRequest))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.sale.id", is(sale.getId().intValue())))
				.andExpect(jsonPath("$.furnitureId").exists())
				.andExpect(jsonPath("$.quantity").exists())
				.andExpect(jsonPath("$.price").exists())
				.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	public void itShouldUpdateOneSaledItem() throws Exception {
		// given
		final ObjectMapper objectMapper = new ObjectMapper();

		Region region = Region.builder().name("Europe").build();
		region = entityManager.persistAndFlush(region);

		ServicePoint servicePoint = ServicePoint.builder().region(region).build();
		servicePoint = entityManager.persistAndFlush(servicePoint);

		Sale sale = Sale.builder().servicePoint(servicePoint).build();
		sale = entityManager.persistAndFlush(sale);

		SaledItem saledItem = SaledItem.builder().sale(sale).furnitureId(1L).build();
		saledItem = entityManager.persistAndFlush(saledItem);

		final int quantity = 2;
		final BigDecimal price = new BigDecimal(23);
		final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		final SaledItemRequest saledItemRequest = SaledItemRequest.builder()
				.id(saledItem.getId())
				.saleId(sale.getId())
				.furnitureId(1L)
				.quantity(quantity)
				.price(price)
				.timestamp(timestamp)
				.build();

		// when

		// then
		this.mockMvc.perform(
				post("/saled-items/update")
					.content(objectMapper.writeValueAsString(saledItemRequest))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(saledItem.getId().intValue())))
				.andExpect(jsonPath("$.sale.id", is(sale.getId().intValue())))
				.andExpect(jsonPath("$.furnitureId").exists())
				.andExpect(jsonPath("$.quantity").exists())
				.andExpect(jsonPath("$.price").exists())
				.andExpect(jsonPath("$.timestamp").exists());
	}

}
