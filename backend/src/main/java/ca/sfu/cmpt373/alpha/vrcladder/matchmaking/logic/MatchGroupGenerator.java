package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MatchMakingException;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for generating the groups of teams that will play matches against each other each week
 */
public class MatchGroupGenerator {
    private static final String ERROR_MESSAGE = "There are not enough attending teams to sort into groups of 3 or 4";

    /**
     * preconditions: teams are assumed to be in sorted ranked order
     * Generates groups of three or four teams to play matches against one another
     * @throws MatchMakingException if teams cannot be sorted into groups
     */
    public static List<MatchGroup> generateMatchGroupings(List<Team> teams) {
        List<MatchGroup> results = new ArrayList<>();
        List<Team> attendingTeams = getAttendingTeams(teams);
        List<Team> teamsToGroup = new ArrayList<>();

        int deductedTeamsCount = 0;
        int teamsTotalCount = attendingTeams.size();

        while(true) {
            int currentGroupSize;
            int remainingTeamsCount = (teamsTotalCount - deductedTeamsCount);
            if(remainingTeamsCount < MatchGroup.MIN_NUM_TEAMS){
                break;
            }

            currentGroupSize = decideCurrentTeam(remainingTeamsCount);
            createGroup(currentGroupSize, deductedTeamsCount, teams, teamsToGroup);
            deductedTeamsCount = deductedTeamsCount + currentGroupSize;
            results.add(new MatchGroup(teamsToGroup));
            teamsToGroup.clear();
        }

        if(attendingTeams.size() > deductedTeamsCount){
            throw new MatchMakingException(ERROR_MESSAGE);
        }

        return results;
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

    private static int decideCurrentTeam(int remainingTeams){
        if(remainingTeams % MatchGroup.MAX_NUM_TEAMS == 0 && ((remainingTeams - MatchGroup.MAX_NUM_TEAMS) == 0 || (remainingTeams - MatchGroup.MAX_NUM_TEAMS) == 4)){
           return MatchGroup.MAX_NUM_TEAMS;
        } else {
           return MatchGroup.MIN_NUM_TEAMS;
        }
    }

    private static void createGroup(int currentGroupSize, int deductedTeams, List<Team> teams, List<Team> teamsToGroup){
        List<Team> attendingTeams = getAttendingTeams(teams);
        for(int subCounter = 0; subCounter < currentGroupSize; subCounter++){
            int teamIndex = (subCounter + deductedTeams);
            if(teamIndex < attendingTeams.size()){
                Team currentTeam = attendingTeams.get(teamIndex);
                teamsToGroup.add(currentTeam);
            }
        }
    }
}
