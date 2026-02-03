package si.um.feri.measurements;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import si.um.feri.measurements.dao.MeasurementRepository;
import si.um.feri.measurements.dao.ProductRepository;
import si.um.feri.measurements.dto.post.PostMeasurement;
import si.um.feri.measurements.dto.post.PostMeasurementResponse;
import si.um.feri.measurements.rest.MeasurementController;
import si.um.feri.measurements.vao.Measurement;
import si.um.feri.measurements.vao.Product;

import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeasurementControllerTest {

    MeasurementController controller;

    @Mock
    MeasurementRepository measurementRepository;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        controller = new MeasurementController();
        setField(controller, "measurementRepository", measurementRepository);
        setField(controller, "productRepository", productRepository);
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
    void addMeasurement_okWithinRange() {
        Product product = new Product(new si.um.feri.measurements.dto.Product(1L, "P1", 10.0, 0.0));
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Uni.createFrom().item(product));
        when(measurementRepository.persistAndFlush(any(Measurement.class)))
                .thenAnswer(invocation -> Uni.createFrom().item(invocation.getArgument(0)));

        RestResponse<PostMeasurementResponse> response = controller
            .addMeasurement(new PostMeasurement(1L, 5.0))
            .await().indefinitely();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        assertEquals("ok", response.getEntity().result());

        ArgumentCaptor<Measurement> captor = ArgumentCaptor.forClass(Measurement.class);
        verify(measurementRepository).persistAndFlush(captor.capture());
        assertTrue(captor.getValue().isOk());
    }

    @Test
    void addMeasurement_notOkOutOfRange() {
        Product product = new Product(new si.um.feri.measurements.dto.Product(2L, "P2", 10.0, 0.0));
        product.setId(2L);

        when(productRepository.findById(2L)).thenReturn(Uni.createFrom().item(product));
        when(measurementRepository.persistAndFlush(any(Measurement.class)))
                .thenAnswer(invocation -> Uni.createFrom().item(invocation.getArgument(0)));

        RestResponse<PostMeasurementResponse> response = controller
            .addMeasurement(new PostMeasurement(2L, -1.0))
            .await().indefinitely();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        assertEquals("not ok", response.getEntity().result());

        ArgumentCaptor<Measurement> captor = ArgumentCaptor.forClass(Measurement.class);
        verify(measurementRepository).persistAndFlush(captor.capture());
        assertFalse(captor.getValue().isOk());
    }

    @Test
    void addMeasurement_productNotFound() {
        when(productRepository.findById(99L))
                .thenReturn(Uni.createFrom().failure(new RuntimeException("not found")));

        RestResponse<PostMeasurementResponse> response = controller
            .addMeasurement(new PostMeasurement(99L, 1.0))
            .await().indefinitely();

        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        assertEquals("product-not-found", response.getEntity().result());
        verifyNoInteractions(measurementRepository);
    }
}
