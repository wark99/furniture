package ro.sapientia.furniture.service;

import org.springframework.stereotype.Service;
import ro.sapientia.furniture.repository.UsedMaterialsRepository;
import ro.sapientia.furniture.model.UsedMaterials;

import java.util.List;

@Service
public class UsedMaterialsService {
    private final UsedMaterialsRepository usedMaterialsRepository;

    public UsedMaterialsService(final UsedMaterialsRepository usedMaterialsRepository) {
        this.usedMaterialsRepository = usedMaterialsRepository;
    }

    public List<UsedMaterials> findAllUsedMaterials() {

        return this.usedMaterialsRepository.findAll();
    }

    public UsedMaterials findUsedMaterialsById(final Long id) {
        return this.usedMaterialsRepository.findUsedMaterialsById(id);
    }

    public UsedMaterials create(final UsedMaterials usedMaterial) {
        return this.usedMaterialsRepository.saveAndFlush(usedMaterial);
    }

    public UsedMaterials update(final UsedMaterials usedMaterial) {
        return this.usedMaterialsRepository.saveAndFlush(usedMaterial);
    }

    public void delete(final Long id) {

        this.usedMaterialsRepository.deleteById(id);
    }
}
