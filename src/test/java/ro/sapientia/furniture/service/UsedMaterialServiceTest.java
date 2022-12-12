package ro.sapientia.furniture.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.sapientia.furniture.exception.MaterialNotFoundException;
import ro.sapientia.furniture.exception.UsedMaterialNotFoundException;
import ro.sapientia.furniture.mocking.MaterialsDatabaseBuilder;
import ro.sapientia.furniture.mocking.UsedMaterialDatabaseBuilder;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.UsedMaterial;
import ro.sapientia.furniture.model.dto.UsedMaterialRequest;
import ro.sapientia.furniture.repository.UsedMaterialRepository;


public class UsedMaterialServiceTest {

    private UsedMaterialRepository usedMaterialRepository;

    private MaterialsService materialService;

    private UsedMaterialService usedMaterialService;

    @BeforeEach
    public void setUp() {
        usedMaterialRepository = mock(UsedMaterialRepository.class);
        materialService = mock(MaterialsService.class);
        usedMaterialService = new UsedMaterialService(usedMaterialRepository, materialService);
    }

    @Test
    public void findAllShouldReturnAnEmptyList() {
        when(usedMaterialService.findAll()).thenReturn(Collections.emptyList());
        final List<UsedMaterial> expectedUsedMaterials = usedMaterialService.findAll();

        assertEquals(0, expectedUsedMaterials.size());
    }

    @Test
    public void findByIdShouldThrowExceptionForNonExistentUsedMaterial() {

        when(usedMaterialRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(UsedMaterialNotFoundException.class, () -> usedMaterialService.findById(10L));
    }

    @Test
    public void findByMaterialShouldReturnOneUsedMaterial() {

        final long usedMaterialID = 1;
        final long materialID = 1;
        final Material material = Material.builder().id(materialID).build();
        final UsedMaterial usedMaterial = UsedMaterial.builder().id(usedMaterialID).material(material).build();

        when(usedMaterialRepository.findByMaterial(any(Material.class))).thenReturn(List.of(usedMaterial));
        List<UsedMaterial> expectedUsedMaterials = usedMaterialService.findByMaterial(material);

        assertEquals(1, expectedUsedMaterials.size());
        assertEquals(usedMaterialID, (long) expectedUsedMaterials.get(0).getId());
        assertEquals(materialID, (long) expectedUsedMaterials.get(0).getMaterial().getId());
    }

    @Test
    public void createShouldReturnTheCreatedUsedMaterial() {
        final long usedMaterialID = 1;
        final long materialID = 1;

        final UsedMaterialRequest usedMaterialRequest = new UsedMaterialRequest(
                usedMaterialID,
                materialID,
                (long) 1,
                1,
                new BigDecimal(23),
                new Timestamp(System.currentTimeMillis())
        );

        final Material material = Material.builder().id(materialID).build();
        final UsedMaterial usedMaterial = UsedMaterial.builder().id(usedMaterialID).material(material).furnitureId(1L).build();

        when(materialService.getMaterialById(materialID)).thenReturn(material);
        when(usedMaterialRepository.saveAndFlush(any(UsedMaterial.class))).thenReturn(usedMaterial);
        UsedMaterial expectedUsedMaterial = usedMaterialService.create(usedMaterialRequest);

        assertEquals(usedMaterial, expectedUsedMaterial);
    }

    @Test
    public void updateShouldThrowExceptionForNonExistentUsedMaterial() {
        final long usedMaterialID = 1;
        final UsedMaterialRequest usedMaterialRequest = new UsedMaterialRequest(
                usedMaterialID,
                (long) 1,
                (long) 1,
                1,
                new BigDecimal(23),
                new Timestamp(System.currentTimeMillis())
        );

        when(usedMaterialRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(UsedMaterialNotFoundException.class, () -> usedMaterialService.update(usedMaterialRequest));
        verify(usedMaterialRepository, times(0)).saveAndFlush(any(UsedMaterial.class));
    }

    @Test
    public void updateShouldReturnTheUpdatedUsedMaterial() {
        final long usedMaterialID = 1;
        final long materialID = 1;

        final Material material = Material.builder().id(materialID).build();
        final UsedMaterial usedMaterial = UsedMaterial.builder().id(usedMaterialID).material(material).build();
        final UsedMaterialRequest usedMaterialRequest = new UsedMaterialRequest(
                usedMaterialID,
                materialID,
                (long) 1,
                1,
                new BigDecimal(23),
                new Timestamp(System.currentTimeMillis())
        );

        when(usedMaterialRepository.findById(any())).thenReturn(Optional.of(usedMaterial));
        when(materialService.getMaterialById(any())).thenReturn(material);
        when(usedMaterialRepository.saveAndFlush(any(UsedMaterial.class))).thenReturn(usedMaterial);
        final UsedMaterial expectedUsedMaterial = usedMaterialService.update(usedMaterialRequest);

        assertNotNull(expectedUsedMaterial);
        assertEquals(usedMaterialID, (long) expectedUsedMaterial.getId());
        assertEquals(materialID, (long) expectedUsedMaterial.getMaterial().getId());
    }

    @Test
    public void deleteShouldDeleteOneUsedMaterial() {
        final UsedMaterial usedMaterial = UsedMaterialDatabaseBuilder.buildTestUsedMaterials().get(0);

        when(usedMaterialRepository.findById(anyLong())).thenReturn(Optional.of(usedMaterial));
        usedMaterialService.delete(1L);

        verify(usedMaterialRepository, times(1)).deleteById(anyLong());

    }

    @Test
    public void shouldNotDeleteForNonExistentUsedMaterial() {
        when(usedMaterialRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(UsedMaterialNotFoundException.class, () -> usedMaterialService.delete(1L));
        verify(usedMaterialRepository, times(0)).deleteById(anyLong());
    }
}
