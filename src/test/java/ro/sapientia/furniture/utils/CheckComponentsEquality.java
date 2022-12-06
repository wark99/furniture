package ro.sapientia.furniture.utils;

import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Component;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.ServicePoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Component
public class CheckComponentsEquality {
    public static void assertMaterialEquals(Material expectedMaterial, Material materialFromRepository) {
        Assertions.assertNotNull(materialFromRepository.getServicePoint());
        assertServicePointEquals(expectedMaterial.getServicePoint(), materialFromRepository.getServicePoint());
        Assertions.assertNotNull(materialFromRepository.getName());
        Assertions.assertEquals(expectedMaterial.getName(), materialFromRepository.getName());
        Assertions.assertNotNull(materialFromRepository.getOrigin());
        Assertions.assertEquals(expectedMaterial.getOrigin(), materialFromRepository.getOrigin());
        Assertions.assertNotNull(materialFromRepository.getUnit());
        Assertions.assertEquals(expectedMaterial.getUnit(), materialFromRepository.getUnit());
        Assertions.assertNotNull(materialFromRepository.getUnitPrice());
        Assertions.assertEquals(expectedMaterial.getUnitPrice(), materialFromRepository.getUnitPrice());
        Assertions.assertNotNull(materialFromRepository.getQuantity());
        Assertions.assertEquals(expectedMaterial.getQuantity(), materialFromRepository.getQuantity());
        Assertions.assertNotNull(materialFromRepository.getQuality());
        Assertions.assertEquals(expectedMaterial.getQuality(), materialFromRepository.getQuality());
    }

    public static void assertServicePointEquals(final ServicePoint servicePointExpectedResult, final ServicePoint servicePointResult) {
        assertNotNull(servicePointResult.getId());
        assertNotNull(servicePointResult.getRegion());
        assertNotNull(servicePointResult.getRegion().getId());
        assertNotNull(servicePointResult.getRegion().getName());
        assertEquals(servicePointExpectedResult.getRegion().getName(), servicePointResult.getRegion().getName());
        assertNotNull(servicePointResult.getCountry());
        assertEquals(servicePointExpectedResult.getCountry(), servicePointResult.getCountry());
        assertNotNull(servicePointResult.getCounty());
        assertEquals(servicePointExpectedResult.getCounty(), servicePointResult.getCounty());
        assertNotNull(servicePointResult.getCity());
        assertEquals(servicePointExpectedResult.getCity(), servicePointResult.getCity());
        assertNotNull(servicePointResult.getStreet());
        assertEquals(servicePointExpectedResult.getStreet(), servicePointResult.getStreet());
        assertNotNull(servicePointResult.getNumber());
        assertEquals(servicePointExpectedResult.getNumber(), servicePointResult.getNumber());
        assertNotNull(servicePointResult.getZipCode());
        assertEquals(servicePointExpectedResult.getZipCode(), servicePointResult.getZipCode());
    }
}
