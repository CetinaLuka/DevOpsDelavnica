package si.um.feri.measurements.rest;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestPath;
import si.um.feri.measurements.dao.ProductRepository;
import si.um.feri.measurements.vao.Product;
import java.util.List;

@Path("/products")
public class ProductController {

    @Inject
    ProductRepository productRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Product>> getAllProducts() {
        return productRepository.listAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Uni<Product> getProductById(@RestPath Long id) {
        return productRepository.findById(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Product> addProduct(Product product){
        return productRepository.persistAndFlush(product);
    }

    @Path("/{id}")
    @DELETE
    @WithTransaction
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Boolean> deleteProduct(@RestPath Long id){
        return productRepository.deleteById(id);
    }

    @PUT
    @Path("/{id}")
    @WithTransaction
    public Uni<Response> putProduct(@PathParam("id") int id, si.um.feri.measurements.dto.Product dto) {
        return productRepository.findById((long) id)
                .onItem().ifNull().failWith(new NotFoundException("Product not found"))
                .onItem().invoke(product -> product.updateFrom(dto))
                .onItem().invoke(productRepository::persist)
                .onItem().transform(product -> Response.ok(product.toDto()).build());
    }

}
