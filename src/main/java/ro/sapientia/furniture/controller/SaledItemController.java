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
import ro.sapientia.furniture.model.SaledItem;
import ro.sapientia.furniture.model.dto.SaledItemRequest;
import ro.sapientia.furniture.service.SaledItemService;

@RestController
@RequestMapping("/saled-items")
@RequiredArgsConstructor
public class SaledItemController {

	private final SaledItemService saledItemService;

	@GetMapping("/all")
	public ResponseEntity<List<SaledItem>> getAllSaledItems() {
		final List<SaledItem> saledItems = saledItemService.findAll();
		return new ResponseEntity<>(saledItems, HttpStatus.OK);
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<SaledItem> getSaledItemById(@PathVariable("id") Long id) {
		final SaledItem saledItem = saledItemService.findById(id);
		if (saledItem == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(saledItem, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<SaledItem> createSaledItem(@RequestBody SaledItemRequest saledItemRequest) {
		final SaledItem persistentSaledItem = saledItemService.create(saledItemRequest);
		return new ResponseEntity<>(persistentSaledItem, HttpStatus.OK);
	}

	@PostMapping("/update")
	public ResponseEntity<SaledItem> updateSaledItem(@RequestBody SaledItemRequest saledItemRequest) {
		final SaledItem persistentSaledItem = saledItemService.update(saledItemRequest);
		return new ResponseEntity<>(persistentSaledItem, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteSaledItemById(@PathVariable("id") Long id) {
		saledItemService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
