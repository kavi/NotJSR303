package notjsr.validators;

import java.lang.annotation.Annotation;

import javax.validation.constraints.NotNull;

import notjsr.ValidationError;

public class NotNullValidator implements Validator {

	@Override
	public <E> ValidationError<E> validate(E rootEntity, Annotation a, Object objectValue) {
		if (a.annotationType() != NotNull.class) {
			throw new RuntimeException("Internal error. NotNullValidator called with wrong type.");
		}
		if (objectValue != null) {
			return null;
		}
		ValidationError<E> error = new ValidationError<E>();
		error.setMessage("NotNull");
		return error;
	}
}
