package notjsr;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import notjsr.NotJSRPath.PathNode;
import notjsr.validators.DecimalMaxValidator;
import notjsr.validators.DecimalMinValidator;
import notjsr.validators.DigitsValidator;
import notjsr.validators.FutureValidator;
import notjsr.validators.NotNullValidator;
import notjsr.validators.PastValidator;
import notjsr.validators.PatternValidator;
import notjsr.validators.SizeValidator;
import notjsr.validators.Validator;

public class NotJSRValidator<E> {

	private E entity;

	private Map<Class<?>, Set<Object>> validated;

	private Map<Class<? extends Annotation>, Validator> validators;

	private List<ValidationError<E>> validationErrors;

	private ObjectGraph objectGraph;

	public NotJSRValidator(E entity) {
		super();
		this.entity = entity;
		validationErrors = new ArrayList<ValidationError<E>>();
		validated = new HashMap<Class<?>, Set<Object>>();
		validators = new HashMap<Class<? extends Annotation>, Validator>();

		validators.put(Size.class, new SizeValidator());
		validators.put(NotNull.class, new NotNullValidator());
		validators.put(Pattern.class, new PatternValidator());
		validators.put(DecimalMax.class, new DecimalMaxValidator());
		validators.put(DecimalMin.class, new DecimalMinValidator());
		validators.put(Min.class, new DecimalMinValidator()); 
		validators.put(Max.class, new DecimalMaxValidator()); 
		validators.put(Digits.class, new DigitsValidator()); 
		validators.put(Past.class, new PastValidator()); 
		validators.put(Future.class, new FutureValidator()); 
	}

	public ObjectGraph getObjectGraph() {
		return objectGraph;
	}

	private Validator getValidator(Annotation a) {
		return validators.get(a.annotationType());
	}

	private Object getValue(Object o, Field f) {
		try {
			Object value;
			if (f.isAccessible()) {
				value = f.get(o);
			} else {
				String property = f.getName().substring(0, 1).toUpperCase()
						+ f.getName().substring(1);
				Method getter = o.getClass().getMethod("get" + property,
						new Class<?>[0]);
				value = getter.invoke(o, new Object[0]);
			}
			return value;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Non-accessible field annotated: "
					+ f.getName(), e);
		} catch (SecurityException e) {
			throw new RuntimeException(
					"Unable to access field: " + f.getName(), e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(
					"Unable to access field: " + f.getName(), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(
					"Unable to access field: " + f.getName(), e);
		}
	}

	private boolean isValidated(Object o, Class<?> type) {
		Set<Object> set = validated.get(type);
		if (set == null) {
			set = new HashSet<Object>();
			validated.put(type, set);
		}
		if (set.contains(o)) {
			return true;
		}
		return false;
	}

	private void makeRecursiveValidation(ObjectGraph path,
			Object enclosingObject, Field currentField,
			Class<?> currentFieldType) {
		Object value = getValue(enclosingObject, currentField);
		if (value == null) {
			return;
		}
		if (currentFieldType.isArray()) {
			Object arrayValue = value;
			int length = Array.getLength(arrayValue);
			for (int i = 0; i < length; i++) {
				Object element = Array.get(arrayValue, i);
				path.addActiveChild(currentFieldType, currentField.getName(), i);
				validate(path, element);
				objectGraph.makeParentActive();
			}
		} else if (value instanceof Iterable<?>) {
			Iterable<?> iterableValue = (Iterable<?>) getValue(enclosingObject,
					currentField);
			Iterator<?> it = iterableValue.iterator();
			int index = 0;
			while (it.hasNext()) {
				Object element = it.next();
				path.addActiveChild(currentFieldType, currentField.getName(), index);
				validate(path, element);
				objectGraph.makeParentActive();
				index++;
			}
		} else if (value instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) getValue(enclosingObject,
					currentField);
			Set<?> keys = map.keySet();
			for (Object key : keys) {
				Object element = map.get(key);
				path.addActiveChild(currentFieldType, currentField.getName(), key);
				validate(path, element);
				objectGraph.makeParentActive();
			}
		} else {
			path.addActiveChild(currentFieldType,
					currentField.getName());
			validate(path, getValue(enclosingObject, currentField));
			objectGraph.makeParentActive();
		}
	}
	
	public List<ValidationError<E>> validate() {
		objectGraph = new ObjectGraph(entity.getClass());
		validationErrors.clear();
		validate(objectGraph, entity);
		return validationErrors;
	}

	private void validate(ObjectGraph path, Object enclosingObject) {
		if (enclosingObject == null) {
			return;
		}
		Class<?> enclosingtype = enclosingObject.getClass();
		if (isValidated(enclosingObject, enclosingtype)) {
			return;
		}
		validated.get(enclosingtype).add(enclosingObject);

		// Validate fields
		Field[] fields = enclosingtype.getDeclaredFields();
		for (Field currentField : fields) {
			Class<?> currentFieldType = currentField.getType();
			validateField(path, enclosingObject, currentField);
			if (currentField.getAnnotation(Valid.class) != null) {
				makeRecursiveValidation(path, enclosingObject, currentField, currentFieldType);
			} else {
				path.addActiveChild(currentFieldType, currentField.getName());
				path.makeParentActive();
			}
		}
	}

	private void validateField(ObjectGraph path, Object enclosingObject,
			Field currentField) {
		Class<?> currentFieldType = currentField.getType();
		Object value = null;
		for (Annotation a : currentField.getAnnotations()) {
			Validator v = getValidator(a);
			if (v == null) {
				// No validator for this annotation
				continue;
			}
			if (value == null) {
				value = getValue(enclosingObject, currentField);
			}
			ValidationError<E> error = v.validate(entity, a, value);
			if (error != null) {
				error.setRootBean(entity);
				error.setInvalidValue(value);
				error.setType(currentFieldType);
				NotJSRPath currentFieldPath = path.getActiveNodePath();
				currentFieldPath.addElement(new PathNode(currentFieldType, currentField.getName()));
				error.setPropertyPath(currentFieldPath);
				validationErrors.add(error);
			}
		}
	}
	
	public static <E> List<ValidationError<E>> validate(E entity) {
		NotJSRValidator<E> validator = new NotJSRValidator<E>(entity);
		return validator.validate();
	}
}
