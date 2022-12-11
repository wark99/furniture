package ro.sapientia.furniture.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.sapientia.furniture.exception.MaterialNotFoundException;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.UsedMaterial;
import ro.sapientia.furniture.model.dto.MaterialRequest;
import ro.sapientia.furniture.repository.MaterialsRepository;
import ro.sapientia.furniture.repository.UsedMaterialRepository;
import ro.sapientia.furniture.service.MaterialsService;
import ro.sapientia.furniture.service.ServicePointService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MaterialsServiceImpl implements MaterialsService {
    private final MaterialsRepository materialsRepository;
    private final ServicePointService servicePointService;
    private final UsedMaterialRepository usedMaterialRepository;

    @Override
    public List<Material> getAllMaterials() {
        return materialsRepository.findAll();
    }

    @Override
    public List<Material> getByServicePointId(final Long id) {
        return materialsRepository.getByServicePointId(id);
    }

    @Override
    public Material getMaterialById(final Long id) {
        var currentMaterial = materialsRepository.findById(id);
        if (currentMaterial.isEmpty()) {
            throw new MaterialNotFoundException(Material.class, Map.of("id", id.toString()));
        }
        return currentMaterial.get();
    }

    @Override
    public Material createMaterial(final MaterialRequest materialRequest) {
        var createdMaterial = Material.builder()
                .servicePoint(servicePointService.findServicePointBy(materialRequest.getServicePointId()))
                .name(materialRequest.getName())
                .origin(materialRequest.getOrigin())
                .unit(materialRequest.getUnit())
                .unitPrice(materialRequest.getUnitPrice())
                .quantity(materialRequest.getQuantity())
                .quality(materialRequest.getQuality())
                .build();
        return materialsRepository.saveAndFlush(createdMaterial);
    }

    @Override
    public void updateMaterial(final MaterialRequest materialRequest) {
        var existingMaterial = materialsRepository.findById(materialRequest.getId());
        if (existingMaterial.isEmpty()) {
            throw new MaterialNotFoundException(Material.class, Map.of("id", materialRequest.getId().toString()));
        }

        var updatedMaterial = Material.builder()
                .id(existingMaterial.get().getId())
                .servicePoint(servicePointService.findServicePointBy(materialRequest.getServicePointId()))
                .name(materialRequest.getName())
                .origin(materialRequest.getOrigin())
                .unit(materialRequest.getUnit())
                .unitPrice(materialRequest.getUnitPrice())
                .quantity(materialRequest.getQuantity())
                .quality(materialRequest.getQuality())
                .build();
        materialsRepository.saveAndFlush(updatedMaterial);
    }

    @Override
    public void deleteMaterialById(final Long id) {
        var currentMaterial = materialsRepository.findById(id);
        if (currentMaterial.isEmpty()) {
            throw new MaterialNotFoundException(Material.class, Map.of("id", id.toString()));
        }

        for(UsedMaterial usedMaterial : usedMaterialRepository.findByMaterial(currentMaterial.get())) {
            usedMaterialRepository.delete(usedMaterial);
        }
        materialsRepository.deleteById(currentMaterial.get().getId());
    }
}
