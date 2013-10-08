package notjsr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import notjsr.testmodel.Address;
import notjsr.testmodel.Car;
import notjsr.testmodel.Country;
import notjsr.testmodel.Country.CountryName;
import notjsr.testmodel.Person;
import notjsr.testmodel.Vehicle;

import org.junit.Test;

public class NotJSRValidatorTest {

	private NotJSRValidator<A> subject;

	@Test
	public void test() {
		A a = new A("Long!!");
		a.setNoMax("12345");
		a.setNoMin("");
		C[] cArray = new C[2];
		cArray[0] = new C();
		cArray[1] = new C();
		List<C> clist = new ArrayList<C>();
		clist.add(new C());
		a.setcArray(cArray);
		a.setClist(clist);
		B b = new B("BBB");
		a.setAtoBreference(b);
		C c = new C();
		b.setBtoCReference(c);
		c.setCtoBReference(b);
		subject = new NotJSRValidator<NotJSRValidatorTest.A>(a);
		List<ValidationError<A>> validationResult = subject.validate();
		assertNotNull(validationResult);
		assertEquals(10, validationResult.size());
		for (ValidationError<A> e : validationResult) {
			System.out.println(e);
		}
		System.out.println(subject.getObjectGraph().graphToString());
	}
	
	@Test
	public void testValid() {
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		Country danmark = new Country("Danmark");
		danmark.addTranslation("UK", new CountryName("Denmark"));
		danmark.addTranslation("DE", new CountryName("Dänemark"));
		Address aarhus = new Address();
		aarhus.setCountry(danmark);
		aarhus.setPostCode(8000);
		aarhus.setStreet("Falstersgade");
		aarhus.setStreetNumber("14A");
		Person peter = new Person();
		peter.setFirstName("Peter");
		peter.setLastName("Peter");
		peter.setAddress(aarhus);
		peter.setVehicles(vehicles);
		
		Car opel = new Car();
		opel.setModel("Opel");
		opel.setOwner(peter);
		peter.addVehicle(opel);
		
		NotJSRValidator<Person> validator = new NotJSRValidator<Person>(peter);
		List<ValidationError<Person>> violations = validator.validate();
		for(ValidationError<Person> e : violations) {
			System.out.println(e);
		}
		assertEquals(0, violations.size());
		System.out.println(validator.getObjectGraph().graphToString());
	}

	public static class A {
		@Size(min = 2, max = 5)
		private String name;

		@Size(min = 1)
		private String noMax;

		@Size(max = 2)
		private String noMin;

		@NotNull
		@Valid
		private B atoBreference;

		@NotNull
		@Valid
		private C atoCreference;
		
		@Valid
		private C[] cArray;
		
		@Valid
		private List<C> clist;

		public List<C> getClist() {
			return clist;
		}

		public void setClist(List<C> clist) {
			this.clist = clist;
		}

		public A(String name) {
			super();
			this.name = name;
		}

		public String getNoMax() {
			return noMax;
		}

		public void setNoMax(String noMax) {
			this.noMax = noMax;
		}

		public String getNoMin() {
			return noMin;
		}

		public void setAtoCreference(C atoCreference) {
			this.atoCreference = atoCreference;
		}

		public void setcArray(C[] cArray) {
			this.cArray = cArray;
		}

		public C[] getCArray() {
			return cArray;
		}

		public void setNoMin(String noMin) {
			this.noMin = noMin;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public B getAtoBreference() {
			return atoBreference;
		}

		public void setAtoBreference(B reference) {
			this.atoBreference = reference;
		}

		public C getAtoCreference() {
			return atoCreference;
		}

		public void setCreference(C creference) {
			this.atoCreference = creference;
		}
	}

	public static class B {
		@Size(min = 4, max = 6)
		private String name;

		@NotNull
		@Valid
		private C btoCReference;

		public B(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public C getBtoCReference() {
			return btoCReference;
		}

		public void setBtoCReference(C reference) {
			this.btoCReference = reference;
		}

	}

	public static class C {
		@NotNull
		@Valid
		B ctoBReference;

		@Size(min = 1, max = 2)
		private List<String> names = new ArrayList<String>();

		public C() {
			super();
		}

		public B getCtoBReference() {
			return ctoBReference;
		}

		public void setCtoBReference(B reference) {
			this.ctoBReference = reference;
		}

		public List<String> getNames() {
			return names;
		}

		public void addName(String name) {
			this.names.add(name);
		}

	}

}
