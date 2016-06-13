package ca.sfu.cmpt373.alpha.vrcladder.game.score;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class ScoreCard {
    static final String ERROR_TEAM_NOT_IN_ROUND = "team is not in round";
    static final String ERROR_MATCHGROUP_SIZE = "matchgroup must be of size 4";
    static final String ERROR_ROUNDS_OVER = "All round winners have been recorded";
    static final String ERROR_ROUNDS_NOT_OVER = "Not all rounds have been played yet";
    private static final String ERROR_TEAM_NOT_IN_GROUP = "Team is not in match group";

    @Id
    @Column(name = PersistenceConstants.COLUMN_ID)
    private String id;

    @OneToOne
    MatchGroup matchGroup;

    @javax.persistence.ManyToMany(cascade = CascadeType.ALL)
    @OrderColumn
    List<Team> roundWinners = new ArrayList<>();

    Integer currentRound = 1;

    ScoreCard(MatchGroup matchGroup) {
        id = new IdType().getId();
        this.matchGroup = matchGroup;
    }

    ScoreCard() {
        id = new IdType().getId();
        //for hibernate
    }

    private void checkTeamInMatchGroup(Team team) {
        boolean isTeamInGroup = false;
        for (Team groupTeam : matchGroup.getTeams()) {
            if (groupTeam.equals(team)) {
                isTeamInGroup = true;
            }
        }
        if (!isTeamInGroup) {
            throw new IllegalStateException(ERROR_TEAM_NOT_IN_GROUP);
        }
    }

    public Pair<Integer, Integer> getTeamWinsAndLosses(Team team) {
        if (currentRound <= getLastRound()) {
            throw new IllegalStateException(ERROR_ROUNDS_NOT_OVER);
        }
        checkTeamInMatchGroup(team);
        int wins = 0;
        int losses = 0;
        for (Team winner : roundWinners) {
            if (winner.equals(team)) {
                wins++;
            }
        }
        int gamesPlayedPerTeam = 2;
        losses = gamesPlayedPerTeam - wins;
        return new ImmutablePair<>(wins, losses);
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object otherScoreCard) {
        if (this == otherScoreCard) return true;

        if (otherScoreCard == null || getClass() != otherScoreCard.getClass()) {
            return false;
        }

        ScoreCard scoreCard = (ScoreCard) otherScoreCard;

        return id.equals(scoreCard.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public abstract void recordRoundWinner(Team team);
    public abstract List<Team> getRankedResults();
    public abstract int getLastRound();
}
