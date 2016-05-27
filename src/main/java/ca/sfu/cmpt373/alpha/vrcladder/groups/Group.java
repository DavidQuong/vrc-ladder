package ca.sfu.cmpt373.alpha.vrcladder.groups;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Trevor on 5/26/2016.
 * A class for sorting teams into groups that will play against each other
 * Groups are strictly limited to 3 or 4 teams
 */
public class Group {
    private static final int MIN_NUM_TEAMS = 3;
    private static final int MAX_NUM_TEAMS = 4;

    private final List<Team> teams;

    public Group (Team team1, Team team2, Team team3) {
        this.teams = Arrays.asList(team1, team2, team3);
    }

    public Group (Team team1, Team team2, Team team3, Team team4) {
        this.teams = Arrays.asList(team1, team2, team3, team4);
    }

    /**
     * @throws IllegalStateException if the list contains more or less than the min/max amount of permissible teams
     */
    Group(List<Team> teams) {
        if (teams.size() < MIN_NUM_TEAMS || teams.size() > MAX_NUM_TEAMS) {
            throw new IllegalStateException("Teams list contains more or less than the min or max number of permissible teams");
        }
        this.teams = teams;
    }

    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }
}
