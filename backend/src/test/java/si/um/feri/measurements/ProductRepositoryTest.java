package si.um.feri.measurements;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import si.um.feri.measurements.dao.MeasurementRepository;
import si.um.feri.measurements.dao.ProductRepository;
import si.um.feri.measurements.vao.Product;

@QuarkusTest
public class ProductRepositoryTest {

    @Inject
    ProductRepository daoProd;

    @Inject
    MeasurementRepository daoMeas;

    Product product;

    @BeforeEach
    void beforeEach() {
//        daoMeas.deleteAll().await().atMost(Duration.ofMillis(200));
//        daoProd.deleteAll().await().atMost(Duration.ofMillis(200));
//        Uni<Product> ret=daoProd.persistAndFlush(
//            new Product(new si.um.feri.measurements.dto.Product(
//                0l,"Test product",25.0,-12.5
//            ))
//        );
//        ret.await().atMost(Duration.ofMillis(200));
//        ret.onItem().transform((p)->{product=p;return p;});
    }

}
