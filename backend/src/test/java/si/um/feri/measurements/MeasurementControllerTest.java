package si.um.feri.measurements;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import si.um.feri.measurements.dao.MeasurementRepository;
import si.um.feri.measurements.dao.ProductRepository;
import si.um.feri.measurements.dto.post.PostMeasurement;
import si.um.feri.measurements.dto.post.PostMeasurementResponse;
import si.um.feri.measurements.rest.MeasurementController;
import si.um.feri.measurements.vao.Measurement;
import si.um.feri.measurements.vao.Product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class MeasurementControllerTest {

    @Inject
    MeasurementController controller;

    @InjectMock
    MeasurementRepository measurementRepository;

    @InjectMock
    ProductRepository productRepository;

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
