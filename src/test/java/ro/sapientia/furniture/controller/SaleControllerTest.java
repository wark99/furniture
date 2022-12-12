package ro.sapientia.furniture.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import ro.sapientia.furniture.model.Sale;
import ro.sapientia.furniture.model.dto.SaleRequest;
import ro.sapientia.furniture.service.SaleService;

@WebMvcTest(controllers = SaleController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class SaleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean(SaleService.class)
	private SaleService saleService;

	@Test
	public void itShouldGetTwoSales() throws Exception {
		// given
		final Sale sale1 = new Sale();
		final Sale sale2 = new Sale();

		// when
		when(saleService.findAll()).thenReturn(List.of(sale1, sale2));

		// then
		this.mockMvc.perform(get("/sales/all"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json("[{}, {}]"));
	}

	@Test
	public void itShouldGetOneSaleById() throws Exception {
		// given
		final Sale sale = new Sale();
		sale.setId(1L);

		// when
		when(saleService.findById(anyLong())).thenReturn(sale);

		// then
		this.mockMvc.perform(get("/sales/get/1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(1)));
	}

	@Test
	public void itShouldGet404ErrorForNonExistentSale() throws Exception {
		// given

		// when
		when(saleService.findById(anyLong())).thenReturn(null);

		// then
		this.mockMvc.perform(get("/sales/get/1"))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void itShouldCreateOneSale() throws Exception {
		// given
		final ObjectMapper objectMapper = new ObjectMapper();
		final SaleRequest saleRequest = new SaleRequest(1L, 1L, null, null);
		final Sale sale = new Sale();
		sale.setId(1L);

		// when
		when(saleService.create(any(SaleRequest.class))).thenReturn(sale);

		// then
		this.mockMvc.perform(
				post("/sales/create")
					.content(objectMapper.writeValueAsString(saleRequest))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)));

		verify(saleService, times(1)).create(any(SaleRequest.class));
	}

	@Test
	public void itShouldUpdateOneSale() throws Exception {
		// given
		final ObjectMapper objectMapper = new ObjectMapper();
		final SaleRequest saleRequest = new SaleRequest(1L, 1L, null, null);
		final BigDecimal totalPrice = new BigDecimal(23.9);
		final Sale sale = new Sale();
		sale.setId(1L);
		sale.setTotalPrice(totalPrice);

		// when
		when(saleService.update(any(SaleRequest.class))).thenReturn(sale);

		// then
		this.mockMvc.perform(
				post("/sales/update")
					.content(objectMapper.writeValueAsString(saleRequest))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.totalPrice", is(totalPrice)));

		verify(saleService, times(1)).update(any(SaleRequest.class));
	}

	@Test
	public void itShouldDeleteOneSale() throws Exception {
		// given

		// when

		// then
		this.mockMvc.perform(delete("/sales/delete/1"))
				.andDo(print())
				.andExpect(status().isOk());

		verify(saleService, times(1)).delete(anyLong());
	}

}
