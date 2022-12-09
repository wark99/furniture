package ro.sapientia.furniture.service;

import ro.sapientia.furniture.model.dto.MaterialRequest;
import ro.sapientia.furniture.model.Material;

import java.util.List;

public interface MaterialsService {
    List<Material> getAllMaterials();
    List<Material> getByServicePointId(Long id);
    Material getMaterialById(Long id);
    Material createMaterial(MaterialRequest materialRequest);
    void updateMaterial(MaterialRequest materialRequest);
    void deleteMaterialById(Long id);
}
