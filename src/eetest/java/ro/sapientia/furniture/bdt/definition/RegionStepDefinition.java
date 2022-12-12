package ro.sapientia.furniture.bdt.definition;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ro.sapientia.furniture.model.Region;

import java.util.Map;

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
@ContextConfiguration
@AutoConfigureTestEntityManager
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:eetest.properties")
public class RegionStepDefinition {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestEntityManager entityManager;

    @Given("^that we have the following regions:$")
    public void that_we_have_the_following_region(final DataTable regions) throws Throwable {
        for (final Map<String, String> data : regions.asMaps(String.class, String.class)) {
            final var region = new Region();
            region.setName(data.get("name"));
            entityManager.persist(region);
        }
        entityManager.flush();
    }

    @Then("^I should get \"([^\"]*)\" error for find all$")
    public void I_should_get_RECORD_NOT_FOUND_error_for_find_all(final String expectedResult) throws Throwable {
        mvc.perform(get("/regions").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("error", is(expectedResult)));
    }

    @Then("^I should get the name \"([^\"]*)\" for the position \"([^\"]*)\"$")
    public void I_should_get_result_in_stories_list(final String expectedResult, final String expectedPosition) throws Throwable {
        mvc.perform(get("/regions").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[" + expectedPosition + "].name", is(expectedResult)));
    }

    @Then("^I should get \"([^\"]*)\" error for find by \"([^\"]*)\"")
    public void I_should_get_RECORD_NOT_FOUND_error_for_find_by(final String expectedResult, final long id) throws Throwable {
        mvc.perform(get("/regions/" + id).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("error", is(expectedResult)));
    }

    @Then("^I should get the name \"([^\"]*)\" for find by \"([^\"]*)\"$")
    public void I_should_get_name_for_find_by(final String expectedResult, final long id) throws Throwable {
        mvc.perform(get("/regions/" + id).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("name", is(expectedResult)));
    }

    @Then("^I should get no error for adding \"([^\"]*)\"$")
    public void I_should_get_no_error_for_add(final String name) throws Throwable {
        mvc.perform(post("/regions/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"" + name + "\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("name", is(name)));
    }

    @Then("^I should get \"([^\"]*)\" error for updating \"([^\"]*)\" at position \"([^\"]*)\"$")
    public void I_should_get_RECORD_NOT_FOUND_error_for_updating(
        final String expectedResult, final String name, final Long id
    ) throws Exception {
        mvc.perform(post("/regions/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": " + id + ", \"name\": \"" + name + "\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("error", is(expectedResult)));
    }

    @Then("^I should get no error for updating \"([^\"]*)\" at position \"([^\"]*)\"$")
    public void I_should_get_no_error_for_updating(final String name, final Long id) throws Exception {
        mvc.perform(post("/regions/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": " + id + ", \"name\": \"" + name + "\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Then("^I should get \"([^\"]*)\" error for deleting element at position \"([^\"]*)\"$")
    public void I_should_get_RECORD_NOT_FOUND_error_for_deleting(final String expectedResult, final long id) throws Exception {
        mvc.perform(get("/regions/delete/"+id))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("error", is(expectedResult)));
    }

    @Then("^I should get no error for deleting element at position \"([^\"]*)\"$")
    public void I_should_get_no_error_for_updating(final Long id) throws Exception {
        mvc.perform(get("/regions/delete/" + id))
            .andExpect(status().isOk());
    }
}
