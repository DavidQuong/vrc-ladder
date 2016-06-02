package ca.sfu.cmpt373.alpha.vrcladder.exceptions;

import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;

public class UnplayablePlayTimeException extends BaseException {

    public UnplayablePlayTimeException(PlayTime playTime) {
        super("Play time " + playTime + " is unplayable");
    }

    public UnplayablePlayTimeException(String message) {
        super(message);
    }
}
