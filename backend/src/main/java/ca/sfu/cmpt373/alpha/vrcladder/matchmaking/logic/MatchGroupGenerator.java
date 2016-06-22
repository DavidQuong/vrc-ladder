package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MatchMakingException;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class for generating the groups of teams that will play matches against each other each week
 */
public class MatchGroupGenerator {
    private static final String ERROR_MESSAGE = "There are not enough teams to sort into groups of 3 or 4";

    /**
     * preconditions: teams are assumed to be in sorted ranked order
     * Generates groups of three or four teams to play matches against one another
     * @throws MatchMakingException if teams cannot be sorted into groups
     */
    public static List<MatchGroup> generateMatchGroupings(List<Team> teams) {
        List<MatchGroup> results = new ArrayList<>();

        List<Team> attendingTeams = getAttendingTeams(teams);
        List<Team> teamsToGroup = new ArrayList<>();

        int currentGroupSize = decideCurrentGroupSize(attendingTeams.size());
        for(int counter = 0; counter < attendingTeams.size(); counter++) {
            teamsToGroup.add(attendingTeams.get(counter));
            if(teamsToGroup.size() == currentGroupSize) {
                results.add(new MatchGroup( teamsToGroup));
                int remainingTeams = attendingTeams.size() - (counter + 1);
                currentGroupSize = decideCurrentGroupSize(remainingTeams);
                teamsToGroup.clear();
            }
        }

        if(!teamsToGroup.isEmpty()){
            throw new MatchMakingException(ERROR_MESSAGE);
        }

        return results;
    }

    private static int decideCurrentGroupSize(int teamSize){
        if( (teamSize % MatchGroup.MAX_NUM_TEAMS) == 0 || teamSize > MatchGroup.MAX_NUM_TEAMS){
            return MatchGroup.MAX_NUM_TEAMS;
        }
        return MatchGroup.MIN_NUM_TEAMS;
    }

    private static List<Team> getAttendingTeams (List<Team> teams) {
        List<Team> attendingTeams = new ArrayList<>();
        for (Team team : teams) {
            if (team.getAttendanceCard().isAttending()) {
                attendingTeams.add(team);
            }
        }
        return attendingTeams;
    }
}
