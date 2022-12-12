package ro.sapientia.furniture.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.sapientia.furniture.exception.MaterialNotFoundException;
import ro.sapientia.furniture.mocking.MaterialsDatabaseBuilder;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.dto.MaterialRequest;
import ro.sapientia.furniture.repository.MaterialsRepository;
import ro.sapientia.furniture.repository.UsedMaterialRepository;
import ro.sapientia.furniture.service.impl.MaterialsServiceImpl;
import ro.sapientia.furniture.utils.CheckComponentsEquality;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MaterialsServiceTest {

    private MaterialsRepository materialsRepository;

    private ServicePointService servicePointService;

    private MaterialsService materialsService;

    private UsedMaterialRepository usedMaterialRepository;

    @BeforeEach
    public void setUp() {
        materialsRepository = mock(MaterialsRepository.class);
        servicePointService = mock(ServicePointService.class);
        usedMaterialRepository = mock(UsedMaterialRepository.class);
        materialsService = new MaterialsServiceImpl(materialsRepository, servicePointService, usedMaterialRepository);
    }

    @Test
    public void shouldGetAllMaterialsReturnEmptyList() {
        when(materialsRepository.findAll()).thenReturn(Collections.emptyList());
        var allMaterials = materialsService.getAllMaterials();

        Assertions.assertEquals(0, allMaterials.size());
    }

    @Test
    public void shouldGetAllMaterialsWorkingCorrectly() {
        when(materialsRepository.findAll()).thenReturn(MaterialsDatabaseBuilder.buildTestMaterials());
        var allMaterials =  materialsService.getAllMaterials();

        Assertions.assertEquals(2, allMaterials.size());
    }

    @Test
    public void testGetMaterialByIdWithInvalidId() {
        when(materialsRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(MaterialNotFoundException.class, () -> materialsService.getMaterialById(10L));
    }

    @Test
    public void testGetMaterialByIdWithValidId() {
        var expectedMaterial = MaterialsDatabaseBuilder.buildTestMaterials().get(0);
        when(materialsRepository.findById(anyLong())).thenReturn(Optional.of(expectedMaterial));

        var material = materialsService.getMaterialById(1L);

        Assertions.assertNotNull(material);
        CheckComponentsEquality.assertMaterialEquals(expectedMaterial, material);
    }

    @Test
    public void testGetMaterialByServicePointId() {
        var expectedMaterial = MaterialsDatabaseBuilder.buildTestMaterials().get(1);
        when(materialsRepository.getByServicePointId(anyLong())).thenReturn(List.of(expectedMaterial));

        var materials = materialsService.getByServicePointId(2L);

        Assertions.assertEquals(1, materials.size());
        Assertions.assertEquals(expectedMaterial.getId(), materials.get(0).getId());
        Assertions.assertEquals(expectedMaterial.getServicePoint().getId(), materials.get(0).getServicePoint().getId());
    }

    @Test
    public void testCreateMaterial() {
        var materialRequest = MaterialRequest.builder()
                .servicePointId(1L)
                .name("Material Name 1")
                .origin("Material Origin 1")
                .build();

        Assertions.assertEquals(0, materialsRepository.findAll().size());

        when(materialsRepository.saveAndFlush(any(Material.class))).thenReturn(MaterialsDatabaseBuilder.buildTestMaterials().get(0));

        var material = materialsService.createMaterial(materialRequest);

        Assertions.assertNotNull(material.getName());
        Assertions.assertNotNull(material.getOrigin());
    }

    @Test
    public void testUpdateMaterialWithInvalidId() {
        var materialRequest = MaterialRequest.builder()
                .id(1L)
                .servicePointId(1L)
                .name("Material Name 1")
                .build();

        when(materialsRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(MaterialNotFoundException.class, () -> materialsService.updateMaterial(materialRequest));
        verify(materialsRepository, times(0)).saveAndFlush(any(Material.class));
    }

    @Test
    public void testUpdateMaterialWithValidId() {
        var materialRequest = MaterialRequest.builder()
                .id(1L)
                .servicePointId(1L)
                .name("Material Name 1")
                .build();

        var material = MaterialsDatabaseBuilder.buildTestMaterials().get(0);
        when(materialsRepository.findById(anyLong())).thenReturn(Optional.of(material));

        materialsService.updateMaterial(materialRequest);
        verify(materialsRepository, times(1)).saveAndFlush(any(Material.class));
    }

    @Test
    public void testDeleteMaterialByIdInvalidId() {
        when(materialsRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(MaterialNotFoundException.class, () -> materialsService.deleteMaterialById(1L));
        verify(materialsRepository, times(0)).deleteById(anyLong());
    }

    @Test
    public void testDeleteMaterialByIdWithValidId() {
        var material = MaterialsDatabaseBuilder.buildTestMaterials().get(0);
        when(materialsRepository.findById(anyLong())).thenReturn(Optional.of(material));

        materialsService.deleteMaterialById(1L);
        verify(materialsRepository, times(1)).deleteById(anyLong());
    }
}
