package ro.sapientia.furniture.bdt.definition;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
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
import ro.sapientia.furniture.exception.MaterialNotFoundException;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.ServicePoint;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
@ContextConfiguration
public class MaterialStepDefinition {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestEntityManager entityManager;

    @Given("^that we have the following materials:$")
    public void that_we_have_the_following_materials(final DataTable materials) {
        for (final Map<String, String> data: materials.asMaps(String.class, String.class)) {
            var region = new Region();
            region.setName(data.get("regionName"));
            entityManager.persist(region);
            var servicePoint = new ServicePoint();
            servicePoint.setRegion(region);
            servicePoint.setCity(data.get("servicePointCityName"));
            entityManager.persist(servicePoint);
            var material = new Material();
            material.setServicePoint(servicePoint);
            material.setName(data.get("name"));
            material.setOrigin(data.get("origin"));
            material.setUnit(data.get("unit"));
            material.setUnitPrice(Double.valueOf(data.get("unitPrice")));
            material.setQuantity(Double.valueOf(data.get("quantity")));
            material.setQuality(data.get("quality"));
            entityManager.persist(material);
        }
        entityManager.flush();
    }

    @Then("^I should get \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" for get all materials position \"([^\"]*)\"$")
    public void iShouldGetForGetAllMaterialsMaterialPosition(final String regionName, final String servicePointCityName, final String name, final String origin, final String unit, final Double unitPrice, final Double quantity, final String quality, final Long position) throws Exception {
        mvc.perform(get("/materials/getAllMaterials"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[" + position + "].servicePoint.region.name", is(regionName)))
                .andExpect(jsonPath("$[" + position + "].servicePoint.city", is(servicePointCityName)))
                .andExpect(jsonPath("$[" + position + "].name", is(name)))
                .andExpect(jsonPath("$[" + position + "].origin", is(origin)))
                .andExpect(jsonPath("$[" + position + "].unit", is(unit)))
                .andExpect(jsonPath("$[" + position + "].unitPrice", is(unitPrice)))
                .andExpect(jsonPath("$[" + position + "].quantity", is(quantity)))
                .andExpect(jsonPath("$[" + position + "].quality", is(quality)));
    }

    @Then("^I should get \"([^\"]*)\" error for get material by id \"([^\"]*)\"$")
    public void iShouldGetErrorForGetMaterialById(final String error, final Long id) throws Exception {
        mvc.perform(get("/materials/getMaterial/" + id))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("error", is(error)))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MaterialNotFoundException));
    }

    @Then("^I should get \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" for get material by id \"([^\"]*)\"$")
    public void iShouldGetForGetMaterialByIdMaterial(final String regionName, final String servicePointCityName, final String name, final String origin, final String unit, final Double unitPrice, final Double quantity, final String quality, final Long id) throws Exception {
        mvc.perform(get("/materials/getMaterial/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.servicePoint.region.name", is(regionName)))
                .andExpect(jsonPath("$.servicePoint.city", is(servicePointCityName)))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.origin", is(origin)))
                .andExpect(jsonPath("$.unit", is(unit)))
                .andExpect(jsonPath("$.unitPrice", is(unitPrice)))
                .andExpect(jsonPath("$.quantity", is(quantity)))
                .andExpect(jsonPath("$.quality", is(quality)));
    }

    @Then("^I should succeed in creating \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void iShouldSucceedInMaterialCreation(final Long servicePointId, final String name, final String origin, final String unit, final Double unitPrice, final Double quantity, final String quality) throws Exception {
        var materialRequest = "{\"servicePointId\": " + servicePointId + ",\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"origin\": \"" + origin + "\",\n" +
                "    \"unit\": \"" + unit + "\",\n" +
                "    \"unitPrice\": " + unitPrice + ",\n" +
                "    \"quantity\": " + quantity + ",\n" +
                "    \"quality\": \"" + quality + "\"\n}";
        mvc.perform(post("/materials/createMaterial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(materialRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.origin", is(origin)))
                .andExpect(jsonPath("$.unit", is(unit)))
                .andExpect(jsonPath("$.unitPrice", is(unitPrice)))
                .andExpect(jsonPath("$.quantity", is(quantity)))
                .andExpect(jsonPath("$.quality", is(quality)));
    }

    @Then("^I should succeed in updating material \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void iShouldSucceedInMaterialUpdate(final Long materialId, final Long servicePointId, final String name, final String origin, final String unit, final Double unitPrice, final Double quantity, final String quality) throws Exception {
        var materialRequest = "{\"id\": " + materialId + ",\n" +
                "    \"servicePointId\": " + servicePointId + ",\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"origin\": \"" + origin + "\",\n" +
                "    \"unit\": \"" + unit + "\",\n" +
                "    \"unitPrice\": " + unitPrice + ",\n" +
                "    \"quantity\": " + quantity + ",\n" +
                "    \"quality\": \"" + quality + "\"\n}";
        mvc.perform(post("/materials/updateMaterial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(materialRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Then("^I should get \"([^\"]*)\" error for updating material \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void iShouldGetErrorInMaterialUpdate(final String error, final Long materialId, final Long servicePointId, final String name, final String origin, final String unit, final Double unitPrice, final Double quantity, final String quality) throws Exception {
        var materialRequest = "{\"id\": " + materialId + ",\n" +
                "    \"servicePointId\": " + servicePointId + ",\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"origin\": \"" + origin + "\",\n" +
                "    \"unit\": \"" + unit + "\",\n" +
                "    \"unitPrice\": " + unitPrice + ",\n" +
                "    \"quantity\": " + quantity + ",\n" +
                "    \"quality\": \"" + quality + "\"\n}";
        mvc.perform(post("/materials/updateMaterial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(materialRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("error", is(error)))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MaterialNotFoundException));
    }

    @Then("^I should get \"([^\"]*)\" error for deleting material by id \"([^\"]*)\"$")
    public void iShouldGetErrorForDeletingMaterialById(final String error, final Long id) throws Exception {
        mvc.perform(delete("/materials/deleteMaterial/" + id))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("error", is(error)))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MaterialNotFoundException));
    }

    @Then("^I should succeed in deleting material by id \"([^\"]*)\"$")
    public void iShouldSucceedInDeletingMaterialById(final Long id) throws Exception {
        mvc.perform(delete("/materials/deleteMaterial/" + id))
                .andExpect(status().isOk());
    }
}
