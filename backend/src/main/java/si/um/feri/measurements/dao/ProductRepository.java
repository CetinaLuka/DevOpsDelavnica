package si.um.feri.measurements.dao;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import si.um.feri.measurements.vao.Product;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
}
