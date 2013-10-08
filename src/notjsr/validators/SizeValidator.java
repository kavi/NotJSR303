package notjsr.validators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import javax.validation.constraints.Size;

import notjsr.ValidationError;

public class SizeValidator implements Validator {

	@Override
	public <E> ValidationError<E> validate(E rootEntity, Annotation a, Object objectValue) {
		if (objectValue == null) {
			return null;
		}
		if (a.annotationType() != Size.class) {
			System.out.println(a.annotationType());
			throw new RuntimeException(
					"Internal error. SizeValidator called with wrong type.");
		}
		Size sizeConstraint = (Size) a;
		int len = getLength(objectValue);
		if (len < sizeConstraint.min()) {
			ValidationError<E> error = new ValidationError<E>();
			error.setMessage("Size less than "
					+ sizeConstraint.min());
			return error;
		}
		if (len > sizeConstraint.max()) {
			ValidationError<E> error = new ValidationError<E>();
			error.setMessage("Size greater than "
					+ sizeConstraint.max());
			return error;
		}
		return null;
	}

	private int getLength(Object objectValue) {
		if (objectValue.getClass() == String.class) {
			return ((String) objectValue).length();
		} else if (objectValue.getClass().isArray()) {
			return Array.getLength(objectValue);
		} else if (objectValue instanceof Collection<?>) {
			return ((Collection<?>) objectValue).size();
		} else if (objectValue instanceof Map<?, ?>) {
			return ((Map<?, ?>) objectValue).size();
		} else {
			throw new RuntimeException("Unsupported type for SizeValidator: "
					+ objectValue.getClass());
		}

	}

}
