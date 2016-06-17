package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ScoreCard {
    
    private static final String ERROR_TEAM_NOT_IN_GROUP = "Team is not in match group";
    private static final String ERROR_ILLEGAL_SIZE = "Ranked teams list must be the same size as MatchGroup teams";

    @EmbeddedId
    private GeneratedId id;

    @OneToOne
    private MatchGroup matchGroup;

    @OneToMany
    @OrderColumn
    private List<Team> rankedTeams = new ArrayList<>();

    private ScoreCard() {
        // Required by Hibernate.
    }

    public ScoreCard(MatchGroup matchGroup) {
        this.id = new GeneratedId();
        this.matchGroup = matchGroup;
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
     * @return A list of teams in the order they came in in their matches.
     */
    public List<Team> getRankedTeams() {
        return rankedTeams;
    }

    public MatchGroup getMatchGroup() {
        return matchGroup;
    }

    public GeneratedId getId() {
        return id;
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        ScoreCard otherScoreCard = (ScoreCard) otherObj;

        return id.equals(otherScoreCard.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
