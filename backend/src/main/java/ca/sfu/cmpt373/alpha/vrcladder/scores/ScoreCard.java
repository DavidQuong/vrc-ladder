package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ScoreCard {

    private static final String ERROR_TEAM_NOT_ATTENDING = "Teams that did not attend should not change rankings within their matchgroups";
    private static final String ERROR_TEAM_NOT_IN_GROUP = "Team is not in match group";
    private static final String ERROR_ILLEGAL_SIZE = "Ranked teams list must be the same size as MatchGroup teams";
    private static final String ERROR_NO_RESULTS_SET = "There are no results recorded on this score card";
    private static final String ERROR_DUPLICATE_TEAMS = "There cannot be duplicate teams in the ScoreCard";

    @EmbeddedId
    private GeneratedId id;

    @OneToOne
    private MatchGroup matchGroup;

    @OneToMany (fetch = FetchType.EAGER)
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

        checkTeamsUnique(rankedTeams);

        if (rankedTeams.size() != matchGroup.getTeamCount()) {
            throw new IllegalStateException(ERROR_ILLEGAL_SIZE);
        }

        checkNonAttendingTeamsStayedTheSame(rankedTeams);

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

    private void checkNonAttendingTeamsStayedTheSame(List<Team> rankedTeams) {
        for (int i = 0; i < rankedTeams.size(); i++) {
            AttendanceCard attendanceCard = rankedTeams.get(i).getAttendanceCard();
            boolean isTeamPresent = attendanceCard.isPresent();
            if (!isTeamPresent) {
                boolean isTeamPositionDifferent = !rankedTeams.get(i).equals(matchGroup.getTeams().get(i));
                if (isTeamPositionDifferent) {
                    throw new IllegalStateException(ERROR_TEAM_NOT_ATTENDING);
                }
            }
        }
    }

    private void checkTeamsUnique(List<Team> teams) {
        for (int i = 0; i < teams.size(); i++) {
            Team currTeam = teams.get(i);
            for (int j = i + 1; j < teams.size(); j++) {
                Team otherTeam = teams.get(j);
                if (currTeam.equals(otherTeam)) {
                    throw new IllegalStateException(ERROR_DUPLICATE_TEAMS);
                }
            }
        }
    }

    /**
     * @return A list of teams in the order they came in in their matches.
     */
    public List<Team> getRankedTeams() {
        if (rankedTeams.size() != matchGroup.getTeamCount()) {
            throw new IllegalStateException(ERROR_NO_RESULTS_SET);
        }
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
