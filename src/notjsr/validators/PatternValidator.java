package notjsr.validators;

import java.lang.annotation.Annotation;

import javax.validation.constraints.Pattern;

import notjsr.ValidationError;

public class PatternValidator implements Validator {

	@Override
	public <E> ValidationError<E> validate(E rootEntity, Annotation a, Object objectValue) {
		if (a.annotationType() != Pattern.class) {
			throw new RuntimeException("Internal error. PatternValidator called with wrong type.");
		}
		if (objectValue == null) {
			return null;
		}
		if (objectValue.getClass() != String.class) {
			throw new RuntimeException("Invalid value type for PatternValidator." + objectValue.getClass());
		}
		Pattern pattern = (Pattern) a;
		String value = (String) objectValue;
		if (!java.util.regex.Pattern.matches(pattern.regexp(), value)) {
			ValidationError<E> error = new ValidationError<E>();
			error.setMessage("Value '" + value + "' did not match regex: " + pattern.regexp());			
			return error;			
		}
		return null;
	}
}
