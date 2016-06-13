package ca.sfu.cmpt373.alpha.vrcladder.game.score;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

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
}
