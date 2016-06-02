package ca.sfu.cmpt373.alpha.vrcladder.exceptions;

import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;

public class NoMatchScheduledException extends BaseException {
    public NoMatchScheduledException(PlayTime playTime) {
        super("No match scheduled for play time " + playTime);
    }

    public NoMatchScheduledException(String message) {
        super(message);
    }
}
