package si.um.feri.measurements;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
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
    @RunOnVertxContext
    void getAllProducts_returnsList() {
        Product p1 = new Product(new si.um.feri.measurements.dto.Product(1L, "A", 10.0, 0.0));
        Product p2 = new Product(new si.um.feri.measurements.dto.Product(2L, "B", 5.0, -1.0));
        when(productRepository.listAll()).thenReturn(Uni.createFrom().item(List.of(p1, p2)));

        UniAssertSubscriber<List<Product>> subscriber = controller.getAllProducts()
                .subscribe().withSubscriber(UniAssertSubscriber.create());
        List<Product> result = subscriber.awaitItem().getItem();
        assertEquals(2, result.size());
    }

    @Test
    @RunOnVertxContext
    void getProductById_returnsProduct() {
        Product p1 = new Product(new si.um.feri.measurements.dto.Product(1L, "A", 10.0, 0.0));
        p1.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Uni.createFrom().item(p1));

        UniAssertSubscriber<Product> subscriber = controller.getProductById(1L)
                .subscribe().withSubscriber(UniAssertSubscriber.create());
        Product result = subscriber.awaitItem().getItem();
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @RunOnVertxContext
    void addProduct_persistsProduct() {
        Product product = new Product(new si.um.feri.measurements.dto.Product(1L, "A", 10.0, 0.0));
        when(productRepository.persistAndFlush(any(Product.class)))
                .thenAnswer(invocation -> Uni.createFrom().item(invocation.getArgument(0)));

        UniAssertSubscriber<Product> subscriber = controller.addProduct(product)
                .subscribe().withSubscriber(UniAssertSubscriber.create());
        Product result = subscriber.awaitItem().getItem();
        assertNotNull(result);
        verify(productRepository).persistAndFlush(product);
    }

    @Test
    @RunOnVertxContext
    void deleteProduct_returnsStatus() {
        when(productRepository.deleteById(12L)).thenReturn(Uni.createFrom().item(true));
        UniAssertSubscriber<Boolean> subscriber = controller.deleteProduct(12L)
                .subscribe().withSubscriber(UniAssertSubscriber.create());
        Boolean result = subscriber.awaitItem().getItem();
        assertTrue(result);
    }

    @Test
    @RunOnVertxContext
    void putProduct_updatesAndReturnsDto() {
        Product product = new Product(new si.um.feri.measurements.dto.Product(1L, "Old", 10.0, 0.0));
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Uni.createFrom().item(product));
        doNothing().when(productRepository).persist(any(Product.class));

        si.um.feri.measurements.dto.Product dto = new si.um.feri.measurements.dto.Product(1L, "New", 12.0, -2.0);
        UniAssertSubscriber<Response> subscriber = controller.putProduct(1, dto)
                .subscribe().withSubscriber(UniAssertSubscriber.create());
        Response response = subscriber.awaitItem().getItem();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        si.um.feri.measurements.dto.Product body = (si.um.feri.measurements.dto.Product) response.getEntity();
        assertEquals("New", body.name());
        assertEquals(12.0, body.maxMeasure(), 0.0001);
        assertEquals(-2.0, body.minMeasure(), 0.0001);
    }

    @Test
    @RunOnVertxContext
    void putProduct_notFound_throws() {
        when(productRepository.findById(1L)).thenReturn(Uni.createFrom().nullItem());
        si.um.feri.measurements.dto.Product dto = new si.um.feri.measurements.dto.Product(1L, "New", 12.0, -2.0);

        UniAssertSubscriber<Response> subscriber = controller.putProduct(1, dto)
                .subscribe().withSubscriber(UniAssertSubscriber.create());
        subscriber.awaitFailure();
        assertTrue(subscriber.getFailure() instanceof NotFoundException);
    }
}
