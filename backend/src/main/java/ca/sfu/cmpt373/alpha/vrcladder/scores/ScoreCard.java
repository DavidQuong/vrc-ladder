package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import javax.persistence.Embedded;
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

    @Id
    @Embedded
    private IdType id;

    @OneToOne
    private MatchGroup matchGroup;

    @OneToMany
    @OrderColumn
    private List<Team> rankedTeams = new ArrayList<>();

    public ScoreCard(MatchGroup matchGroup) {
        id = new IdType();
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

    public IdType getId() {
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
