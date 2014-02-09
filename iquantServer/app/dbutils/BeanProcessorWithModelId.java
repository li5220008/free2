package dbutils;

import org.apache.commons.dbutils.BeanProcessor;
import play.db.jpa.Model;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * bean处理, 主要是处理playframework里的Model里的id. 因为它是public的, 用java的属性反射得不到它. 这里强制指定
 * 这里先把 org.apache.commons.dbutils.BeanProcessor 代码先拷贝过来
 * User: wenzhihong
 * Date: 12-12-12
 * Time: 上午10:04
 */
public class BeanProcessorWithModelId extends BeanProcessor {
    /**
         * Set a bean's primitive properties to these defaults when SQL NULL
         * is returned.  These are the same as the defaults that ResultSet get*
         * methods return in the event of a NULL column.
         */
        private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<Class<?>, Object>();

        static {
            primitiveDefaults.put(Integer.TYPE, Integer.valueOf(0));
            primitiveDefaults.put(Short.TYPE, Short.valueOf((short) 0));
            primitiveDefaults.put(Byte.TYPE, Byte.valueOf((byte) 0));
            primitiveDefaults.put(Float.TYPE, Float.valueOf(0f));
            primitiveDefaults.put(Double.TYPE, Double.valueOf(0d));
            primitiveDefaults.put(Long.TYPE, Long.valueOf(0L));
            primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
            primitiveDefaults.put(Character.TYPE, Character.valueOf((char) 0));
        }


    public BeanProcessorWithModelId() {
        super();
    }

    public BeanProcessorWithModelId(Map<String, String> columnToPropertyOverrides) {
        super(columnToPropertyOverrides);

    }

    /**
     * Convert a <code>ResultSet</code> row into a JavaBean.  This
     * implementation uses reflection and <code>BeanInfo</code> classes to
     * match column names to bean property names.  Properties are matched to
     * columns based on several factors:
     * <br/>
     * <ol>
     *     <li>
     *     The class has a writable property with the same name as a column.
     *     The name comparison is case insensitive.
     *     </li>
     *
     *     <li>
     *     The column type can be converted to the property's set method
     *     parameter type with a ResultSet.get* method.  If the conversion fails
     *     (ie. the property was an int and the column was a Timestamp) an
     *     SQLException is thrown.
     *     </li>
     * </ol>
     *
     * <p>
     * Primitive bean properties are set to their defaults when SQL NULL is
     * returned from the <code>ResultSet</code>.  Numeric fields are set to 0
     * and booleans are set to false.  Object bean properties are set to
     * <code>null</code> when SQL NULL is returned.  This is the same behavior
     * as the <code>ResultSet</code> get* methods.
     * </p>
     * @param <T> The type of bean to create
     * @param rs ResultSet that supplies the bean data
     * @param type Class from which to create the bean instance
     * @throws java.sql.SQLException if a database access error occurs
     * @return the newly created bean
     */
    public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {

        PropertyDescriptor[] props = this.propertyDescriptors(type);

        ResultSetMetaData rsmd = rs.getMetaData();
        int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);

