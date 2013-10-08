package notjsr.validators;

import java.lang.annotation.Annotation;

import notjsr.ValidationError;

public interface Validator {

	public abstract <E> ValidationError<E> validate(E rootEntity, Annotation a, Object value);
}
