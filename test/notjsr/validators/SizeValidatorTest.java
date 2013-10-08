package notjsr.validators;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Payload;
import javax.validation.constraints.Size;

import notjsr.ValidationError;

import org.junit.Before;
import org.junit.Test;

public class SizeValidatorTest {
	
	private SizeValidator subject;
	
	@Before
	public void setup() {
		subject = new SizeValidator();
	}

	@Test
	public void testStringMinViolated() {
		SizeMock size = new SizeMock();
		size.min = 1;
		String value = "";
		ValidationError<String> error = subject.validate(value, size, value);
		assertNotNull("Should not be null.", error);
	}

	@Test
	public void testStringNullOk() {
		SizeMock size = new SizeMock();
		size.min = 1;
		ValidationError<String> error = subject.validate(null, size, null);
		assertNull(error);
	}
	
	@Test
	public void testStringMaxViolated() {
		SizeMock size = new SizeMock();
		size.max = 1;
		String value = "12";
		ValidationError<String> error = subject.validate(value, size, value);
		assertNotNull("Should not be null.", error);
	}


	@Test
	public void testListMinViolated() {
		SizeMock size = new SizeMock();
		size.min = 1;
		List<String> value = new ArrayList<String>();
		ValidationError<List<String>> error = subject.validate(value, size, value);
		assertNotNull("Should not be null.", error);
	}
	
	@Test
	public void testListMaxViolated() {
		SizeMock size = new SizeMock();
		size.max = 1;
		List<String> value = new ArrayList<String>();
		value.add("1");
		value.add("2");
		ValidationError<List<String>> error = subject.validate(value, size, value);
		assertNotNull("Should not be null.", error);
	}
	
	@Test
	public void testArrayMinViolated() {
		SizeMock size = new SizeMock();
		size.min = 1;
		String[] value = new String[0];
		ValidationError<String[]> error = subject.validate(value, size, value);
		assertNotNull("Should not be null.", error);
	}
	
	@Test
	public void testArrayMaxViolated() {
		SizeMock size = new SizeMock();
		size.max = 1;
		String[] value = new String[2];
		ValidationError<String[]> error = subject.validate(value, size, value);
		assertNotNull("Should not be null.", error);
	}
	
	
	public static class SizeMock implements Size {
		
		int max = Integer.MAX_VALUE;
		int min = 0;
		
		public SizeMock() {
			super();
		}

		public SizeMock(int max, int min) {
			super();
			this.max = max;
			this.min = min;
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return Size.class;
		}

		@Override
		public Class<?>[] groups() {
			return null;
		}

		@Override
		public int max() {
			return max;
		}

		@Override
		public String message() {
			return null;
		}

		@Override
		public int min() {
			return min;
		}

		@Override
		public Class<? extends Payload>[] payload() {
			return null;
		}
		
	}
}
