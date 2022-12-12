package ro.sapientia.furniture.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import ro.sapientia.furniture.model.SaledItem;
import ro.sapientia.furniture.model.dto.SaledItemRequest;
import ro.sapientia.furniture.service.SaleService;
import ro.sapientia.furniture.service.SaledItemService;

@WebMvcTest(controllers = SaledItemController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class SaledItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean(SaledItemService.class)
	private SaledItemService saledItemService;

	@Test
	public void itShouldGetTwoSaledItems() throws Exception {
		// given
		final SaledItem saledItem1 = new SaledItem();
		final SaledItem saledItem2 = new SaledItem();

		// when
		when(saledItemService.findAll()).thenReturn(List.of(saledItem1, saledItem2));

		// then
		this.mockMvc.perform(get("/saled-items/all"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json("[{}, {}]"));
	}

	@Test
	public void itShouldGetOneSaledItemById() throws Exception {
		// given
		final SaledItem saledItem = new SaledItem();
		saledItem.setId(1L);

		// when
		when(saledItemService.findById(anyLong())).thenReturn(saledItem);

		// then
		this.mockMvc.perform(get("/saled-items/get/1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(1)));
	}

	@Test
	public void itShouldGet404ErrorForNonExistentSaledItem() throws Exception {
		// given

		// when
		when(saledItemService.findById(anyLong())).thenReturn(null);

		// then
		this.mockMvc.perform(get("/saled-items/get/1"))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void itShouldCreateOneSaledItem() throws Exception {
		// given
		final ObjectMapper objectMapper = new ObjectMapper();
		final SaledItem saledItem = new SaledItem();
		saledItem.setId(1L);

		// when
		when(saledItemService.create(any(SaledItemRequest.class))).thenReturn(saledItem);

		// then
		this.mockMvc.perform(
				post("/saled-items/create")
					.content(objectMapper.writeValueAsString(new SaledItemRequest()))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)));

		verify(saledItemService, times(1)).create(any(SaledItemRequest.class));
	}

	@Test
	public void itShouldUpdateOneSaledItem() throws Exception {
		// given
		final ObjectMapper objectMapper = new ObjectMapper();
		final SaledItemRequest saledItemRequest = SaledItemRequest.builder().id(1L).build();
		final BigDecimal price = new BigDecimal(15.3);
		final SaledItem saledItem = new SaledItem();
		saledItem.setId(1L);
		saledItem.setPrice(price);

		// when
		when(saledItemService.update(any(SaledItemRequest.class))).thenReturn(saledItem);

		// then
		this.mockMvc.perform(
				post("/saled-items/update")
					.content(objectMapper.writeValueAsString(saledItemRequest))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.price", is(price)));

		verify(saledItemService, times(1)).update(any(SaledItemRequest.class));
	}

	@Test
	public void itShouldDeleteOneSaledItem() throws Exception {
		// given

		// when

		// then
		this.mockMvc.perform(delete("/saled-items/delete/1"))
				.andDo(print())
				.andExpect(status().isOk());

		verify(saledItemService, times(1)).delete(anyLong());
	}
}
