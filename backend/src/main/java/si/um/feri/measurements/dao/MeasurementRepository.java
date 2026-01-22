package si.um.feri.measurements.dao;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import si.um.feri.measurements.vao.Measurement;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class MeasurementRepository implements PanacheRepository<Measurement> {
    public Uni<List<Measurement>> findByCreatedGreaterThan(LocalDateTime created){
        return find("created >= ?1", created).list();
    }
}
