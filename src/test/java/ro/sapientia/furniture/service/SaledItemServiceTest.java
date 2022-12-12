package ro.sapientia.furniture.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ro.sapientia.furniture.model.Sale;
import ro.sapientia.furniture.model.SaledItem;
import ro.sapientia.furniture.model.dto.SaledItemRequest;
import ro.sapientia.furniture.repository.SaledItemRepository;

public class SaledItemServiceTest {

	private SaledItemRepository saledItemRepository;

	private SaleService saleService;

	private SaledItemService saledItemService;

	@BeforeEach
	public void setUp() {
		saledItemRepository = mock(SaledItemRepository.class);
		saleService = mock(SaleService.class);
		saledItemService = new SaledItemService(saledItemRepository, saleService);
	}

	@Test
	public void findAllShouldReturnAnEmptyList() {
		// given

		// when
		when(saledItemService.findAll()).thenReturn(Collections.emptyList());
		final List<SaledItem> expectedSaledItems = saledItemService.findAll();

		// then
		assertEquals(0, expectedSaledItems.size());
	}

	@Test
	public void findByIdShouldReturnNullForNonExistentSaledItem() {
		// given
		final long saledItemId = 1;

		// when
		when(saledItemRepository.findById(saledItemId)).thenReturn(Optional.empty());
		final SaledItem expectedSaledItem = saledItemService.findById(saledItemId);

		// then
		assertNull(expectedSaledItem);
	}

	@Test
	public void findBySaleShouldReturnOneSaledItem() {
		//given
		final long saledItemId = 1;
		final long saleId = 1;
		final Sale sale = Sale.builder().id(saleId).build();
		final SaledItem saledItem = SaledItem.builder().id(saledItemId).sale(sale).build();

		// when
		when(saledItemRepository.findBySale(any(Sale.class))).thenReturn(List.of(saledItem));
		List<SaledItem> expectedSaledItems = saledItemService.findBySale(sale);

		// then
		assertEquals(1, expectedSaledItems.size());
		assertEquals(saledItemId, (long) expectedSaledItems.get(0).getId());
		assertEquals(saleId, (long) expectedSaledItems.get(0).getSale().getId());
	}

	@Test
	public void createShouldReturnTheCreatedSaledItem() {
		//given
		final long saledItemId = 1;
		final long saleId = 1;
		final SaledItemRequest saledItemRequest = SaledItemRequest.builder()
				.id(saledItemId)
				.saleId(saleId)
				.furnitureId(1L)
				.quantity(1)
				.price(new BigDecimal(23))
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.build();
		final Sale sale = Sale.builder().id(saleId).build();
		final SaledItem saledItem = SaledItem.builder().id(saledItemId).sale(sale).build();

		// when
		when(saleService.findById(saleId)).thenReturn(sale);
		when(saledItemRepository.saveAndFlush(any(SaledItem.class))).thenReturn(saledItem);
		SaledItem expectedSaledItem = saledItemService.create(saledItemRequest);

		// then
		assertEquals(saledItem, expectedSaledItem);
	}

	@Test
	public void updateShouldReturnNullForNonExistentSaledItem() {
		// given
		final long saledItemId = 1;
		final SaledItemRequest saledItemRequest = SaledItemRequest.builder()
				.id(saledItemId)
				.saleId(1L)
				.furnitureId(1L)
				.quantity(1)
				.price(new BigDecimal(23))
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.build();

		//when
		when(saledItemRepository.findById(any())).thenReturn(Optional.empty());
		final SaledItem expectedSaledItem = saledItemService.update(saledItemRequest);

		// then
		assertNull(expectedSaledItem);
	}

	@Test
	public void updateShouldReturnTheUpdatedSaledItem() {
		// given
		final long saledItemId = 1;
		final long saleId = 1;
		final Sale sale = Sale.builder().id(saleId).build();
		final SaledItem saledItem = SaledItem.builder().id(saledItemId).sale(sale).build();
		final SaledItemRequest saledItemRequest = SaledItemRequest.builder()
				.id(saledItemId)
				.saleId(saleId)
				.furnitureId(1L)
				.quantity(1)
				.price(new BigDecimal(23))
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.build();

		//when
		when(saledItemRepository.findById(any())).thenReturn(Optional.of(saledItem));
		when(saleService.findById(any())).thenReturn(sale);
		when(saledItemRepository.saveAndFlush(any(SaledItem.class))).thenReturn(saledItem);
		final SaledItem expectedSaledItem = saledItemService.update(saledItemRequest);

		// then
		assertNotNull(expectedSaledItem);
		assertEquals(saledItemId, (long) expectedSaledItem.getId());
		assertEquals(saleId, (long) expectedSaledItem.getSale().getId());
	}

	@Test
	public void deleteShouldDeleteOneItem() {
		// given
		final SaledItem saledItem = new SaledItem();

		// when
		when(saledItemRepository.findById(anyLong())).thenReturn(Optional.of(saledItem));
		saledItemService.delete(1L);

		// then
		verify(saledItemRepository, times(1)).delete(any(SaledItem.class));
	}

	@Test
	public void itShouldNotDeleteForNonExistentSaledItem() {
		// given

		// when
		when(saledItemRepository.findById(anyLong())).thenReturn(Optional.empty());
		saledItemService.delete(1L);

		// then
		verify(saledItemRepository, times(0)).delete(any(SaledItem.class));
	}

}
