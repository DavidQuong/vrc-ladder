package ca.sfu.cmpt373.alpha.vrcladder.exceptions;

/**
 * An exception for field validation on data fields.
 * An example use case would be verifying that a Team object has two different players. Otherwise, this exception would be thrown
 * This is used by the REST API to catch and handle any validation errors that may occur when receiving data from a client
 */
public class ValidationException extends BaseException {

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

}