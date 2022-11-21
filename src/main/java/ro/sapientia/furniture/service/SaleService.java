package ro.sapientia.furniture.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ro.sapientia.furniture.model.Sale;
import ro.sapientia.furniture.repository.SaleRepository;

@Service
public class SaleService {
	
	private final SaleRepository saleRepository;
	
	public SaleService(final SaleRepository saleRepository) {
		this.saleRepository = saleRepository;
	}
	
	public List<Sale> findAll() {
		return this.saleRepository.findAll();
	}
	
	public List<Sale> findByServicePoint(ServicePoint servicePoint) {
		return this.saleRepository.findByServicePoint(servicePoint);
	}	

	public Sale findById(final Long id) {
		return this.saleRepository.findById(id).orElse(null);
	}

	public Sale create(Sale sale) {
		return this.saleRepository.saveAndFlush(sale);
	}

	public Sale update(Sale sale) {
		return this.saleRepository.saveAndFlush(sale);
	}

	public void delete(Long id) {
		this.saleRepository.deleteById(id);
	}
	
}