        return this.createBean(rs, type, props, columnToProperty);
    }

    /**
     * 转换成bean. 只转列出来的属性
     */
    public <T> T toBeanWithField(ResultSet rs, Class<T> type, String... fields) throws SQLException {
        PropertyDescriptor[] props = this.propertyDescriptors(type);
        List<PropertyDescriptor> list = new ArrayList<PropertyDescriptor>(fields.length);
        for (String f : fields) {
            for (PropertyDescriptor prop : props) {
                if (f.equalsIgnoreCase(prop.getName())) {
                    list.add(prop);
                    break;
                }
            }
        }

        props = list.toArray(new PropertyDescriptor[list.size()]);
        ResultSetMetaData rsmd = rs.getMetaData();
        int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);

        return this.createBean(rs, type, props, columnToProperty);
    }

    /**
     * Convert a <code>ResultSet</code> into a <code>List</code> of JavaBeans.
     * This implementation uses reflection and <code>BeanInfo</code> classes to
     * match column names to bean property names. Properties are matched to
     * columns based on several factors:
     * <br/>
     * <ol>
     *     <li>
     *     The class has a writable property with the same name as a column.
     *     The name comparison is case insensitive.
     *     </li>
     *
     *     <li>
     *     The column type can be converted to the property's set method
     *     parameter type with a ResultSet.get* method.  If the conversion fails
     *     (ie. the property was an int and the column was a Timestamp) an
     *     SQLException is thrown.
     *     </li>
     * </ol>
     *
     * <p>
     * Primitive bean properties are set to their defaults when SQL NULL is
     * returned from the <code>ResultSet</code>.  Numeric fields are set to 0
     * and booleans are set to false.  Object bean properties are set to
     * <code>null</code> when SQL NULL is returned.  This is the same behavior
     * as the <code>ResultSet</code> get* methods.
     * </p>
     * @param <T> The type of bean to create
     * @param rs ResultSet that supplies the bean data
     * @param type Class from which to create the bean instance
     * @throws java.sql.SQLException if a database access error occurs
     * @return the newly created List of beans
     */
    public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException {
        List<T> results = new ArrayList<T>();

        if (!rs.next()) {
            return results;
        }

        PropertyDescriptor[] props = propertyDescriptors(type);
        ResultSetMetaData rsmd = rs.getMetaData();
        int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);

        do {
            results.add(this.createBean(rs, type, props, columnToProperty));
        } while (rs.next());

        return results;
    }

    /**
     * 这里加入id的处理
     * Creates a new object and initializes its fields from the ResultSet.
     * @param <T> The type of bean to create
     * @param rs The result set.
     * @param type The bean type (the return type of the object).
     * @param props The property descriptors.
     * @param columnToProperty The column indices in the result set.
     * @return An initialized object.
     * @throws java.sql.SQLException if a database error occurs.
     */
    private <T> T createBean(ResultSet rs, Class<T> type,
            PropertyDescriptor[] props, int[] columnToProperty)
            throws SQLException {

        T bean = this.newInstance(type);

        for (int i = 1; i < columnToProperty.length; i++) {

            if (columnToProperty[i] == PROPERTY_NOT_FOUND) {
                continue;
            }

            PropertyDescriptor prop = props[columnToProperty[i]];
            Class<?> propType = prop.getPropertyType();

            Object value = this.processColumn(rs, i, propType);

            if (propType != null && value == null && propType.isPrimitive()) {
                value = primitiveDefaults.get(propType);
            }

            this.callSetter(bean, prop, value);
        }

        if(Model.class.isAssignableFrom(type)){ // 是否是model子类
            try {
                Field idField = type.getField("id"); //得到idField.
                long idVal = rs.getLong("id"); //得到id值
                idField.setAccessible(true);
                idField.set(bean, idVal);
            } catch (NoSuchFieldException e) {

            } catch (IllegalAccessException e) {

            }
        }

        return bean;
    }

    /**
     * Calls the setter method on the target object for the given property.
     * If no setter method exists for the property, this method does nothing.
     * @param target The object to set the property on.
     * @param prop The property to set.
     * @param value The value to pass into the setter.
     * @throws java.sql.SQLException if an error occurs setting the property.
     */
    private void callSetter(Object target, PropertyDescriptor prop, Object value)
            throws SQLException {

        Method setter = prop.getWriteMethod();

        if (setter == null) {
            return;
        }

        Class<?>[] params = setter.getParameterTypes();
        try {
            // convert types for some popular ones
            if (value instanceof java.util.Date) {
                final String targetType = params[0].getName();
                if ("java.sql.Date".equals(targetType)) {
                    value = new java.sql.Date(((java.util.Date) value).getTime());
                } else
                if ("java.sql.Time".equals(targetType)) {
                    value = new java.sql.Time(((java.util.Date) value).getTime());
                } else
                if ("java.sql.Timestamp".equals(targetType)) {
                    value = new java.sql.Timestamp(((java.util.Date) value).getTime());
                }
            }

            // Don't call setter if the value object isn't the right type
            if (this.isCompatibleType(value, params[0])) {
                setter.invoke(target, new Object[]{value});
            } else {
              throw new SQLException(
                  "Cannot set " + prop.getName() + ": incompatible types, cannot convert "
                  + value.getClass().getName() + " to " + params[0].getName());
                  // value cannot be null here because isCompatibleType allows null
            }

        } catch (IllegalArgumentException e) {
            throw new SQLException(
                "Cannot set " + prop.getName() + ": " + e.getMessage());

        } catch (IllegalAccessException e) {
            throw new SQLException(
                "Cannot set " + prop.getName() + ": " + e.getMessage());

        } catch (InvocationTargetException e) {
            throw new SQLException(
                "Cannot set " + prop.getName() + ": " + e.getMessage());
        }
    }

    /**
     * ResultSet.getObject() returns an Integer object for an INT column.  The
     * setter method for the property might take an Integer or a primitive int.
     * This method returns true if the value can be successfully passed into
     * the setter method.  Remember, Method.invoke() handles the unwrapping
     * of Integer into an int.
     *
     * @param value The value to be passed into the setter method.
     * @param type The setter's parameter type (non-null)
     * @return boolean True if the value is compatible (null => true)
     */
    private boolean isCompatibleType(Object value, Class<?> type) {
        // Do object check first, then primitives
        if (value == null || type.isInstance(value)) {
            return true;

        } else if (type.equals(Integer.TYPE) && Integer.class.isInstance(value)) {
            return true;

        } else if (type.equals(Long.TYPE) && Long.class.isInstance(value)) {
            return true;

        } else if (type.equals(Double.TYPE) && Double.class.isInstance(value)) {
            return true;

        } else if (type.equals(Float.TYPE) && Float.class.isInstance(value)) {
            return true;

        } else if (type.equals(Short.TYPE) && Short.class.isInstance(value)) {
            return true;

        } else if (type.equals(Byte.TYPE) && Byte.class.isInstance(value)) {
            return true;

        } else if (type.equals(Character.TYPE) && Character.class.isInstance(value)) {
            return true;

        } else if (type.equals(Boolean.TYPE) && Boolean.class.isInstance(value)) {
            return true;

        }
        return false;

    }

    /**
     * Returns a PropertyDescriptor[] for the given Class.
     *
     * @param c The Class to retrieve PropertyDescriptors for.
     * @return A PropertyDescriptor[] describing the Class.
     * @throws java.sql.SQLException if introspection failed.
     */
    private PropertyDescriptor[] propertyDescriptors(Class<?> c)
        throws SQLException {
        // Introspector caches BeanInfo classes for better performance
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(c);

        } catch (IntrospectionException e) {
            throw new SQLException(
                "MyBean introspection failed: " + e.getMessage());
        }

        return beanInfo.getPropertyDescriptors();
    }
}
