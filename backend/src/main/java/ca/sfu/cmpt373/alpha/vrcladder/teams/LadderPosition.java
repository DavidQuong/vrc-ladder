package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ValidationException;

public class LadderPosition {

    private static final String INVALID_POSITION_FORMAT = "Invalid valid rank position: %s.";

    private Integer position;

    public LadderPosition(Integer position) {
        if (position <= 0) {
            String errorMsg = String.format(INVALID_POSITION_FORMAT, position);
            throw new ValidationException(errorMsg);
        }

        this.position = position;
    }

    public Integer getPosition() {
        return position;
    }

}
