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
import ro.sapientia.furniture.mocking.ServicePointDatabaseBuilder;

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
public class ServicePointEETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeAll
    public static void setupAll(@Autowired final ServicePointDatabaseBuilder servicePointDatabaseBuilder) {
        servicePointDatabaseBuilder.build();
    }


    @AfterAll
    public static void dispose(@Autowired final ServicePointDatabaseBuilder servicePointDatabaseBuilder) {
        servicePointDatabaseBuilder.clean();
    }

    @Test
    @Order(1)
    public void testGetServicePointsShouldSucceed() throws Exception {
        mockMvc.perform(get("/service_points"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].region.id", is(1)))
            .andExpect(jsonPath("$[0].region.name", is("Test Region 1")))
            .andExpect(jsonPath("$[0].country", is("Test Country 1")))
            .andExpect(jsonPath("$[0].county", is("Test County 1")))
            .andExpect(jsonPath("$[0].city", is("Test City 1")))
            .andExpect(jsonPath("$[0].street", is("Test Street 1")))
            .andExpect(jsonPath("$[0].number", is("Test Number 1")))
            .andExpect(jsonPath("$[0].zipCode", is("Test Zip Code 1")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].region.id", is(2)))
            .andExpect(jsonPath("$[1].region.name", is("Test Region 2")))
            .andExpect(jsonPath("$[1].country", is("Test Country 2")))
            .andExpect(jsonPath("$[1].county", is("Test County 2")))
            .andExpect(jsonPath("$[1].city", is("Test City 2")))
            .andExpect(jsonPath("$[1].street", is("Test Street 2")))
            .andExpect(jsonPath("$[1].number", is("Test Number 2")))
            .andExpect(jsonPath("$[1].zipCode", is("Test Zip Code 2")));
    }


    @Test
    @Order(2)
    public void testGetServicePointByShouldFail() throws Exception {
        mockMvc.perform(get("/service_points/3"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("Record not found in table: ServicePoint")))
            .andExpect(jsonPath("params.id", is("3")));
    }

    @Test
    @Order(3)
    public void testGetServicePointByShouldSucceed() throws Exception {
        mockMvc.perform(get("/service_points/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("id", is(1)))
            .andExpect(jsonPath("region.id", is(1)))
            .andExpect(jsonPath("region.name", is("Test Region 1")))
            .andExpect(jsonPath("country", is("Test Country 1")))
            .andExpect(jsonPath("county", is("Test County 1")))
            .andExpect(jsonPath("city", is("Test City 1")))
            .andExpect(jsonPath("street", is("Test Street 1")))
            .andExpect(jsonPath("number", is("Test Number 1")))
            .andExpect(jsonPath("zipCode", is("Test Zip Code 1")));
    }

    @Test
    @Order(4)
    public void testAddServicePointShouldSucceed() throws Exception {
        mockMvc.perform(post("/service_points/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1,\n" +
                    "    \"regionId\": 1,\n" +
                    "    \"country\": \"Test Country 1\",\n" +
                    "    \"county\": \"Test County 1\",\n" +
                    "    \"city\": \"Test City 1\",\n" +
                    "    \"street\": \"Test Street 1\",\n" +
                    "    \"number\": \"Test Number 1\",\n" +
                    "    \"zipCode\": \"Test Zip Code 1\"\n}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("id", is(3)))
            .andExpect(jsonPath("region.id", is(1)))
            .andExpect(jsonPath("region.name", is("Test Region 1")))
            .andExpect(jsonPath("country", is("Test Country 1")))
            .andExpect(jsonPath("county", is("Test County 1")))
            .andExpect(jsonPath("city", is("Test City 1")))
            .andExpect(jsonPath("street", is("Test Street 1")))
            .andExpect(jsonPath("number", is("Test Number 1")))
            .andExpect(jsonPath("zipCode", is("Test Zip Code 1")));
    }

    @Test
    @Order(5)
    public void testUpdateServicePointShouldFail() throws Exception {
        mockMvc.perform(post("/service_points/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 54,\n" +
                    "    \"regionId\": 3,\n" +
                    "    \"country\": \"Asd\",\n" +
                    "    \"county\": \"Qwe\",\n" +
                    "    \"city\": \"Zxc\",\n" +
                    "    \"street\": \"RT\",\n" +
                    "    \"number\": \"11\",\n" +
                    "    \"zipCode\": \"12-S\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("Record not found in table: ServicePoint")))
            .andExpect(jsonPath("params.id", is("54")));
    }

    @Test
    @Order(6)
    public void testUpdateServicePointShouldSucceed() throws Exception {
        mockMvc.perform(post("/service_points/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1,\n" +
                    "    \"regionId\": 2,\n" +
                    "    \"country\": \"Asd\",\n" +
                    "    \"county\": \"Qwe\",\n" +
                    "    \"city\": \"Zxc\",\n" +
                    "    \"street\": \"RT\",\n" +
                    "    \"number\": \"11\",\n" +
                    "    \"zipCode\": \"12-S\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void testDeleteServicePointByShouldFail() throws Exception {
        mockMvc.perform(get("/service_points/delete/3"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("Record not found in table: ServicePoint")))
            .andExpect(jsonPath("params.id", is("3")));
    }

    @Test
    @Order(8)
    public void testDeleteServicePointByShouldSucceed() throws Exception {
        mockMvc.perform(get("/service_points/delete/1"))
            .andExpect(status().isOk());
    }
}
