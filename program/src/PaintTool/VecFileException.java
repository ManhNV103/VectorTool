package PaintTool;

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