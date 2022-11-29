package ro.sapientia.furniture.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.sapientia.furniture.model.UsedMaterials;
import ro.sapientia.furniture.service.UsedMaterialsService;

import java.util.List;

@RestController
@RequestMapping("/used_materials")
public class UsedMaterialsController {
    private final UsedMaterialsService usedMaterialsService;

    public UsedMaterialsController(final UsedMaterialsService usedMaterialsService) {
        this.usedMaterialsService = usedMaterialsService;
    }

    @GetMapping()
    public ResponseEntity<List<UsedMaterials>> getAllUsedMaterials(){
        final List<UsedMaterials> usedMaterials = usedMaterialsService.findAllUsedMaterials();
        return new ResponseEntity<>(usedMaterials, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsedMaterials> getUsedMaterialById(@PathVariable("id") Long id){
        final UsedMaterials usedMaterial = usedMaterialsService.findUsedMaterialsById(id);
        return new ResponseEntity<>(usedMaterial,HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<UsedMaterials> addUsedMaterial(@RequestBody UsedMaterials usedMaterial){
        final UsedMaterials persistentUsedMaterial = usedMaterialsService.create(usedMaterial);
        return new ResponseEntity<>(persistentUsedMaterial,HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<UsedMaterials> updateUsedMaterial(@RequestBody UsedMaterials usedMaterial){
        final UsedMaterials persistentUsedMaterial = usedMaterialsService.update(usedMaterial);
        return new ResponseEntity<>(usedMaterial,HttpStatus.OK);
    }

    @GetMapping("delete/{id}")
    public ResponseEntity<?> deleteUsedMaterialsById(@PathVariable("id") Long id){
        usedMaterialsService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
