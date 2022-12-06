package ro.sapientia.furniture.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.sapientia.furniture.exception.RecordNotFoundException;
import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.dto.RegionRequest;
import ro.sapientia.furniture.repository.RegionRepository;
import ro.sapientia.furniture.repository.ServicePointRepository;
import ro.sapientia.furniture.service.impl.RegionServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ro.sapientia.furniture.mocking.RegionDatabaseBuilder.buildTestRegions;
import static ro.sapientia.furniture.mocking.ServicePointDatabaseBuilder.buildTestServicePoints;

@ExtendWith(MockitoExtension.class)
public class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private ServicePointRepository servicePointRepository;

    @InjectMocks
    private RegionServiceImpl underTest;

    @Test
    public void testFindRegionsShouldFail() {
        when(regionRepository.findAll()).thenReturn(List.of());

        assertThrows(
            RecordNotFoundException.class,
            () -> underTest.findRegions());
    }

    @Test
    public void testFindRegionsShouldSucceed() {
        when(regionRepository.findAll()).thenReturn(buildTestRegions());

        final var regionList = underTest.findRegions();

        assertEquals(2, regionList.size());
    }

    @Test
    public void testFindRegionByShouldFail() {
        when(regionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
            RecordNotFoundException.class,
            () -> underTest.findRegionBy(1L));
    }

    @Test
    public void testFindRegionByShouldSucceed() {
        final var expectedRegion = buildTestRegions().get(0);
        when(regionRepository.findById(anyLong())).thenReturn(Optional.of(expectedRegion));

        final var region = underTest.findRegionBy(1L);

        assertNotNull(region.getId());
        assertNotNull(region.getName());
        assertNotNull(expectedRegion.getName(), region.getName());
    }

    @Test
    public void testCreate() {
        final var regionRequest = RegionRequest.builder()
            .name("Test Region 1")
            .build();
        when(regionRepository.saveAndFlush(any(Region.class))).thenReturn(buildTestRegions().get(0));

        final var region = underTest.create(regionRequest);

        verify(regionRepository, times(1)).saveAndFlush(any(Region.class));
        assertNotNull(region.getName());
        assertEquals(regionRequest.getName(), region.getName());
    }

    @Test
    public void testUpdateShouldFail() {
        final var region = buildTestRegions().get(0);
        when(regionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
            RecordNotFoundException.class,
            () -> underTest.update(region));

        verify(regionRepository, times(0)).saveAndFlush(any(Region.class));
    }

    @Test
    public void testUpdateShouldSucceed() {
        final var region = buildTestRegions().get(0);
        when(regionRepository.findById(anyLong())).thenReturn(Optional.of(region));

        underTest.update(region);

        verify(regionRepository, times(1)).saveAndFlush(any(Region.class));
    }

    @Test
    public void testDeleteRegionByShouldFail() {
        when(regionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
            RecordNotFoundException.class,
            () -> underTest.deleteRegionBy(1L));

        verify(servicePointRepository, times(0)).deleteAll(anyCollection());
        verify(regionRepository, times(0)).deleteById(anyLong());
    }

    @Test
    public void testDeleteRegionByShouldSucceed() {
        when(regionRepository.findById(anyLong())).thenReturn(Optional.of(buildTestRegions().get(0)));
        when(servicePointRepository.findAllByRegion(any(Region.class))).thenReturn(buildTestServicePoints());

        underTest.deleteRegionBy(1L);

        verify(servicePointRepository, times(1)).deleteAll(anyCollection());
        verify(regionRepository, times(1)).deleteById(anyLong());
    }
}
