package notjsr.validators;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;

import javax.validation.constraints.DecimalMin;

import notjsr.ValidationError;

public class DecimalMinValidator implements Validator {

	@Override
	public <E> ValidationError<E> validate(E rootEntity, Annotation annotation, Object objectValue) {
		if (objectValue == null) {
			return null;
		}
		if (annotation.annotationType() != DecimalMin.class) {
			System.out.println(annotation.annotationType());
			throw new RuntimeException(
					"Internal error. DecimalMinValidator called with wrong type.");
		}
		DecimalMin decimalMinConstraint = (DecimalMin) annotation;
		BigDecimal minValue = new BigDecimal(decimalMinConstraint.value());
		BigDecimal value = null;
		try {
			value = getBigDecimalValue(objectValue);
		} catch (ParseException e) {
			ValidationError<E> error = new ValidationError<E>();
			error.setMessage("Unable to parse value for DecimalMax. value="
					+ objectValue);
			return error;
		}
		if (value.compareTo(minValue) < 0) {
			ValidationError<E> error = new ValidationError<E>();
			error.setMessage("Value less than "
					+ decimalMinConstraint.value());
			return error;
		}
		return null;
	}

	private BigDecimal getBigDecimalValue(Object objectValue) throws ParseException {
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
		}  else {
			throw new RuntimeException("Unsupported type for DecimalMinValidator: "
					+ objectValue.getClass());
		}

	}

}
