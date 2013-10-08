package notjsr.testmodel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Car extends Vehicle {
	@NotNull
	@Size(min=1)
	private String model;

	public Car() {
		super();
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	
	
}
