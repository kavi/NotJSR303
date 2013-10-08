package notjsr.validators;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.validation.UnexpectedTypeException;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;

import notjsr.ValidationError;

public class DecimalMaxValidator implements Validator {

	@Override
	public <E> ValidationError<E> validate(E rootEntity, Annotation annotation,
			Object objectValue) {
		if (objectValue == null) {
			return null;
		}
		if (annotation.annotationType() != DecimalMax.class && annotation.annotationType() != Max.class) {
			throw new RuntimeException(
					"Internal error. DecimalMaxValidator called with wrong type. " + annotation.annotationType());
		}
		BigDecimal maxValue = null;
		boolean inclusive = true;
		if (annotation.annotationType() == DecimalMax.class) { 
			DecimalMax decimalMaxConstraint = (DecimalMax) annotation;
			maxValue = new BigDecimal(decimalMaxConstraint.value());
			inclusive = decimalMaxConstraint.inclusive();
		}
		if (annotation.annotationType() == Max.class) {
			maxValue = new BigDecimal(((Max) annotation).value());
		}
		BigDecimal value = getBigDecimalValue(objectValue);
		if (value.compareTo(maxValue) > 0) {
			ValidationError<E> error = new ValidationError<E>();
			error.setMessage("Value greated than "
					+ maxValue);
			return error;
		}
		
		if (!inclusive) {
			if (value.compareTo(maxValue) == 0) {
				ValidationError<E> error = new ValidationError<E>();
				error.setMessage("Non-inclusive maxValue."
						+ maxValue);
				return error;
			}
		}
		return null;
	}

	private BigDecimal getBigDecimalValue(Object objectValue) {
		Class<? extends Object> type = objectValue.getClass();
		if (type == String.class) {
			return new BigDecimal((String) objectValue);
		} else if (type == short.class || type == Short.class) {
			return BigDecimal.valueOf((Short) objectValue);
		} else if (type == int.class || type == Integer.class) {
			return BigDecimal.valueOf((Integer) objectValue);
		} else if (type == byte.class || type == Byte.class) {
			return BigDecimal.valueOf((Byte) objectValue);
		} else if (type == long.class || type == Long.class) {
			return BigDecimal.valueOf((Long) objectValue);
		} else if (type == float.class || type == Float.class) {
			return BigDecimal.valueOf((Float) objectValue);
		} else if (type == double.class || type == Double.class) {
			return BigDecimal.valueOf((Double) objectValue);
		} else if (type == BigDecimal.class) {
			return (BigDecimal)objectValue;
		} else if (type == BigInteger.class) {
			return new BigDecimal((BigInteger) objectValue);
		} else {
			throw new UnexpectedTypeException("Unsupported type for DecimalMaxValidator: "
					+ objectValue.getClass());
		}

	}

}
