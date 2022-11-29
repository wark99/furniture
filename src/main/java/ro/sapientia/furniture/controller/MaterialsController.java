package ro.sapientia.furniture.controller;

import org.springframework.web.bind.annotation.*;
import ro.sapientia.furniture.model.dto.MaterialDTO;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.service.MaterialsService;

import java.util.List;

@RestController
@RequestMapping("/materials")
public class MaterialsController {
    private final MaterialsService materialsService;

    public MaterialsController(MaterialsService materialsService) {
        this.materialsService = materialsService;
    }

    @GetMapping
    public List<Material> getAllMaterials() {
        return materialsService.getAllMaterials();
    }

    @GetMapping("/{id}")
    public Material getMaterialById(@PathVariable("id") final Long id) {
        return materialsService.getMaterialById(id);
    }

    @PostMapping("/createMaterial")
    public Material createMaterial(@RequestBody final MaterialDTO materialDTO) {
        return materialsService.createMaterial(materialDTO);
    }

    @PostMapping("/updateMaterial")
    public void updateMaterial(@RequestBody final Material material) {
        materialsService.updateMaterial(material);
    }

    @DeleteMapping("/{id}/deleteMaterial")
    public void deleteMaterial(@PathVariable("id") final Long id) {
        materialsService.deleteMaterialById(id);
    }
}
