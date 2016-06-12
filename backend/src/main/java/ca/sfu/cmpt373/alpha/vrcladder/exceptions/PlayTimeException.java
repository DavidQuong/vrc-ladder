package ca.sfu.cmpt373.alpha.vrcladder.exceptions;

import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;

public class PlayTimeException extends BaseException {

    public PlayTimeException(PlayTime playTime) {
        super("Play time " + playTime + " is unplayable");
    }

    public PlayTimeException(String message) {
        super(message);
    }

}
