package it.unicam.cs.mpgc.jtime119159.service.exception;

/**
 * Eccezione unchecked per errori di logica di business.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
