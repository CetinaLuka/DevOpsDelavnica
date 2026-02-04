package si.um.feri.measurements;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import si.um.feri.measurements.dao.ProductRepository;
import si.um.feri.measurements.rest.ProductController;
import si.um.feri.measurements.vao.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class ProductControllerTest {

    @Inject
    ProductController controller;

    @InjectMock
    ProductRepository productRepository;

    @Test
    Uni<Void> getAllProducts_returnsList() {
        Product p1 = new Product(new si.um.feri.measurements.dto.Product(1L, "A", 10.0, 0.0));
        Product p2 = new Product(new si.um.feri.measurements.dto.Product(2L, "B", 5.0, -1.0));
        when(productRepository.listAll()).thenReturn(Uni.createFrom().item(List.of(p1, p2)));

        return controller.getAllProducts()
            .invoke(result -> assertEquals(2, result.size()))
            .replaceWithVoid();
    }

    /*@Test
    Uni<Void> getProductById_returnsProduct() {
        Product p1 = new Product(new si.um.feri.measurements.dto.Product(1L, "A", 10.0, 0.0));
        p1.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Uni.createFrom().item(p1));

        return controller.getProductById(1L)
            .invoke(result -> {
                assertNotNull(result);
                assertEquals(1L, result.getId());
            })
            .replaceWithVoid();
    }

    @Test
    Uni<Void> addProduct_persistsProduct() {
        Product product = new Product(new si.um.feri.measurements.dto.Product(1L, "A", 10.0, 0.0));
        when(productRepository.persistAndFlush(any(Product.class)))
            .thenReturn(Uni.createFrom().item(product));

        return controller.addProduct(product)
            .invoke(result -> {
                assertNotNull(result);
                verify(productRepository).persistAndFlush(product);
            })
            .replaceWithVoid();
    }

    @Test
    Uni<Void> deleteProduct_returnsStatus() {
        when(productRepository.deleteById(12L)).thenReturn(Uni.createFrom().item(true));
        return controller.deleteProduct(12L)
            .invoke(result -> assertTrue(result))
            .replaceWithVoid();
    }
    */
    @Test
    Uni<Void> putProduct_updatesAndReturnsDto() {
        Product product = new Product(new si.um.feri.measurements.dto.Product(1L, "Old", 10.0, 0.0));
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Uni.createFrom().item(product));
        when(productRepository.persist(any(Product.class))).thenReturn(Uni.createFrom().nullItem());

        si.um.feri.measurements.dto.Product dto = new si.um.feri.measurements.dto.Product(1L, "New", 12.0, -2.0);
        return controller.putProduct(1, dto)
            .invoke(response -> {
                assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
                si.um.feri.measurements.dto.Product body = (si.um.feri.measurements.dto.Product) response.getEntity();
                assertEquals("New", body.name());
                assertEquals(12.0, body.maxMeasure(), 0.0001);
                assertEquals(-2.0, body.minMeasure(), 0.0001);
            })
            .replaceWithVoid();
    }

}
