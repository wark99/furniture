package ro.sapientia.furniture;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import ro.sapientia.furniture.mocking.RegionDatabaseBuilder;
import ro.sapientia.furniture.service.RegionService;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
@AutoConfigureTestEntityManager
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:eetest.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RegionEETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RegionService regionService;

    @BeforeAll
    public static void setup(@Autowired final RegionDatabaseBuilder regionDatabaseBuilder) {
        regionDatabaseBuilder.build();
    }

    @AfterAll
    public static void dispose(@Autowired final RegionDatabaseBuilder regionDatabaseBuilder) {
        regionDatabaseBuilder.clean();
    }

    @Test
    @Order(1)
    public void testGetRegionsShouldSucceed() throws Exception {
        mockMvc.perform(get("/regions"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].name", is("Test Region 1")))
            .andExpect(jsonPath("$[1].name", is("Test Region 2")));
    }

    @Test
    @Order(2)
    public void testGetRegionByShouldFail() throws Exception {
        mockMvc.perform(get("/regions/99"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("Record not found in table: Region")))
            .andExpect(jsonPath("params.id", is("99")));
    }

    @Test
    @Order(3)
    public void testGetRegionByShouldSucceed() throws Exception {
        final var regionIdInDB = regionService.findRegions().get(0).getId();

        mockMvc.perform(get("/regions/" + regionIdInDB))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("id", is(regionIdInDB.intValue())))
            .andExpect(jsonPath("name", is("Test Region 1")));
    }

    @Test
    @Order(4)
    public void testAddRegionShouldSucceed() throws Exception {
        mockMvc.perform(post("/regions/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Test Region 3\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("name", is("Test Region 3")));
    }

    @Test
    @Order(5)
    public void testUpdateRegionShouldFail() throws Exception {
        mockMvc.perform(post("/regions/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 88, \"name\": \"Test Region 1\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("Record not found in table: Region")))
            .andExpect(jsonPath("params.id", is("88")));
    }

    @Test
    @Order(6)
    public void testUpdateRegionShouldSucceed() throws Exception {
        final var regionIdInDB = regionService.findRegions().get(0).getId();

        mockMvc.perform(post("/regions/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": " + regionIdInDB + ", \"name\": \"Test Region 3\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void testDeleteRegionByShouldFail() throws Exception {
        mockMvc.perform(get("/regions/delete/99"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("Record not found in table: Region")))
            .andExpect(jsonPath("params.id", is("99")));
    }

    @Test
    @Order(8)
    public void testDeleteRegionByShouldSucceed() throws Exception {
        final var regionIdInDB = regionService.findRegions().get(0).getId();

        mockMvc.perform(get("/regions/delete/" + regionIdInDB))
            .andExpect(status().isOk());
    }
}
