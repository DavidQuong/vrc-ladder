package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class ScoreCard {
    private static final String ERROR_TEAM_NOT_IN_GROUP = "Team is not in match group";
    private static final String ERROR_ILLEGAL_SIZE = "Ranked teams list must be the same size as MatchGroup teams";

    @Id
    @Column(name = PersistenceConstants.COLUMN_ID)
    private String id;

    @OneToOne
    private MatchGroup matchGroup;

    @javax.persistence.ManyToMany
    @OrderColumn
    private List<Team> rankedTeams = new ArrayList<>();

    public ScoreCard(MatchGroup matchGroup) {
        id = new IdType().getId();
        this.matchGroup = matchGroup;
    }

    private ScoreCard() {
        //for hibernate
    }

    public void setRankedTeams(List<Team> rankedTeams) {
        for (Team team : rankedTeams) {
            checkTeamInMatchGroup(team);
        }
        if (rankedTeams.size() > matchGroup.getTeamCount()
                || rankedTeams.size() < matchGroup.getTeamCount()) {
            throw new IllegalStateException(ERROR_ILLEGAL_SIZE);
        }
        this.rankedTeams = rankedTeams;
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

    /**
     * @return a list of teams in the order they came in in their matches
     */
    public List<Team> getRankedTeams() {
        return this.rankedTeams;
    }

    public MatchGroup getMatchGroup() {
        return this.matchGroup;
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
}
