package ro.sapientia.furniture.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ro.sapientia.furniture.model.Sale;
import ro.sapientia.furniture.model.dto.SaleRequest;
import ro.sapientia.furniture.service.SaleService;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SaleController {

	private final SaleService saleService;

	@GetMapping("/all")
	public ResponseEntity<List<Sale>> getAllSales() {
		final List<Sale> sales = saleService.findAll();
		return new ResponseEntity<>(sales, HttpStatus.OK);
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<?> getSaleById(@PathVariable("id") Long id) {
		final Sale sale = saleService.findById(id);
		if (sale == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(sale, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<Sale> createSale(@RequestBody SaleRequest saleRequest) {
		final Sale persistentSale = saleService.create(saleRequest);
		return new ResponseEntity<>(persistentSale, HttpStatus.OK);
	}

	@PostMapping("/update")
	public ResponseEntity<?> updateSale(@RequestBody SaleRequest saleRequest) {
		final Sale persistentSale = saleService.update(saleRequest);
		if (persistentSale == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(persistentSale, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteSaleById(@PathVariable("id") Long id) {
		saleService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
