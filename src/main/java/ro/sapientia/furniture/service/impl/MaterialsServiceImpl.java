package ro.sapientia.furniture.service.impl;


import org.springframework.stereotype.Service;
import ro.sapientia.furniture.exception.MaterialNotFoundException;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.dto.MaterialDTO;
import ro.sapientia.furniture.repository.MaterialsRepository;
import ro.sapientia.furniture.service.MaterialsService;
import ro.sapientia.furniture.service.ServicePointService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MaterialsServiceImpl implements MaterialsService {
    private final MaterialsRepository materialsRepository;
    private final ServicePointService servicePointService;

    public MaterialsServiceImpl(final MaterialsRepository materialsRepository,
                                final ServicePointService servicePointService) {
        this.materialsRepository = materialsRepository;
        this.servicePointService = servicePointService;
    }


    @Override
    public List<Material> getAllMaterials() {
        if (Objects.equals(materialsRepository.findAll().size(), 0)) {
            throw new MaterialNotFoundException(Material.class);
        }
        return materialsRepository.findAll();
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
    public Material createMaterial(final MaterialDTO materialDTO) {
        var createdMaterial = Material.builder()
                .servicePoint(servicePointService.findServicePointBy(materialDTO.getServicePointId()))
                .name(materialDTO.getName())
                .origin(materialDTO.getOrigin())
                .unit(materialDTO.getUnit())
                .unitPrice(materialDTO.getUnitPrice())
                .quantity(materialDTO.getQuantity())
                .quality(materialDTO.getQuality())
                .build();
        return materialsRepository.save(createdMaterial);
    }

    @Override
    public void updateMaterial(final Material material) {
        var existingMaterial = getMaterialById(material.getId());

        var updatedMaterial = Material.builder()
                .id(existingMaterial.getId())
                .servicePoint(servicePointService.findServicePointBy(material.getServicePoint().getId()))
                .name(material.getName())
                .origin(material.getOrigin())
                .unit(material.getUnit())
                .unitPrice(material.getUnitPrice())
                .quantity(material.getQuantity())
                .quality(material.getQuality())
                .build();
        materialsRepository.save(updatedMaterial);
    }

    @Override
    public void deleteMaterialById(final Long id) {
        var currentMaterial = getMaterialById(id);
        materialsRepository.deleteById(currentMaterial.getId());
    }
}
