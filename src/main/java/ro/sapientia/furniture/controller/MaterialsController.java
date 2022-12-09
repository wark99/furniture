package ro.sapientia.furniture.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.sapientia.furniture.model.dto.MaterialRequest;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.service.MaterialsService;

import java.util.List;

@RestController
@RequestMapping("/materials")
@RequiredArgsConstructor
public class MaterialsController {
    private final MaterialsService materialsService;

    @GetMapping("/getAllMaterials")
    public ResponseEntity<List<Material>> getAllMaterials() {
        var materials = materialsService.getAllMaterials();
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @GetMapping("/getMaterial/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable("id") final Long id) {
        var material = materialsService.getMaterialById(id);
        return new ResponseEntity<>(material, HttpStatus.OK);
    }

    @PostMapping("/createMaterial")
    public ResponseEntity<Material> createMaterial(@RequestBody final MaterialRequest materialRequest) {
        var createdMaterial =  materialsService.createMaterial(materialRequest);
        return new ResponseEntity<>(createdMaterial, HttpStatus.OK);
    }

    @PostMapping("/updateMaterial")
    public ResponseEntity<?> updateMaterial(@RequestBody final MaterialRequest materialRequest) {
        materialsService.updateMaterial(materialRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteMaterial/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable("id") final Long id) {
        materialsService.deleteMaterialById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
