package ro.sapientia.furniture.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ro.sapientia.furniture.exception.MaterialNotFoundException;
import ro.sapientia.furniture.mocking.MaterialsDatabaseBuilder;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.dto.MaterialRequest;
import ro.sapientia.furniture.service.MaterialsService;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MaterialsController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class MaterialsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(MaterialsService.class)
    private MaterialsService materialsService;

    @Test
    public void shouldGetAllMaterialsReturnMaterials() throws Exception {
        var materials = MaterialsDatabaseBuilder.buildTestMaterials();

        when(materialsService.getAllMaterials()).thenReturn(materials);

        this.mockMvc.perform(get("/materials/getAllMaterials"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{}, {}]"));
    }

    @Test
    public void shouldGetAllMaterialsReturnEmptyList() throws Exception {
        when(materialsService.getAllMaterials()).thenReturn(List.of());

        this.mockMvc.perform(get("/materials/getAllMaterials"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    public void shouldGetMaterialByIdReturnMaterial() throws Exception{
        var materials = MaterialsDatabaseBuilder.buildTestMaterials();

        when(materialsService.getMaterialById(anyLong())).thenReturn(materials.get(0));

        this.mockMvc.perform(get("/materials/getMaterial/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(content().json("{}"));
    }

    @Test
    public void shouldGetMaterialByIdFail() throws Exception{
        when(materialsService.getMaterialById(1L)).thenThrow(new MaterialNotFoundException(Material.class, Map.of("id", "1L")));

        this.mockMvc.perform(get("/materials/getMaterial/1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MaterialNotFoundException));
    }

    @Test
    public void shouldCreateMaterialWorkProperly() throws Exception{
        final ObjectMapper objectMapper = new ObjectMapper();
        var material = MaterialsDatabaseBuilder.buildTestMaterials().get(0);
        var materialRequest = MaterialRequest.builder()
                .servicePointId(1L)
                .name("Material Name 1")
                .origin("Material Origin 1")
                .unit("Material Unit 1")
                .unitPrice(10.0)
                .quantity(1.0)
                .quality("Material Quality 1")
                .build();
        when(materialsService.createMaterial(any(MaterialRequest.class))).thenReturn(material);

        this.mockMvc.perform(post("/materials/createMaterial")
                        .content(objectMapper.writeValueAsString(materialRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(content().json("{}"));

        verify(materialsService, times(1)).createMaterial(any(MaterialRequest.class));
    }

    @Test
    public void shouldUpdateMaterialWorkCorrectly() throws Exception{
        final ObjectMapper objectMapper = new ObjectMapper();
        var material = MaterialRequest.builder()
                .id(1L)
                .servicePointId(1L)
                .name("Material Name 2")
                .origin("Material Origin 1")
                .unit("Material Unit 2")
                .unitPrice(10.0)
                .quantity(2.0)
                .quality("Material Quality 2")
                .build();

        doNothing().when(materialsService).updateMaterial(any(MaterialRequest.class));

        this.mockMvc.perform(post("/materials/updateMaterial")
                        .content(objectMapper.writeValueAsString(material))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    public void shouldUpdateMaterialFail() throws Exception{
        final ObjectMapper objectMapper = new ObjectMapper();
        doThrow(new MaterialNotFoundException(Material.class, Map.of("id", "1L"))).when(materialsService).updateMaterial(null);

        this.mockMvc.perform(post("/materials/updateMaterial")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldDeleteMaterialByIdSucceed() throws Exception{
        this.mockMvc.perform(delete("/materials/deleteMaterial/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteMaterialByIdFail() throws Exception{
        doThrow(new MaterialNotFoundException(Material.class, Map.of("id", "1L"))).when(materialsService).deleteMaterialById(anyLong());
        this.mockMvc.perform(delete("/materials/deleteMaterial/1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MaterialNotFoundException));
    }
}
