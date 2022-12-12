package ro.sapientia.furniture;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ro.sapientia.furniture.exception.MaterialNotFoundException;
import ro.sapientia.furniture.mocking.MaterialsDatabaseBuilder;
import ro.sapientia.furniture.model.dto.MaterialRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestEntityManager
@TestPropertySource(locations = "classpath:eetest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MaterialsEETests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeAll
    public static void setUp(@Autowired final MaterialsDatabaseBuilder materialsDatabaseBuilder) {
        materialsDatabaseBuilder.build();
    }

    @AfterAll
    public static void cleanUp(@Autowired final MaterialsDatabaseBuilder materialsDatabaseBuilder) {
        materialsDatabaseBuilder.clean();
    }

    @Test
    public void testGetAllMaterialsShouldRunProperly() throws Exception {
        mockMvc.perform(get("/materials/getAllMaterials"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{}, {}]"))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].servicePoint.country", is("Test Country 1")))
                .andExpect(jsonPath("$[0].name", is("Material Name 1")))
                .andExpect(jsonPath("$[0].origin", is("Material Origin 1")))
                .andExpect(jsonPath("$[0].unit", is("Material Unit 1")))
                .andExpect(jsonPath("$[0].unitPrice", is(10.0)))
                .andExpect(jsonPath("$[0].quantity", is(1.0)))
                .andExpect(jsonPath("$[0].quality", is("Material Quality 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].servicePoint.county", is("Test County 2")))
                .andExpect(jsonPath("$[1].name", is("Material Name 2")))
                .andExpect(jsonPath("$[1].origin", is("Material Origin 2")))
                .andExpect(jsonPath("$[1].unit", is("Material Unit 2")))
                .andExpect(jsonPath("$[1].unitPrice", is(20.0)))
                .andExpect(jsonPath("$[1].quantity", is(2.0)))
                .andExpect(jsonPath("$[1].quality", is("Material Quality 2")));
    }

    @Test
    public void testGetMaterialByIdShouldRunCorrectly() throws Exception {
        mockMvc.perform(get("/materials/getMaterial/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{}"))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.servicePoint.country", is("Test Country 2")))
                .andExpect(jsonPath("$.name", is("Material Name 2")))
                .andExpect(jsonPath("$.origin", is("Material Origin 2")))
                .andExpect(jsonPath("$.unit", is("Material Unit 2")))
                .andExpect(jsonPath("$.unitPrice", is(20.0)))
                .andExpect(jsonPath("$.quantity", is(2.0)))
                .andExpect(jsonPath("$.quality", is("Material Quality 2")));
    }

    @Test
    public void testGetMaterialByIdShouldFail() throws Exception {
        mockMvc.perform(get("/materials/getMaterial/10"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MaterialNotFoundException));
    }

    @Test
    public void testCreateMaterial() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        var materialRequest = MaterialRequest.builder()
                .servicePointId(1L)
                .name("Material Name 10")
                .origin("Material Origin 10")
                .unit("Material Unit 10")
                .unitPrice(10.0)
                .quantity(1.0)
                .quality("Material Quality 10")
                .build();
        mockMvc.perform(post("/materials/createMaterial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materialRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Material Name 10")))
                .andExpect(jsonPath("$.origin", is("Material Origin 10")))
                .andExpect(jsonPath("$.unit", is("Material Unit 10")))
                .andExpect(jsonPath("$.unitPrice", is(10.0)))
                .andExpect(jsonPath("$.quantity", is(1.0)))
                .andExpect(jsonPath("$.quality", is("Material Quality 10")))
                .andExpect(content().json("{}"));
    }

    @Test
    public void testUpdateMaterialShouldRunCorrectly() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        var materialRequest = MaterialRequest.builder()
                .id(2L)
                .servicePointId(1L)
                .name("Material Name 55")
                .origin("Material Origin 1")
                .unit("Material Unit 55")
                .unitPrice(50.0)
                .quantity(2.0)
                .quality("Material Quality 9")
                .build();

        mockMvc.perform(post("/materials/updateMaterial")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(materialRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateMaterialShouldFail() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        var materialRequest = MaterialRequest.builder()
                .id(100L)
                .servicePointId(1L)
                .name("Material Name 55")
                .origin("Material Origin 1")
                .unit("Material Unit 55")
                .unitPrice(50.0)
                .quantity(2.0)
                .quality("Material Quality 9")
                .build();

        mockMvc.perform(post("/materials/updateMaterial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materialRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MaterialNotFoundException));
    }

    @Test
    public void testDeleteMaterialByIdShouldRunCorrectly() throws Exception {
        mockMvc.perform(delete("/materials/deleteMaterial/2"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteMaterialByIdShouldFail() throws Exception {
        mockMvc.perform(delete("/materials/deleteMaterial/33"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", is(400)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MaterialNotFoundException));
    }

}
