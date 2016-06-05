package ca.sfu.cmpt373.alpha.vrcladder.teams.attendance;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PlayTimeException;

public enum PlayTime {
    NONE(false),              // Not Attending
    TIME_SLOT_A(true),        // 8:00 PM
    TIME_SLOT_B(true);        // 9:30 PM

    Boolean isPlayable;

    PlayTime(boolean isPlayable) {
        this.isPlayable = isPlayable;
    }

    public boolean isPlayable() {
        return isPlayable;
    }

    /**
     * @throws PlayTimeException if the play time is not playable
     */
    public void checkPlayablePlayTime() {
        if (!this.isPlayable()) {
            throw new PlayTimeException(this);
        }
    }
}
