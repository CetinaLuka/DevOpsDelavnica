package si.um.feri.measurements;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.vertx.VertxContextSupport;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import si.um.feri.measurements.dao.MeasurementRepository;
import si.um.feri.measurements.dao.ProductRepository;
import si.um.feri.measurements.dto.post.PostMeasurement;
import si.um.feri.measurements.vao.Measurement;
import si.um.feri.measurements.vao.Product;

@ApplicationScoped
public class AddTestData {

    @Inject
    ProductRepository productRepository;
    @Inject
    MeasurementRepository measurementRepository;

    public void onStart(@Observes StartupEvent ev) throws Throwable {
        VertxContextSupport.subscribeAndAwait(() ->
            Panache.withTransaction(() -> {
                Product p1 = new Product();
                p1.setName("Milka Classic");
                p1.setMinMeasure(-5.0);
                p1.setMaxMeasure(18.0);

                Product p2 = new Product();
                p2.setName("Chicken Breasts");
                p2.setMinMeasure(-25.0);
                p2.setMaxMeasure(-8.0);

                PostMeasurement pm1 = new PostMeasurement(Long.valueOf(1), 12);
                PostMeasurement pm2 = new PostMeasurement(Long.valueOf(2), -10);
                return productRepository.persist(p1, p2).chain(() -> measurementRepository.persist(new Measurement(pm1, p1), new Measurement(pm2, p2)));
            }));
    }
}
