package ro.sapientia.furniture.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ro.sapientia.furniture.model.Sale;
import ro.sapientia.furniture.model.SaledItem;
import ro.sapientia.furniture.model.ServicePoint;
import ro.sapientia.furniture.model.dto.SaleRequest;
import ro.sapientia.furniture.repository.SaleRepository;
import ro.sapientia.furniture.repository.SaledItemRepository;

@Service
@RequiredArgsConstructor
public class SaleService {

	private final SaleRepository saleRepository;
	private final SaledItemRepository saledItemRepository;
	private final ServicePointService servicePointService;

	public List<Sale> findAll() {
		return this.saleRepository.findAll();
	}

	public List<Sale> findByServicePoint(ServicePoint servicePoint) {
		return this.saleRepository.findByServicePoint(servicePoint);
	}

	public Sale findById(final Long id) {
		return this.saleRepository.findById(id).orElse(null);
	}

	public Sale create(SaleRequest saleRequest) {
		final Sale sale =  Sale.builder()
				.servicePoint(servicePointService.findServicePointBy(saleRequest.getServicePointId()))
				.totalPrice(saleRequest.getTotalPrice())
				.saledDate(saleRequest.getSaledDate())
				.build();
		return this.saleRepository.saveAndFlush(sale);
	}

	public Sale update(SaleRequest saleRequest) {
		final Sale existingSale = findById(saleRequest.getId());
		if (existingSale == null) return null;
		final Sale sale =  Sale.builder()
				.id(existingSale.getId())
				.servicePoint(servicePointService.findServicePointBy(saleRequest.getServicePointId()))
				.totalPrice(saleRequest.getTotalPrice())
				.saledDate(saleRequest.getSaledDate())
				.build();
		return this.saleRepository.saveAndFlush(sale);
	}

	public void delete(Long id) {
		final Sale sale = findById(id);
		if (sale != null) {
			for (SaledItem saledItem : saledItemRepository.findBySale(sale)) {
				saledItemRepository.delete(saledItem);
			}
			this.saleRepository.delete(sale);
		}
	}

}
