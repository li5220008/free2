package dbutils.spring;

import com.google.gdata.util.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for {@link dbutils.spring.SqlParameterSource} implementations.
 * Provides registration of SQL types per parameter.
 *
 * @author Juergen Hoeller
 * @since 2.0
 */
public abstract class AbstractSqlParameterSource implements SqlParameterSource {

	private final Map<String, Integer> sqlTypes = new HashMap<String, Integer>();

	private final Map<String, String> typeNames = new HashMap<String, String>();


	/**
	 * Register a SQL type for the given parameter.
	 * @param paramName the name of the parameter
	 * @param sqlType the SQL type of the parameter
	 */
	public void registerSqlType(String paramName, int sqlType) {
        Preconditions.checkNotNull(paramName);
		this.sqlTypes.put(paramName, sqlType);
	}

	/**
	 * Register a SQL type for the given parameter.
	 * @param paramName the name of the parameter
	 * @param typeName the type name of the parameter
	 */
	public void registerTypeName(String paramName, String typeName) {
        Preconditions.checkNotNull(paramName);
		this.typeNames.put(paramName, typeName);
	}

	/**
	 * Return the SQL type for the given parameter, if registered.
	 * @param paramName the name of the parameter
	 * @return the SQL type of the parameter,
	 * or {@code TYPE_UNKNOWN} if not registered
	 */
	public int getSqlType(String paramName) {
        Preconditions.checkNotNull(paramName);
		Integer sqlType = this.sqlTypes.get(paramName);
		if (sqlType != null) {
			return sqlType;
		}
		return TYPE_UNKNOWN;
	}

	/**
	 * Return the type name for the given parameter, if registered.
	 * @param paramName the name of the parameter
	 * @return the type name of the parameter,
	 * or {@code null} if not registered
	 */
	public String getTypeName(String paramName) {
        Preconditions.checkNotNull(paramName);
		return this.typeNames.get(paramName);
	}

}

