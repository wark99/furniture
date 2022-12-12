package ro.sapientia.furniture.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.sapientia.furniture.exception.UsedMaterialNotFoundException;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.UsedMaterial;
import ro.sapientia.furniture.model.dto.UsedMaterialRequest;
import ro.sapientia.furniture.repository.UsedMaterialRepository;


import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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
        var currentUsedMaterial = usedMaterialRepository.findById(id);
        if (currentUsedMaterial.isEmpty()) {
            throw new UsedMaterialNotFoundException(UsedMaterial.class, Map.of("id", id.toString()));
        }
        return currentUsedMaterial.get();
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
        var existingUsedMaterial = usedMaterialRepository.findById(usedMaterialRequest.id());
        if (existingUsedMaterial.isEmpty()) {
            throw new UsedMaterialNotFoundException(UsedMaterial.class, Map.of("id", usedMaterialRequest.id().toString()));
        }

        var updatedUsedMaterial = UsedMaterial.builder()
                .id(existingUsedMaterial.get().getId())
                .material(materialsService.getMaterialById(usedMaterialRequest.materialId()))
                .furnitureId(usedMaterialRequest.furnitureId())
                .quantity(usedMaterialRequest.quantity())
                .price(usedMaterialRequest.price())
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        return this.usedMaterialRepository.saveAndFlush(updatedUsedMaterial);
    }

    public void delete(Long id) {
        var currentUsedMaterial = usedMaterialRepository.findById(id);
        if (currentUsedMaterial.isEmpty()) {
            throw new UsedMaterialNotFoundException(UsedMaterial.class, Map.of("id", id.toString()));
        }

        this.usedMaterialRepository.deleteById(currentUsedMaterial.get().getId());
    }

}
