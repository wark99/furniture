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

import ro.sapientia.furniture.model.SaledItem;
import ro.sapientia.furniture.service.SaledItemService;

@RestController
@RequestMapping("/saled-items")
public class SaledItemController {

	private final SaledItemService saledItemService;
	
	public SaledItemController(SaledItemService saledItemService) {
		this.saledItemService = saledItemService;
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<SaledItem>> getAllSaledItems() {
		final List<SaledItem> saledItems = saledItemService.findAll();
		return new ResponseEntity<>(saledItems, HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<SaledItem> getSaledItemById(@PathVariable("id") Long id) {
		final SaledItem saledItem = saledItemService.findById(id);
		return new ResponseEntity<>(saledItem, HttpStatus.OK);
	}
	
	@PostMapping("/create")
	public ResponseEntity<SaledItem> createSaledItem(@RequestBody SaledItem saledItem) {
		final SaledItem persistentSaledItem = saledItemService.create(saledItem);
		return new ResponseEntity<>(persistentSaledItem, HttpStatus.OK);
	}
	
	@PostMapping("/update")
	public ResponseEntity<SaledItem> updateSaledItem(@RequestBody SaledItem saledItem) {
		final SaledItem persistentSaledItem = saledItemService.update(saledItem);
		return new ResponseEntity<>(persistentSaledItem, HttpStatus.OK);
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deleteSaledItemById(@PathVariable("id") Long id) {
		saledItemService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
