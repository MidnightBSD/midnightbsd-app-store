package org.midnightbsd.appstore.services;

/**
 * @author Lucas Holt
 */
public class ServiceException extends Exception {
    private static final long serialVersionUID = 8911047285453302885L;

    public ServiceException() {
        super();
    }

    public ServiceException(Exception e) {
        super(e);
    }

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String msg, Exception e) {
        super(msg, e);
    }

    @Override
    public String getMessage() {
        return "ServiceException: " + super.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return "ServiceException: " + super.getLocalizedMessage();
    }
}
