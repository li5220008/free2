package dbutils.spring;

/**
 * A simple empty implementation of the {@link dbutils.spring.SqlParameterSource} interface.
 *
 * @author Juergen Hoeller
 * @since 3.2.2
 */
public class EmptySqlParameterSource implements SqlParameterSource {

	/**
	 * A shared instance of {@link EmptySqlParameterSource}.
	 */
	public static final EmptySqlParameterSource INSTANCE = new EmptySqlParameterSource();


	public boolean hasValue(String paramName) {
		return false;
	}

	public Object getValue(String paramName) throws IllegalArgumentException {
		throw new IllegalArgumentException("This SqlParameterSource is empty");
	}

	public int getSqlType(String paramName) {
		return TYPE_UNKNOWN;
	}

	public String getTypeName(String paramName) {
		return null;
	}

}
