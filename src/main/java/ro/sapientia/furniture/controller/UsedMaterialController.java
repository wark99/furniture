package ro.sapientia.furniture.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.sapientia.furniture.model.UsedMaterial;
import ro.sapientia.furniture.model.dto.UsedMaterialRequest;
import ro.sapientia.furniture.service.UsedMaterialService;

import java.util.List;

@RestController
@RequestMapping("/used_materials")
@RequiredArgsConstructor
public class UsedMaterialController {
    private final UsedMaterialService usedMaterialService;

    @GetMapping()
    public ResponseEntity<List<UsedMaterial>> getAllUsedMaterials() {
        final List<UsedMaterial> usedMaterials = usedMaterialService.findAll();
        return new ResponseEntity<>(usedMaterials, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsedMaterial> getUsedMaterialById(@PathVariable("id") Long id) {
        final UsedMaterial usedMaterial = usedMaterialService.findById(id);
        if (usedMaterial == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usedMaterial, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<UsedMaterial> createUsedMaterial(@RequestBody UsedMaterialRequest usedMaterialRequest) {
        final UsedMaterial persistentUsedMaterial = usedMaterialService.create(usedMaterialRequest);
        return new ResponseEntity<>(persistentUsedMaterial, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<UsedMaterial> updateUsedMaterial(@RequestBody UsedMaterialRequest usedMaterialRequest) {
        final UsedMaterial persistentUsedMaterial = usedMaterialService.update(usedMaterialRequest);
        return new ResponseEntity<>(persistentUsedMaterial, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUsedMaterialById(@PathVariable("id") Long id) {
        usedMaterialService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
