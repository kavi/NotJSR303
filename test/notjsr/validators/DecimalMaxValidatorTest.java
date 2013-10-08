package notjsr.validators;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.validation.Payload;
import javax.validation.constraints.DecimalMax;

import notjsr.ValidationError;

import org.junit.Before;
import org.junit.Test;

public class DecimalMaxValidatorTest {
	
	private DecimalMaxValidator subject;
	
	@Before
	public void setup() {
		subject = new DecimalMaxValidator();
	}

	@Test
	public void testStringTooBig() {
		String objectValue = "20";
		DecimalMax constraint = new DecimalMaxMock("19.999");
		ValidationError<String> result = subject.validate(objectValue, constraint, objectValue);
		assertNotNull(result);
	}

	@Test
	public void testStringOk() {
		String objectValue = "19";
		DecimalMax constraint = new DecimalMaxMock("19");
		ValidationError<String> result = subject.validate(objectValue, constraint, objectValue);
		assertNull(result);
	}

	@Test
	public void testIntTooBig() {
		int objectValue = 20;
		DecimalMax constraint = new DecimalMaxMock("19.999");
		ValidationError<Integer> result = subject.validate(objectValue, constraint, objectValue);
		assertNotNull(result);
	}

	@Test
	public void testIntOk() {
		int objectValue = 19;
		DecimalMax constraint = new DecimalMaxMock("19");
		ValidationError<Integer> result = subject.validate(objectValue, constraint, objectValue);
		assertNull(result);
	}
	@Test
	public void testBigIntegerTooBig() {
		BigInteger objectValue = BigInteger.valueOf(20);
		DecimalMax constraint = new DecimalMaxMock("19.999");
		ValidationError<BigInteger> result = subject.validate(objectValue, constraint, objectValue);
		assertNotNull(result);
	}

	@Test
	public void testBigIntegerOk() {
		BigInteger objectValue = BigInteger.valueOf(19);
		DecimalMax constraint = new DecimalMaxMock("19");
		ValidationError<BigInteger> result = subject.validate(objectValue, constraint, objectValue);
		assertNull(result);
	}
	
	@Test
	public void testBigDecimalTooBig() {
		BigDecimal objectValue = BigDecimal.valueOf(20);
		DecimalMax constraint = new DecimalMaxMock("19.999");
		ValidationError<BigDecimal> result = subject.validate(objectValue, constraint, objectValue);
		assertNotNull(result);
	}

	@Test
	public void testBigDecimalOk() {
		BigDecimal objectValue = BigDecimal.valueOf(19);
		DecimalMax constraint = new DecimalMaxMock("19");
		ValidationError<BigDecimal> result = subject.validate(objectValue, constraint, objectValue);
		assertNull(result);
	}

	@Test
	public void testIntInclusiveFail() {
		int objectValue = 19;
		DecimalMax constraint = new DecimalMaxMock("19", false);
		ValidationError<Integer> result = subject.validate(objectValue, constraint, objectValue);
		assertNotNull(result);
	}

	@Test
	public void testIntInclusiveOk() {
		int objectValue = 18;
		DecimalMax constraint = new DecimalMaxMock("19", false);
		ValidationError<Integer> result = subject.validate(objectValue, constraint, objectValue);
		assertNull(result);
	}
	
	private static class DecimalMaxMock implements DecimalMax {
		private String value;
		private boolean inclusive = true;

		public DecimalMaxMock(String value) {
			super();
			this.value = value;
		}

		public DecimalMaxMock(String value, boolean inclusive) {
			super();
			this.value = value;
			this.inclusive = inclusive;
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return DecimalMax.class;
		}

		@Override
		public Class<?>[] groups() {
			return null;
		}

		@Override
		public boolean inclusive() {
			return inclusive;
		}

		@Override
		public String message() {
			return null;
		}

		@Override
		public Class<? extends Payload>[] payload() {
			return null;
		}

		@Override
		public String value() {
			return value;
		}
		
	}

}
