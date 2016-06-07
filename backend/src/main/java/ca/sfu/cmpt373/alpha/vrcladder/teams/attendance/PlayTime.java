package ca.sfu.cmpt373.alpha.vrcladder.teams.attendance;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PlayTimeException;

public enum PlayTime {
    NONE(false, "N/A"),                  // Not Attending
    TIME_SLOT_A(true, "8:00 PM"),        // 8:00 PM
    TIME_SLOT_B(true, "9:30 PM");        // 9:30 PM

    private Boolean isPlayable;
    private String displayTime;

    PlayTime(boolean isPlayable, String displayTime) {
        this.isPlayable = isPlayable;
        this.displayTime = displayTime;
    }

    public boolean isPlayable() {
        return isPlayable;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    /**
     * @throws PlayTimeException if the play displayTime is not playable
     */
    public void checkPlayablePlayTime() {
        if (!this.isPlayable()) {
            throw new PlayTimeException(this);
        }
    }
}