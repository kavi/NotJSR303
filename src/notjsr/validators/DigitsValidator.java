package notjsr.validators;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;

import javax.validation.ValidationException;
import javax.validation.constraints.Digits;

import notjsr.ValidationError;

/**
 * 
 * Warning: this class is currently not tested and not expected to work properly due
 * to the nature of BigDecimal and scaling.	
 *
 */
public class DigitsValidator implements Validator {
	@Override
	public <E> ValidationError<E> validate(E rootEntity, Annotation annotation, Object objectValue) {
		if (objectValue == null) {
			return null;
		}
		if (annotation.annotationType() != Digits.class) {
			System.out.println(annotation.annotationType());
			throw new ValidationException("Internal error. DigitsValidator called with wrong type.");
		}
		Digits digitsConstraint = (Digits) annotation;
		int fraction = digitsConstraint.fraction();
		int integer = digitsConstraint.integer();
		
		BigDecimal value = null;
		try {
			value = getBigDecimalValue(objectValue);
		} catch (ParseException e) {
			ValidationError<E> error = new ValidationError<E>();
			error.setMessage("Unable to parse value for Digits. value="
					+ objectValue);
			return error;
		}
		String[] plainString = value.toPlainString().split(".");
		if (plainString.length == 0 || plainString.length > 2) {
			throw new ValidationException("Internal error. Value could not be split into integer/fraction parts. value=" + value);
		}
		int actualIntLength = plainString[0].length();
		int actualFracLength = 0;
		if (plainString.length == 2) {
			actualFracLength = plainString[1].length();
		}
		if (integer < actualIntLength) {
			ValidationError<E> error = new ValidationError<E>();
			error.setMessage("Integer length must not be greater than " + integer + ", but was " + actualIntLength);
			return error;
		}
		if (fraction < actualFracLength) {
			ValidationError<E> error = new ValidationError<E>();
			error.setMessage("Fraction length must not be greater than " + fraction + ", but was " + actualFracLength);
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
