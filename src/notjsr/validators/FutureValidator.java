package notjsr.validators;

import java.lang.annotation.Annotation;
import java.util.Date;

import javax.validation.ValidationException;
import javax.validation.constraints.Future;

import notjsr.ValidationError;

public class FutureValidator implements Validator {

	@Override
	public <E> ValidationError<E> validate(E rootEntity, Annotation a,
			Object value) {
		if (value == null) {
			return null;
		}
		if (a.annotationType() != Future.class) {
			throw new ValidationException("Internal error. PastValidator called with invalid annotation.");
		}
		if (! Date.class.equals(value.getClass())) {
			throw new ValidationException("Unexpected type for Past annotation");
		}
		Date date = (Date) value;
		if (date.compareTo(new Date()) < 0) {
			ValidationError<E> error = new ValidationError<E>();
			error.setInvalidValue(date);
			error.setMessage("Date should be in the future.");
			return error;
		}
		return null;
	}

}
