package si.um.feri.measurements;

import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import si.um.feri.measurements.dao.MeasurementRepository;
import si.um.feri.measurements.dto.post.PostMeasurement;
import si.um.feri.measurements.rest.MeasurementHistoryController;
import si.um.feri.measurements.vao.Measurement;
import si.um.feri.measurements.vao.Product;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeasurementHistoryControllerTest {

    MeasurementHistoryController controller;

    @Mock
    MeasurementRepository measurementRepository;

    @BeforeEach
    void setUp() {
        controller = new MeasurementHistoryController();
        setField(controller, "measurementRepository", measurementRepository);
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Failed to set field: " + fieldName, e);
        }
    }

    @Test
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

        List<si.um.feri.measurements.dto.Measurement> result = controller.getHistory().await().indefinitely();

        assertEquals(2, result.size());
        assertEquals(100L, result.get(0).id());
        assertEquals(1L, result.get(0).productId());
        assertEquals(2.5, result.get(0).avgTemperature(), 0.0001);
        assertTrue(result.get(0).isOk());
        assertFalse(result.get(1).isOk());
    }
}
