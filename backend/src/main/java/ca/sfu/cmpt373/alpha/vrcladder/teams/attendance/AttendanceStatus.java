package ca.sfu.cmpt373.alpha.vrcladder.teams.attendance;

public enum AttendanceStatus {
    PRESENT(0),
    LATE(4),
    NO_SHOW(10);

    private int attendancePenalty;

    AttendanceStatus(int attendancePenalty) {
        this.attendancePenalty = attendancePenalty;
    }

    public int getPenalty() {
        return attendancePenalty;
    }
}
