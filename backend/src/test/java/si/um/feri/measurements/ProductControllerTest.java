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
    void getAllProducts_returnsList() {
        Product p1 = new Product(new si.um.feri.measurements.dto.Product(1L, "A", 10.0, 0.0));
        Product p2 = new Product(new si.um.feri.measurements.dto.Product(2L, "B", 5.0, -1.0));
        when(productRepository.listAll()).thenReturn(Uni.createFrom().item(List.of(p1, p2)));

    List<Product> result = controller.getAllProducts().await().indefinitely();
        assertEquals(2, result.size());
    }

    @Test
    void getProductById_returnsProduct() {
        Product p1 = new Product(new si.um.feri.measurements.dto.Product(1L, "A", 10.0, 0.0));
        p1.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Uni.createFrom().item(p1));

    Product result = controller.getProductById(1L).await().indefinitely();
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void addProduct_persistsProduct() {
        Product product = new Product(new si.um.feri.measurements.dto.Product(1L, "A", 10.0, 0.0));
    when(productRepository.persistAndFlush(any(Product.class)))
        .thenReturn(Uni.createFrom().item(product));

    Product result = controller.addProduct(product).await().indefinitely();
        assertNotNull(result);
        verify(productRepository).persistAndFlush(product);
    }

    @Test
    void deleteProduct_returnsStatus() {
        when(productRepository.deleteById(12L)).thenReturn(Uni.createFrom().item(true));
    Boolean result = controller.deleteProduct(12L).await().indefinitely();
        assertTrue(result);
    }

    @Test
    void putProduct_updatesAndReturnsDto() {
        Product product = new Product(new si.um.feri.measurements.dto.Product(1L, "Old", 10.0, 0.0));
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Uni.createFrom().item(product));
    when(productRepository.persist(any(Product.class))).thenReturn(Uni.createFrom().nullItem());

        si.um.feri.measurements.dto.Product dto = new si.um.feri.measurements.dto.Product(1L, "New", 12.0, -2.0);
    Response response = controller.putProduct(1, dto).await().indefinitely();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        si.um.feri.measurements.dto.Product body = (si.um.feri.measurements.dto.Product) response.getEntity();
        assertEquals("New", body.name());
        assertEquals(12.0, body.maxMeasure(), 0.0001);
        assertEquals(-2.0, body.minMeasure(), 0.0001);
    }

    @Test
    void putProduct_notFound_throws() {
        when(productRepository.findById(1L)).thenReturn(Uni.createFrom().nullItem());
        si.um.feri.measurements.dto.Product dto = new si.um.feri.measurements.dto.Product(1L, "New", 12.0, -2.0);

        assertThrows(NotFoundException.class, () -> controller.putProduct(1, dto).await().indefinitely());
    }
}
