package si.um.feri.measurements;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import si.um.feri.measurements.dto.Measurement;
import si.um.feri.measurements.testdto.PostMeasurementDto;
import si.um.feri.measurements.testdto.PostMeasurementResponseDto;
import si.um.feri.measurements.vao.Product;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class RestScenarioTest {

    private Product newProduct;

    Jsonb jsonb= JsonbBuilder.create();

    @BeforeEach
    void createProduct() {
        Product p=new Product(
                new si.um.feri.measurements.dto.Product(0L, "Product", 22.0, -23.5)
        );
        Response response =given()
                .when()
                .body(jsonb.toJson(p))
                .contentType("application/json")
                .post("/products")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract().response();
        newProduct=jsonb.fromJson(response.asPrettyString(),Product.class);
    }

    @Test
    void checkProductExistence() {
        Response response = given()
                .when()
                .get("/products/"+newProduct.getId())
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract().response();
        Product fromServer=jsonb.fromJson(response.asPrettyString(),Product.class);
        assertEquals(fromServer.getName(), newProduct.getName());
        assertEquals(fromServer.getMinMeasure(), -23.5, 0.0001);
    }

    private Response postMeasurement(long productId, double measure) {
        return given()
                .when()
                .body(jsonb.toJson(new PostMeasurementDto(productId,measure)))
                .contentType("application/json")
                .post("/product_measurement")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract().response();
    }

    @Test
    void newOKMeasurement() {
        Response response = postMeasurement(newProduct.getId(),5.5);
        PostMeasurementResponseDto resp=jsonb.fromJson(response.asPrettyString(),PostMeasurementResponseDto.class);
        assertNotNull(resp);
        assertEquals(resp.getResult(),"ok");
    }

    @Test
    void newMeasurementForFakeProduct() {
        Response response = given()
                .when()
                .body(jsonb.toJson(new PostMeasurementDto(11111L,5.5)))
                .contentType("application/json")
                .post("/product_measurement")
                .then()
                .contentType("application/json")
                .extract().response();
        assertTrue(response.getBody().asPrettyString().contains("product-not-found"));
    }

    @Test
    void newNotOKMeasurement() {
        Response response = postMeasurement(newProduct.getId(),-23.6);
        PostMeasurementResponseDto resp=jsonb.fromJson(response.asPrettyString(),PostMeasurementResponseDto.class);
        assertNotNull(resp);
        assertEquals(resp.getResult(),"not ok");
    }

    int count=0;

    @Test
    void measurementHistory() throws JsonProcessingException {
        postMeasurement(newProduct.getId(),1);
        postMeasurement(newProduct.getId(),1.4);
        postMeasurement(newProduct.getId(),0.8);

        Response response = given()
                .when()
                .get("/history")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract().response();

        //JsonArray ja=jsonb.

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Measurement>> listType = new TypeReference<List<Measurement>>() {};

        List<Measurement> jacksonList = objectMapper.readValue(response.asPrettyString(), listType);

        count=0;
        jacksonList.forEach(measurement -> {
            if (measurement.id() == newProduct.getId()) count++;
        });

//        assertEquals(count,3, "The history size should be exactly 3.");
    }

}
