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
import ro.sapientia.furniture.model.ServicePoint;
import ro.sapientia.furniture.model.dto.SaleRequest;
import ro.sapientia.furniture.repository.SaleRepository;
import ro.sapientia.furniture.repository.SaledItemRepository;

public class SaleServiceTest {

	private SaleService saleService;

	private SaleRepository saleRepository;

	private ServicePointService servicePointService;

	private SaledItemRepository saledItemRepository;

	@BeforeEach
	public void setUp() {
		saleRepository = mock(SaleRepository.class);
		servicePointService = mock(ServicePointService.class);
		saledItemRepository = mock(SaledItemRepository.class);
		saleService = new SaleService(saleRepository, saledItemRepository, servicePointService);
	}

	@Test
	public void findAllShouldReturnAnEmptyList() {
		// given

		// when
		when(saleRepository.findAll()).thenReturn(Collections.emptyList());
		final List<Sale> expectedSales = saleService.findAll();

		// then
		assertEquals(0, expectedSales.size());
	}

	@Test
	public void findByIdShouldReturnNullForNonExistentSale() {
		// given
		final long saleId = 1;

		// when
		when(saleRepository.findById(saleId)).thenReturn(Optional.empty());
		final Sale expectedSale = saleService.findById(saleId);

		// then
		assertNull(expectedSale);
	}

	@Test
	public void createShouldReturnTheCreatedSale() {
		//given
		final long saleId = 1;
		final long servicePointId = 1;
		final SaleRequest saleRequest = new SaleRequest(
				saleId,
				servicePointId,
				new BigDecimal(23),
				new Timestamp(System.currentTimeMillis())
		);
		final ServicePoint servicePoint = ServicePoint.builder().id(servicePointId).build();
		final Sale sale = Sale.builder().id(saleId).servicePoint(servicePoint).build();

		// when
		when(servicePointService.findServicePointBy(servicePointId)).thenReturn(servicePoint);
		when(saleRepository.saveAndFlush(any(Sale.class))).thenReturn(sale);
		Sale expectedSale = saleService.create(saleRequest);

		// then
		assertEquals(saleId, (long) expectedSale.getId());
	}

	@Test
	public void findByServicePointShouldReturnOneSale() {
		//given
		final long saleId = 1;
		final long servicePointId = 1;
		final ServicePoint servicePoint = ServicePoint.builder().id(servicePointId).build();
		final Sale sale = Sale.builder().id(saleId).servicePoint(servicePoint).build();

		// when
		when(saleRepository.findByServicePoint(any(ServicePoint.class))).thenReturn(List.of(sale));
		List<Sale> expectedSales = saleService.findByServicePoint(servicePoint);

		// then
		assertEquals(1, expectedSales.size());
		assertEquals(saleId, (long) expectedSales.get(0).getId());
		assertEquals(servicePointId, (long) expectedSales.get(0).getServicePoint().getId());
	}

	@Test
	public void updateShouldReturnNullForNonExistentSale() {
		// given
		final long saleId = 1;
		final SaleRequest saleRequest = new SaleRequest(
				saleId,
				1L,
				new BigDecimal(23),
				new Timestamp(System.currentTimeMillis())
		);

		//when
		when(saleRepository.findById(any())).thenReturn(Optional.empty());
		final Sale expectedSale = saleService.update(saleRequest);

		// then
		assertNull(expectedSale);
	}

	@Test
	public void updateShouldReturnTheUpdatedSale() {
		// given
		final long saleId = 1;
		final long servicePointId = 1;
		final ServicePoint servicePoint = ServicePoint.builder().id(servicePointId).build();
		final Sale sale = Sale.builder().id(saleId).servicePoint(servicePoint).build();
		final SaleRequest saleRequest = new SaleRequest(
				saleId,
				servicePointId,
				new BigDecimal(23),
				new Timestamp(System.currentTimeMillis())
		);

		//when
		when(saleRepository.findById(any())).thenReturn(Optional.of(sale));
		when(servicePointService.findServicePointBy(any())).thenReturn(servicePoint);
		when(saleRepository.saveAndFlush(any(Sale.class))).thenReturn(sale);
		final Sale expectedSale = saleService.update(saleRequest);

		// then
		assertNotNull(expectedSale);
		assertEquals(saleId, (long) expectedSale.getId());
		assertEquals(servicePointId, (long) expectedSale.getServicePoint().getId());
	}

	@Test
	public void deleteShouldDeleteOneItem() {
		// given
		final Sale sale = new Sale();
		List<SaledItem> saledItems = List.of(new SaledItem(), new SaledItem());

		// when
		when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));
		when(saledItemRepository.findBySale(any(Sale.class))).thenReturn(saledItems);
		saleService.delete(1L);

		// then
		verify(saledItemRepository, times(saledItems.size())).delete(any(SaledItem.class));
		verify(saleRepository, times(1)).delete(any(Sale.class));
	}

	@Test
	public void itShouldNotDeleteForNonExistentSale() {
		// given

		// when
		when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());
		saleService.delete(1L);

		// then
		verify(saledItemRepository, times(0)).findBySale(any(Sale.class));
		verify(saledItemRepository, times(0)).delete(any(SaledItem.class));
		verify(saleRepository, times(0)).delete(any(Sale.class));
	}

}
