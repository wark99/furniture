package ro.sapientia.furniture.service;

import org.springframework.stereotype.Service;
import ro.sapientia.furniture.model.FurnitureBody;
import ro.sapientia.furniture.repository.FurnitureBodyRepository;

import java.util.List;

@Service
public class FurnitureBodyService {

    private final FurnitureBodyRepository furnitureBodyRepository;

    public FurnitureBodyService(final FurnitureBodyRepository furnitureBodyRepository) {
        this.furnitureBodyRepository = furnitureBodyRepository;
    }

    public List<FurnitureBody> findAllFurnitureBodies() {
        return this.furnitureBodyRepository.findAll();
    }

    public FurnitureBody findFurnitureBodyById(final Long id) {
        return this.furnitureBodyRepository.findFurnitureBodyById(id);
    }

    public FurnitureBody create(final FurnitureBody furnitureBody) {
        return this.furnitureBodyRepository.saveAndFlush(furnitureBody);
    }

    public FurnitureBody update(final FurnitureBody furnitureBody) {
        return this.furnitureBodyRepository.saveAndFlush(furnitureBody);
    }

    public void delete(final Long id) {
        this.furnitureBodyRepository.deleteById(id);
    }

}
