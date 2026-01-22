package si.um.feri.measurements.testdto;

public class PostMeasurementDto {

	public PostMeasurementDto() {
	}

	public PostMeasurementDto(Long id, double avgTemperature) {
		this.id = id;
		this.avgTemperature = avgTemperature;
	}

	Long id;

	double avgTemperature;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getAvgTemperature() {
		return avgTemperature;
	}

	public void setAvgTemperature(double avgTemperature) {
		this.avgTemperature = avgTemperature;
	}
}
