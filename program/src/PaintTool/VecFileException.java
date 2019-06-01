/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */

package PaintTool;

/**
 * Class that extends the Exception to implement Exception in the application
 */

public class VecFileException extends Exception {
    public VecFileException() {
        super();
    }

    public VecFileException(String message) {
        super(message);
    }

    public VecFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public VecFileException(Throwable cause) {
        super(cause);
    }
}