package ro.sapientia.furniture;

import org.junit.jupiter.api.*;
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
import ro.sapientia.furniture.exception.UsedMaterialNotFoundException;
import ro.sapientia.furniture.mocking.MaterialsDatabaseBuilder;
import ro.sapientia.furniture.mocking.UsedMaterialDatabaseBuilder;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.UsedMaterial;
import ro.sapientia.furniture.model.dto.UsedMaterialRequest;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestEntityManager
@TestPropertySource(locations = "classpath:eetest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UsedMaterialEETest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeAll
    public static void setUp(@Autowired final UsedMaterialDatabaseBuilder usedMaterialDatabaseBuilder) {
        usedMaterialDatabaseBuilder.build();
    }

    @AfterAll
    public static void cleanUp(@Autowired final UsedMaterialDatabaseBuilder usedMaterialDatabaseBuilder) {
        usedMaterialDatabaseBuilder.clean();
    }

    @Test
    public void testGetAllUsedMaterialsShouldRun() throws Exception {
        mockMvc.perform(get("/used_materials"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{}, {}]"))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].material.name", is("Material Name 1")))
                .andExpect(jsonPath("$[0].furnitureId", is(1)))
                .andExpect(jsonPath("$[0].quantity", is(10)));
    }

    @Test
    public void testGetUsedMaterialByIdShouldRun() throws Exception {
        mockMvc.perform(get("/used_materials/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{}"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.material.id", is(1)))
                .andExpect(jsonPath("$.furnitureId", is(1)))
                .andExpect(jsonPath("$.quantity", is(10)));;
    }

    @Test
    public void testGetUsedMaterialByIdShouldFail() throws Exception {
        mockMvc.perform(get("/used_materials/10"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof UsedMaterialNotFoundException));
    }

    @Test
    public void testCreateUsedMaterial() throws Exception {
        mockMvc.perform(post("/used_materials/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\": 1000,\n" +
                            "    \"materialId\": 1,\n" +
                            "    \"furnitureId\": 1,\n" +
                            "    \"quantity\": 10,\n" +
                            "    \"price\": 100\n}")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.material.id", is(1)))
                .andExpect(jsonPath("$.furnitureId", is(1)))
                .andExpect(jsonPath("$.quantity", is(10)))
                .andExpect(jsonPath("$.price", is(100)))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(content().json("{}"));
    }

    @Test
    public void testUpdateUsedMaterialShouldRun() throws Exception {
        mockMvc.perform(post("/used_materials/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1,\n" +
                                "    \"materialId\": 1,\n" +
                                "    \"furnitureId\": 1,\n" +
                                "    \"quantity\": 50,\n" +
                                "    \"price\": 500\n}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateUsedMaterialShouldFail() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        var usedMaterial = UsedMaterial.builder()
                .id(100L)
                .material(Material.builder().id(1L).build())
                .furnitureId(1L)
                .quantity(500)
                .price(new BigDecimal(50))
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();

        mockMvc.perform(post("/used_materials/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usedMaterial))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof UsedMaterialNotFoundException));
    }

    @Test
    public void testDeleteUsedMaterialByIdShouldRun() throws Exception {
        mockMvc.perform(delete("/used_materials/delete/2"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUsedMaterialByIdShouldFail() throws Exception {
        mockMvc.perform(delete("/used_materials/delete/50"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", is(400)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof UsedMaterialNotFoundException));
    }
}
