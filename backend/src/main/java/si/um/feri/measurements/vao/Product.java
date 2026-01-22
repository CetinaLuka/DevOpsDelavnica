package si.um.feri.measurements.vao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Product {

	public Product(si.um.feri.measurements.dto.Product dto) {
		setName(dto.name());
		setMaxMeasure(dto.maxMeasure());
		setMinMeasure(dto.minMeasure());
	}

	public Product() {

	}

	public void updateFrom(si.um.feri.measurements.dto.Product dto) {
		setName(dto.name());
		setMaxMeasure(dto.maxMeasure());
		setMinMeasure(dto.minMeasure());
	}
	
	public si.um.feri.measurements.dto.Product toDto() {
		return new si.um.feri.measurements.dto.Product(
			getId(),
			getName(),
			maxMeasure,
			minMeasure);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	protected String name;

	protected LocalDateTime created=LocalDateTime.now();

	protected double maxMeasure;

	protected double minMeasure;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public double getMaxMeasure() {
		return maxMeasure;
	}

	public void setMaxMeasure(double maxMeasure) {
		this.maxMeasure = maxMeasure;
	}

	public double getMinMeasure() {
		return minMeasure;
	}

	public void setMinMeasure(double minMeasure) {
		this.minMeasure = minMeasure;
	}
}
