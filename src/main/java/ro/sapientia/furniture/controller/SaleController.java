package ro.sapientia.furniture.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.sapientia.furniture.model.Sale;
import ro.sapientia.furniture.service.SaleService;

@RestController
@RequestMapping("/sales")
public class SaleController {

	private final SaleService saleService;
	
	public SaleController(SaleService saleService) {
		this.saleService = saleService;
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Sale>> getAllSales() {
		final List<Sale> sales = saleService.findAll();
		return new ResponseEntity<>(sales, HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<Sale> getSaleById(@PathVariable("id") Long id) {
		final Sale sale = saleService.findById(id);
		return new ResponseEntity<>(sale, HttpStatus.OK);
	}
	
	@PostMapping("/create")
	public ResponseEntity<Sale> createSale(@RequestBody Sale sale) {
		final Sale persistentSale = saleService.create(sale);
		return new ResponseEntity<>(persistentSale, HttpStatus.OK);
	}
	
	@PostMapping("/update")
	public ResponseEntity<Sale> updateSale(@RequestBody Sale sale) {
		final Sale persistentSale = saleService.update(sale);
		return new ResponseEntity<>(persistentSale, HttpStatus.OK);
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deleteSaleById(@PathVariable("id") Long id) {
		saleService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
