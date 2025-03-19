package org.midnightbsd.appstore.exception;

public class MagusFetchException extends RuntimeException {
    public MagusFetchException(String message) {
        super(message);
    }

    public MagusFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}