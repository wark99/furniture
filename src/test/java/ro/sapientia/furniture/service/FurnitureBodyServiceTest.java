package ro.sapientia.furniture.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ro.sapientia.furniture.model.FurnitureBody;
import ro.sapientia.furniture.repository.FurnitureBodyRepository;

public class FurnitureBodyServiceTest {

	private FurnitureBodyRepository repositoryMock;

	private FurnitureBodyService service;

	@BeforeEach
	public void setUp() {
		repositoryMock = mock(FurnitureBodyRepository.class);
		service = new FurnitureBodyService(repositoryMock);
	}

	@Test
	public void testFindAllFurnitureBodies_emptyList() {
		when(repositoryMock.findAll()).thenReturn(Collections.emptyList());
		final List<FurnitureBody> furnitureBodies =  service.findAllFurnitureBodies();
		
		assertEquals(0, furnitureBodies.size());
	}

	@Test
	public void testFindAllFurnitureBodies_null() {
		when(repositoryMock.findAll()).thenReturn(null);
		final List<FurnitureBody> furnitureBodies =  service.findAllFurnitureBodies();
		
		assertNull(furnitureBodies);
	}

}
