package dbutils.spring;

/**
 * User: wenzhihong
 * Date: 13-4-28
 * Time: 下午3:15
 */
public class InvalidDataAccessApiUsageException extends RuntimeException {
    /**
     * Constructor for InvalidDataAccessApiUsageException.
     *
     * @param msg the detail message
     */
    public InvalidDataAccessApiUsageException(String msg) {
        super(msg);
    }

    /**
     * Constructor for InvalidDataAccessApiUsageException.
     *
     * @param msg   the detail message
     * @param cause the root cause from the data access API in use
     */
    public InvalidDataAccessApiUsageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
