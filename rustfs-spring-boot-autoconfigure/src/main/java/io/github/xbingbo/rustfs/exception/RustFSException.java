package io.github.xbingbo.rustfs.exception;

public class RustFSException extends RuntimeException {
    public RustFSException(String message) {
        super(message);
    }

    public RustFSException(String message, Throwable cause) {
        super(message, cause);
    }

    public RustFSException(RustFSErrorCode code) {
        super(code.getMessage());
    }

    public RustFSException(RustFSErrorCode code, Throwable cause) {
        super(code.getMessage(), cause);
    }
}
