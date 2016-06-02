package ca.sfu.cmpt373.alpha.vrcladder.exceptions;

public class CourtsFullException extends BaseException {
    private static final String DEFAULT_ERROR_MESSAGE = "There were not enough courts available to schedule all matches";
    public CourtsFullException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public CourtsFullException(String message) {
        super(message);
    }
}
