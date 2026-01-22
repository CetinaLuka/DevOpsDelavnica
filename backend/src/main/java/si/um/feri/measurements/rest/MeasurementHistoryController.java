package si.um.feri.measurements.rest;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import si.um.feri.measurements.dao.MeasurementRepository;
import si.um.feri.measurements.dto.Measurement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/history")
@Produces(MediaType.APPLICATION_JSON)
public class MeasurementHistoryController {

    private static final Logger log = Logger.getLogger(MeasurementController.class.toString());

    @Inject
    MeasurementRepository measurementRepository;

    private int envHistoryDayslimit = 10;

    @GET
    public Uni<List<Measurement>> getHistory(){
        long history = System.currentTimeMillis() - envHistoryDayslimit * 3_600_000 * 24;
        LocalDateTime historyDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(history), TimeZone.getDefault().toZoneId());
        return measurementRepository.findByCreatedGreaterThan(historyDate).onItem().transform(list -> list.stream().map(si.um.feri.measurements.vao.Measurement::toDto).collect(Collectors.toList()));
    }

}
