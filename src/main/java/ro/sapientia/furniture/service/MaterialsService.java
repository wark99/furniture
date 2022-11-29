package ro.sapientia.furniture.service;

import ro.sapientia.furniture.model.dto.MaterialDTO;
import ro.sapientia.furniture.model.Material;

import java.util.List;

public interface MaterialsService {
    List<Material> getAllMaterials();
    Material getMaterialById(Long id);
    Material createMaterial(MaterialDTO materialDTO);
    void updateMaterial(Material material);
    void deleteMaterialById(Long id);
}
