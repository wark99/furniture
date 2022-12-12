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
import ro.sapientia.furniture.exception.UsedMaterialNotFoundException;
import ro.sapientia.furniture.model.Material;
import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.ServicePoint;
import ro.sapientia.furniture.model.UsedMaterial;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestEntityManager
@TestPropertySource(locations = "classpath:eetest.properties")
@ContextConfiguration
public class UsedMaterialStepDefinition {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestEntityManager entityManager;

    @Given("^that we have the following used materials:$")
    public void that_we_have_the_following_used_materials(final DataTable usedMaterials) {
        for (final Map<String, String> data: usedMaterials.asMaps(String.class, String.class)) {
            var region = new Region();
            region.setName(data.get("regionName"));
            entityManager.persist(region);

            var servicePoint = new ServicePoint();
            servicePoint.setRegion(region);
            servicePoint.setCity(data.get("servicePointCityName"));
            entityManager.persist(servicePoint);

            var material = new Material();
            material.setServicePoint(servicePoint);
            material.setName(data.get("materialName"));
            entityManager.persist(material);

            var usedMaterial = new UsedMaterial();
            usedMaterial.setMaterial(material);
            usedMaterial.setFurnitureId(Long.parseLong(data.get("furnitureId")));
            usedMaterial.setQuantity(Integer.parseInt(data.get("quantity")));
            usedMaterial.setPrice(BigDecimal.valueOf(Double.parseDouble(data.get("price"))));
            entityManager.persist(usedMaterial);
        }
        entityManager.flush();
    }

    @Then("^I should get \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" for get all used materials position \"([^\"]*)\"$")
    public void iShouldGetForGetAllUsedMaterialsUsedMaterialPosition(final String regionName, final String servicePointCityName, final String materialName, final Long furnitureId, final int quantity, final BigDecimal price, final Long position) throws Exception {
        mvc.perform(get("/used_materials"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[" + position + "].material.servicePoint.region.name", is(regionName)))
                .andExpect(jsonPath("$[" + position + "].material.servicePoint.city", is(servicePointCityName)))
                .andExpect(jsonPath("$[" + position + "].material.name", is(materialName)))
                .andExpect(jsonPath("$[" + position + "].furnitureId", is(furnitureId.intValue())))
                .andExpect(jsonPath("$[" + position + "].quantity", is(quantity)))
                .andExpect(jsonPath("$[" + position + "].price", is(price.doubleValue())));
    }

    @Then("^I should get \"([^\"]*)\" error for get used material by id \"([^\"]*)\"$")
    public void iShouldGetErrorForGetUsedMaterialById(final String error, final Long id) throws Exception {
        mvc.perform(get("/used_materials/" + id))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("error", is(error)))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof UsedMaterialNotFoundException));
    }

    @Then("^I should get \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" for get used material by id \"([^\"]*)\"$")
    public void iShouldGetForGetUsedMaterialByIdUsedMaterial(final String regionName, final String servicePointCityName, final String materialName, final Long furnitureId, final int quantity, final BigDecimal price, final Long id) throws Exception {
        mvc.perform(get("/used_materials/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.material.servicePoint.region.name", is(regionName)))
                .andExpect(jsonPath("$.material.servicePoint.city", is(servicePointCityName)))
                .andExpect(jsonPath("$.material.name", is(materialName)))
                .andExpect(jsonPath("$.furnitureId", is(furnitureId.intValue())))
                .andExpect(jsonPath("$.quantity", is(quantity)))
                .andExpect(jsonPath("$.price", is(price.doubleValue())));
    }

    @Then("^I should succeed in creating \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void iShouldSucceedInUsedMaterialCreation(final Long materialId, final Long furnitureId, final int quantity, final BigDecimal price) throws Exception {
        var usedMaterialRequest = "{\"materialId\": " + materialId + ",\n" +
                "    \"furnitureId\": \"" + furnitureId + "\",\n" +
                "    \"quantity\": \"" + quantity + "\",\n" +
                "    \"price\": \"" + price + "\"\n}";
        mvc.perform(post("/used_materials/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usedMaterialRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.material.id", is(materialId.intValue())))
                .andExpect(jsonPath("$.furnitureId", is(furnitureId.intValue())))
                .andExpect(jsonPath("$.quantity", is(quantity)))
                .andExpect(jsonPath("$.price", is(price.doubleValue())));
    }

    @Then("^I should succeed in updating used material \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void iShouldSucceedInUsedMaterialUpdate(final Long usedMaterialId, final Long materialId, final Long furnitureId, final int quantity, final BigDecimal price) throws Exception {
        var usedMaterialRequest = "{\"id\": " + usedMaterialId + ",\n" +
                "    \"materialId\": " + materialId + ",\n" +
                "    \"furnitureId\": \"" + furnitureId + "\",\n" +
                "    \"quantity\": \"" + quantity + "\",\n" +
                "    \"price\": \"" + price + "\"\n}";
        mvc.perform(post("/used_materials/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usedMaterialRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Then("^I should get \"([^\"]*)\" error for updating used material \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void iShouldGetErrorInUsedMaterialUpdate(final String error, final Long usedMaterialId, final Long materialId, final Long furnitureId, final int quantity, final BigDecimal price) throws Exception {
        var usedMaterialRequest = "{\"id\": " + usedMaterialId + ",\n" +
                "    \"materialId\": " + materialId + ",\n" +
                "    \"furnitureId\": \"" + furnitureId + "\",\n" +
                "    \"quantity\": \"" + quantity + "\",\n" +
                "    \"price\": \"" + price + "\"\n}";
        mvc.perform(post("/used_materials/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usedMaterialRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("error", is(error)))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof UsedMaterialNotFoundException));
    }

    @Then("^I should get \"([^\"]*)\" error for deleting used material by id \"([^\"]*)\"$")
    public void iShouldGetErrorForDeletingUsedMaterialById(final String error, final Long id) throws Exception {
        mvc.perform(delete("/used_materials/delete/" + id))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("error", is(error)))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof UsedMaterialNotFoundException));
    }

    @Then("^I should succeed in deleting used material by id \"([^\"]*)\"$")
    public void iShouldSucceedInDeletingUsedMaterialById(final Long id) throws Exception {
        mvc.perform(delete("/used_materials/delete/" + id))
                .andExpect(status().isOk());
    }
}
