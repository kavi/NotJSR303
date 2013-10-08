package notjsr.testmodel;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Address {

	@NotNull
	@Size(min=1, max=50)
	private String street;
	
	@NotNull
	@Pattern(regexp="[0-9]+[A-Z]?")	
	private String streetNumber;
	
	@NotNull
	@DecimalMin(value="1000")
	@DecimalMax(value="9999")
	private int postCode;
	
	@NotNull
	@Valid
	private Country country;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public int getPostCode() {
		return postCode;
	}

	public void setPostCode(int postCode) {
		this.postCode = postCode;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	
}
