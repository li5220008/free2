package exceptions;

/**
 * User: liuhongjiang
 * Date: 13-11-4
 * Time: 下午4:15
 * 功能说明:
 */
public class IquantEntityHasBeenUsedException extends RuntimeException {
    public IquantEntityHasBeenUsedException(String msg) {
        super(msg);
    }
}
