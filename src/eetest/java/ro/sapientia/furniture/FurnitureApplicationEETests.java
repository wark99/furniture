package ro.sapientia.furniture;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ro.sapientia.furniture.model.FurnitureBody;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestEntityManager
@TestPropertySource(locations = "classpath:eetest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class FurnitureApplicationEETests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestEntityManager entityManager;

    private void addOneElement() {
        final FurnitureBody body = new FurnitureBody();
        body.setHeigth(10);
        entityManager.persist(body);
        entityManager.flush();
    }

    @Test
    void furnitureAll_oneElement() throws Exception {
        addOneElement();
        mvc.perform(get("/furniture/all")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].heigth", is(10)));
    }

    @Test
    public void testGetRegionsShouldFail() throws Exception {
        mvc.perform(get("/regions"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("Region table is empty!")));
    }

    @Test
    public void testGetServicePointsShouldFail() throws Exception {
        mvc.perform(get("/service_points"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("ServicePoint table is empty!")));
    }

    @Test
    public void testGetAllMaterialsShouldReturnEmptyList() throws Exception {
        mvc.perform(get("/materials/getAllMaterials"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }
}
