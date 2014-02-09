package dbutils.spring;

import org.apache.commons.lang.reflect.FieldUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link dbutils.spring.SqlParameterSource} implementation that obtains parameter values
 * from bean properties of a given JavaBean object. The names of the bean
 * properties have to match the parameter names.
 *
 * <p>Uses a Spring BeanWrapper for bean property access underneath.
 *
 * @author Thomas Risberg
 * @author Juergen Hoeller
 * @since 2.0
 * @see NamedParameterJdbcTemplate
 * @see org.springframework.beans.BeanWrapper
 */
public class BeanPropertySqlParameterSource extends AbstractSqlParameterSource {

    private Object object;

	private String[] propertyNames;


	/**
	 * Create a new BeanPropertySqlParameterSource for the given bean.
	 * @param object the bean instance to wrap
	 */
	public BeanPropertySqlParameterSource(Object object) {
		this.object = object;
	}


	public boolean hasValue(String paramName) {
        Field field = FieldUtils.getField(object.getClass(), paramName, true);
        return field != null;
	}

	public Object getValue(String paramName) throws IllegalArgumentException {
        Field field = FieldUtils.getField(object.getClass(), paramName, true);
        try {
            return FieldUtils.readField(field, object);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
	}

	/**
	 * Provide access to the property names of the wrapped bean.
	 * Uses support provided in the {@link PropertyAccessor} interface.
	 * @return an array containing all the known property names
	 */
	public String[] getReadablePropertyNames() {
        if (this.propertyNames == null) {
            List<String> names = new ArrayList<String>();

            PropertyDescriptor[] descriptors;
            try {
                descriptors = Introspector.getBeanInfo(object.getClass())
                        .getPropertyDescriptors();
            } catch (IntrospectionException e) {
                throw new RuntimeException("Couldn't introspect bean "
                        + object.getClass().toString(), e);
            }

            for (PropertyDescriptor descriptor : descriptors) {
                names.add(descriptor.getName());
            }
            this.propertyNames = names.toArray(new String[names.size()]);
        }
        return this.propertyNames;
    }

	/**
	 * Derives a default SQL type from the corresponding property type.
	 * @see org.springframework.jdbc.core.StatementCreatorUtils#javaTypeToSqlParameterType
	 */
	@Override
	public int getSqlType(String paramName) {
		int sqlType = super.getSqlType(paramName);
		if (sqlType != TYPE_UNKNOWN) {
			return sqlType;
		}
        Field field = FieldUtils.getField(object.getClass(), paramName, true);
		Class propType = field.getType();
		return StatementCreatorUtils.javaTypeToSqlParameterType(propType);
	}

}

