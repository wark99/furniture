package ro.sapientia.furniture.service;

import java.util.List;

import ro.sapientia.furniture.model.SaledItem;
import ro.sapientia.furniture.repository.SaledItemRepository;

public class SaledItemService {

private final SaledItemRepository saledItemRepository;
	
	public SaledItemService(final SaledItemRepository saledItemRepository) {
		this.saledItemRepository = saledItemRepository;
	}
	
	public List<SaledItem> findAll() {
		return this.saledItemRepository.findAll();
	}

	public SaledItem findById(final Long id) {
		return this.saledItemRepository.findById(id).orElse(null);
	}

	public SaledItem create(SaledItem saledItem) {
		return this.saledItemRepository.saveAndFlush(saledItem);
	}

	public SaledItem update(SaledItem saledItem) {
		return this.saledItemRepository.saveAndFlush(saledItem);
	}

	public void delete(Long id) {
		this.saledItemRepository.deleteById(id);
	}
	
}
