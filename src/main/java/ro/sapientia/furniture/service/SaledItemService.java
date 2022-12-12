package ro.sapientia.furniture.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ro.sapientia.furniture.model.Sale;
import ro.sapientia.furniture.model.SaledItem;
import ro.sapientia.furniture.model.dto.SaledItemRequest;
import ro.sapientia.furniture.repository.SaledItemRepository;

@Service
@RequiredArgsConstructor
public class SaledItemService {

	private final SaledItemRepository saledItemRepository;
	private final SaleService saleService;

	public List<SaledItem> findAll() {
		return this.saledItemRepository.findAll();
	}

	public List<SaledItem> findBySale(final Sale sale) {
		return this.saledItemRepository.findBySale(sale);
	}

	public SaledItem findById(final Long id) {
		return this.saledItemRepository.findById(id).orElse(null);
	}

	public SaledItem create(SaledItemRequest saledItemRequest) {
		final SaledItem saledItem =  SaledItem.builder()
				.sale(saleService.findById(saledItemRequest.saleId()))
				.furnitureId(saledItemRequest.furnitureId())
				.quantity(saledItemRequest.quantity())
				.price(saledItemRequest.price())
				.timestamp(saledItemRequest.timestamp())
				.build();
		return this.saledItemRepository.saveAndFlush(saledItem);
	}

	public SaledItem update(SaledItemRequest saledItemRequest) {
		final SaledItem existingSaledItem = findById(saledItemRequest.id());
		if (existingSaledItem == null) return null;
		final SaledItem saledItem =  SaledItem.builder()
				.id(existingSaledItem.getId())
				.sale(saleService.findById(saledItemRequest.saleId()))
				.furnitureId(saledItemRequest.furnitureId())
				.quantity(saledItemRequest.quantity())
				.price(saledItemRequest.price())
				.timestamp(saledItemRequest.timestamp())
				.build();
		return this.saledItemRepository.saveAndFlush(saledItem);
	}

	public void delete(Long id) {
		SaledItem saledItem = findById(id);
		if (saledItem != null) {
			this.saledItemRepository.delete(saledItem);
		}
	}

}
