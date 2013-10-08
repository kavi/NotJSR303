package notjsr;

import javax.validation.ConstraintViolation;
import javax.validation.metadata.ConstraintDescriptor;

public class ValidationError<E> implements ConstraintViolation<E>{

	private Class<?> type;
	private Object invalidValue;
	private String message;
	private NotJSRPath propertyPath;
	
	private E rootBean;

	public ValidationError() {
		super();
	}

	public ValidationError(Class<?> type, Object invalidValue, String message) {
		super();
		this.type = type;
		this.invalidValue = invalidValue;
		this.message = message;
	}

	@Override
	public ConstraintDescriptor<?> getConstraintDescriptor() {
		throw new UnsupportedOperationException("getConstraintDescriptor currently not supported.");
	}

	@Override
	public Object[] getExecutableParameters() {
		throw new UnsupportedOperationException("executable currently not supported.");
	}

	@Override
	public Object getExecutableReturnValue() {
		throw new UnsupportedOperationException("executable currently not supported.");
	}

	@Override
	public Object getInvalidValue() {
		return invalidValue;
	}

	@Override
	public Object getLeafBean() {
		throw new UnsupportedOperationException("getLeafBean currently not supported");
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getMessageTemplate() {
		throw new UnsupportedOperationException("getMessageTemplate not supported.");
	}

	@Override
	public NotJSRPath getPropertyPath() {
		return propertyPath;
	}

	@Override
	public E getRootBean() {
		return rootBean;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<E> getRootBeanClass() {
		return  (Class<E>) rootBean.getClass();
	}

	public Class<?> getType() {
		return type;
	}

	public void setInvalidValue(Object value) {
		this.invalidValue = value;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setPropertyPath(NotJSRPath path) {
		this.propertyPath = path;
	}

	public void setRootBean(E rootBean) {
		this.rootBean = rootBean;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ValidationError [type=" + type + ", invalidValue="
				+ invalidValue + ", message=" + message + ", propertyPath="
				+ propertyPath.pathAsString() + "]";
	}

	@Override
	public <U> U unwrap(Class<U> arg0) {
		throw new UnsupportedOperationException("unwrap not supported.");
	}
}
