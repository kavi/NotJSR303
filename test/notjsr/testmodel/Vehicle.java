package notjsr.testmodel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public abstract class Vehicle {
	@NotNull
	@Valid
	private Person owner;

	public Vehicle() {
		super();
	}
	
	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	
}
