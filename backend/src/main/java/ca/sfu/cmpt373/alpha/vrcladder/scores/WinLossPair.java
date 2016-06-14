package ca.sfu.cmpt373.alpha.vrcladder.scores;

public class WinLossPair {
    private int wins;
    private int losses;

    WinLossPair(int wins, int losses) {
        this.wins = wins;
        this.losses = losses;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    @Override
    public boolean equals(Object otherWinLossPair) {
        if (this == otherWinLossPair) {
            return true;
        }

        if (otherWinLossPair == null || getClass() != otherWinLossPair.getClass()) {
            return false;
        }

        WinLossPair that = (WinLossPair) otherWinLossPair;
        if (wins != that.wins) {
            return false;
        }

        return losses == that.losses;

    }

    @Override
    public int hashCode() {
        int result = wins;
        result = 31 * result + losses;
        return result;
    }
}
