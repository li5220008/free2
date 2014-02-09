package dbutils.spring;

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link dbutils.spring.SqlParameterSource} implementation that holds a given Map of parameters.
 *
 * <p>This class is intended for passing in a simple Map of parameter values
 * to the methods of the {@link NamedParameterJdbcTemplate} class.
 *
 * <p>The {@code addValue} methods on this class will make adding several
 * values easier. The methods return a reference to the {@link MapSqlParameterSource}
 * itself, so you can chain several method calls together within a single statement.
 *
 * @author Thomas Risberg
 * @author Juergen Hoeller
 * @since 2.0
 * @see #addValue(String, Object)
 * @see #addValue(String, Object, int)
 * @see #registerSqlType
 * @see NamedParameterJdbcTemplate
 */
public class MapSqlParameterSource extends AbstractSqlParameterSource {

	private final Map<String, Object> values = new HashMap<String, Object>();


	/**
	 * Create an empty MapSqlParameterSource,
	 * with values to be added via {@code addValue}.
	 * @see #addValue(String, Object)
	 */
	public MapSqlParameterSource() {
	}

	/**
	 * Create a new MapSqlParameterSource, with one value
	 * comprised of the supplied arguments.
	 * @param paramName the name of the parameter
	 * @param value the value of the parameter
	 * @see #addValue(String, Object)
	 */
	public MapSqlParameterSource(String paramName, Object value) {
		addValue(paramName, value);
	}

	/**
	 * Create a new MapSqlParameterSource based on a Map.
	 * @param values a Map holding existing parameter values (can be {@code null})
	 */
	public MapSqlParameterSource(Map<String, ?> values) {
		addValues(values);
	}


	/**
	 * Add a parameter to this parameter source.
	 * @param paramName the name of the parameter
	 * @param value the value of the parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 */
	public MapSqlParameterSource addValue(String paramName, Object value) {
        Preconditions.checkNotNull(paramName);
		this.values.put(paramName, value);
		if (value instanceof SqlParameterValue) {
			registerSqlType(paramName, ((SqlParameterValue) value).getSqlType());
		}
		return this;
	}

	/**
	 * Add a parameter to this parameter source.
	 * @param paramName the name of the parameter
	 * @param value the value of the parameter
	 * @param sqlType the SQL type of the parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 */
	public MapSqlParameterSource addValue(String paramName, Object value, int sqlType) {
        Preconditions.checkNotNull(paramName);
		this.values.put(paramName, value);
		registerSqlType(paramName, sqlType);
		return this;
	}

	/**
	 * Add a parameter to this parameter source.
	 * @param paramName the name of the parameter
	 * @param value the value of the parameter
	 * @param sqlType the SQL type of the parameter
	 * @param typeName the type name of the parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 */
	public MapSqlParameterSource addValue(String paramName, Object value, int sqlType, String typeName) {
        Preconditions.checkNotNull(paramName);
		this.values.put(paramName, value);
		registerSqlType(paramName, sqlType);
		registerTypeName(paramName, typeName);
		return this;
	}

	/**
	 * Add a Map of parameters to this parameter source.
	 * @param values a Map holding existing parameter values (can be {@code null})
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 */
	public MapSqlParameterSource addValues(Map<String, ?> values) {
		if (values != null) {
			for (Map.Entry<String, ?> entry : values.entrySet()) {
				this.values.put(entry.getKey(), entry.getValue());
				if (entry.getValue() instanceof SqlParameterValue) {
					SqlParameterValue value = (SqlParameterValue) entry.getValue();
					registerSqlType(entry.getKey(), value.getSqlType());
				}
			}
		}
		return this;
	}

	/**
	 * Expose the current parameter values as read-only Map.
	 */
	public Map<String, Object> getValues() {
		return Collections.unmodifiableMap(this.values);
	}


	public boolean hasValue(String paramName) {
		return this.values.containsKey(paramName);
	}

	public Object getValue(String paramName) {
		if (!hasValue(paramName)) {
			throw new IllegalArgumentException("No value registered for key '" + paramName + "'");
		}
		return this.values.get(paramName);
	}

}

