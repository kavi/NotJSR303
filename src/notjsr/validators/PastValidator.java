package notjsr.validators;

import java.lang.annotation.Annotation;
import java.util.Date;

import javax.validation.ValidationException;
import javax.validation.constraints.Past;

import notjsr.ValidationError;

public class PastValidator implements Validator {

	@Override
	public <E> ValidationError<E> validate(E rootEntity, Annotation a,
			Object value) {
		if (value == null) {
			return null;
		}
		if (a.annotationType() != Past.class) {
			throw new ValidationException("Internal error. PastValidator called with invalid annotation.");
		}
		if (! Date.class.equals(value.getClass())) {
			throw new ValidationException("Unexpected type for Past annotation");
		}
		Date date = (Date) value;
		if (date.compareTo(new Date()) > 0) {
			ValidationError<E> error = new ValidationError<E>();
			error.setInvalidValue(date);
			error.setMessage("Date should be in the past.");
			return error;
		}
		return null;
	}

}
