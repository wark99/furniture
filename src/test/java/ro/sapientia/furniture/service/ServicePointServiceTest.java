package ro.sapientia.furniture.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.sapientia.furniture.exception.RecordNotFoundException;
import ro.sapientia.furniture.model.ServicePoint;
import ro.sapientia.furniture.model.dto.ServicePointRequest;
import ro.sapientia.furniture.repository.ServicePointRepository;
import ro.sapientia.furniture.service.impl.ServicePointServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ro.sapientia.furniture.mocking.ServicePointDatabaseBuilder.buildTestServicePoints;

@ExtendWith(MockitoExtension.class)
public class ServicePointServiceTest {

    @Mock
    private ServicePointRepository servicePointRepository;
    @Mock
    private RegionService regionService;
    @InjectMocks
    private ServicePointServiceImpl underTest;

    @Test
    public void testFindServicePointsShouldFail() {
        when(servicePointRepository.findAll()).thenReturn(List.of());

        assertThrows(
            RecordNotFoundException.class,
            () -> underTest.findServicePoints());
    }

    @Test
    public void testFindServicePointsShouldSucceed() {
        when(servicePointRepository.findAll()).thenReturn(buildTestServicePoints());

        final var servicePointList = underTest.findServicePoints();

        assertEquals(2, servicePointList.size());
    }

    @Test
    public void testFindServicePointByShouldFail() {
        when(servicePointRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
            RecordNotFoundException.class,
            () -> underTest.findServicePointBy(1L));
    }

    @Test
    public void testFindServicePointByShouldSucceed() {
        final var expectedServicePoint = buildTestServicePoints().get(0);
        when(servicePointRepository.findById(anyLong())).thenReturn(Optional.of(expectedServicePoint));

        final var servicePoint = underTest.findServicePointBy(1L);

        assertNotNull(servicePoint.getId());
        assertNotNull(servicePoint.getRegion().getId());
        assertNotNull(expectedServicePoint.getRegion().getName(), servicePoint.getRegion().getName());
        assertNotNull(expectedServicePoint.getCountry(), servicePoint.getCountry());
    }

    @Test
    public void testCreate() {
        final var servicePointRequest = ServicePointRequest.builder()
            .regionId(1L)
            .country("Test Country 1")
            .build();
        when(servicePointRepository.saveAndFlush(any(ServicePoint.class))).thenReturn(buildTestServicePoints().get(0));

        final var servicePoint = underTest.create(servicePointRequest);

        verify(servicePointRepository, times(1)).saveAndFlush(any(ServicePoint.class));
        assertNotNull(servicePoint.getRegion());
        assertNotNull(servicePoint.getCountry());
    }

    @Test
    public void testUpdateShouldFail() {
        final var servicePointRequest = ServicePointRequest.builder()
            .id(1L)
            .regionId(1L)
            .country("Test Country 1")
            .build();

        assertThrows(
            RecordNotFoundException.class,
            () -> underTest.update(servicePointRequest));

        verify(servicePointRepository, times(0)).saveAndFlush(any(ServicePoint.class));
    }

    @Test
    public void testUpdateShouldSucceed() {
        final var servicePointRequest = ServicePointRequest.builder()
            .id(1L)
            .regionId(1L)
            .country("Test Country 1")
            .build();
        final var servicePoint = buildTestServicePoints().get(0);
        when(servicePointRepository.findById(anyLong())).thenReturn(Optional.of(servicePoint));

        underTest.update(servicePointRequest);

        verify(servicePointRepository, times(1)).saveAndFlush(any(ServicePoint.class));
    }

    @Test
    public void testDeleteRegionByShouldFail() {
        when(servicePointRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
            RecordNotFoundException.class,
            () -> underTest.deleteServicePointBy(1L));

        verify(servicePointRepository, times(0)).deleteById(anyLong());
    }

    @Test
    public void testDeleteRegionByShouldSucceed() {
        final var expectedServicePoint = buildTestServicePoints().get(0);
        when(servicePointRepository.findById(anyLong())).thenReturn(Optional.of(expectedServicePoint));

        underTest.deleteServicePointBy(1L);

        verify(servicePointRepository, times(1)).deleteById(anyLong());
    }
}
