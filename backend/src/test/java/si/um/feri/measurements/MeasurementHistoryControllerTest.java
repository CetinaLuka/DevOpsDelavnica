package si.um.feri.measurements;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import si.um.feri.measurements.dao.MeasurementRepository;
import si.um.feri.measurements.dto.post.PostMeasurement;
import si.um.feri.measurements.rest.MeasurementHistoryController;
import si.um.feri.measurements.vao.Measurement;
import si.um.feri.measurements.vao.Product;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class MeasurementHistoryControllerTest {

    @Inject
    MeasurementHistoryController controller;

    @InjectMock
    MeasurementRepository measurementRepository;

    @Test
    @RunOnVertxContext
    void getHistory_returnsMappedDtos() {
        Product product = new Product(new si.um.feri.measurements.dto.Product(1L, "P1", 12.0, -3.0));
        product.setId(1L);

        Measurement m1 = new Measurement(new PostMeasurement(1L, 2.5), product);
        m1.setId(100L);
        m1.setOk(true);
        m1.setCreated(LocalDateTime.of(2024, 1, 1, 12, 0));

        Measurement m2 = new Measurement(new PostMeasurement(1L, -2.0), product);
        m2.setId(101L);
        m2.setOk(false);
        m2.setCreated(LocalDateTime.of(2024, 1, 1, 13, 0));

        when(measurementRepository.findByCreatedGreaterThan(any(LocalDateTime.class)))
                .thenReturn(Uni.createFrom().item(List.of(m1, m2)));

        UniAssertSubscriber<List<si.um.feri.measurements.dto.Measurement>> subscriber = controller.getHistory()
            .subscribe().withSubscriber(UniAssertSubscriber.create());
        List<si.um.feri.measurements.dto.Measurement> result = subscriber.awaitItem().getItem();

        assertEquals(2, result.size());
        assertEquals(100L, result.get(0).id());
        assertEquals(1L, result.get(0).productId());
        assertEquals(2.5, result.get(0).avgTemperature(), 0.0001);
        assertTrue(result.get(0).isOk());
        assertFalse(result.get(1).isOk());
    }
}
