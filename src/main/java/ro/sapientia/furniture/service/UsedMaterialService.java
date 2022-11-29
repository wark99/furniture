package ro.sapientia.furniture.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.UsedMaterial;
import ro.sapientia.furniture.model.dto.UsedMaterialRequest;
import ro.sapientia.furniture.repository.UsedMaterialRepository;


import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsedMaterialService {
    private final UsedMaterialRepository usedMaterialRepository;
    private final MaterialsService materialsService;

    public List<UsedMaterial> findAll() {

        return this.usedMaterialRepository.findAll();
    }

    public List<UsedMaterial> findByMaterial(final Material material) {
        return this.usedMaterialRepository.findByMaterial(material);
    }

    public UsedMaterial findById(final Long id) {

        return this.usedMaterialRepository.findById(id).orElse(null);
    }

    public UsedMaterial create(UsedMaterialRequest usedMaterialRequest) {
        final UsedMaterial usedMaterial =  UsedMaterial.builder()
                .material(materialsService.getMaterialById(usedMaterialRequest.materialId()))
                .furnitureId(usedMaterialRequest.furnitureId())
                .quantity(usedMaterialRequest.quantity())
                .price(usedMaterialRequest.price())
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        return this.usedMaterialRepository.saveAndFlush(usedMaterial);
    }

    public UsedMaterial update(UsedMaterialRequest usedMaterialRequest) {
        final UsedMaterial existingUsedMaterial = findById(usedMaterialRequest.id());
        if (existingUsedMaterial == null) {
            return null;
        }

        final UsedMaterial usedMaterial =  UsedMaterial.builder()
                .id(existingUsedMaterial.getId())
                .material(materialsService.getMaterialById(usedMaterialRequest.materialId()))
                .furnitureId(usedMaterialRequest.furnitureId())
                .quantity(usedMaterialRequest.quantity())
                .price(usedMaterialRequest.price())
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        return this.usedMaterialRepository.saveAndFlush(usedMaterial);
    }

    public void delete(Long id) {

        this.usedMaterialRepository.deleteById(id);
    }

}
