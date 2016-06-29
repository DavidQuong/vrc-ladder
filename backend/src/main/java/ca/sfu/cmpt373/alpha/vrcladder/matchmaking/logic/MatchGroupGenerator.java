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
    public static List<MatchGroup> generateMatchGroupings(List<Team> teams, List<Team> waitList, int numberOfGroups) {
        List<MatchGroup> results = new ArrayList<>();
        List<Team> attendingTeams = getAttendingTeams(teams);
        List<Team> teamsToGroup = new ArrayList<>();

        int deductedTeams = 0;
        int teamsTotal = attendingTeams.size();

        for(int counter = 0; counter < numberOfGroups; counter++) {
            int currentGroupSize;
            int remainingGroups = (numberOfGroups - counter);
            int remainingTeams = (teamsTotal - deductedTeams);
            if(remainingTeams < MatchGroup.MIN_NUM_TEAMS){
                break;
            }

            currentGroupSize = decideCurrentTeam(remainingGroups, remainingTeams);
            createGroup(currentGroupSize, deductedTeams, teams, teamsToGroup);
            deductedTeams = deductedTeams + currentGroupSize;
            results.add(new MatchGroup( teamsToGroup));
            teamsToGroup.clear();
        }

        if(results.size() == 0){
            throw new MatchMakingException(ERROR_MESSAGE);
        }

        addToWaitList(deductedTeams, teams, waitList);
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

    private static int decideCurrentTeam(int remainingGroups, int remainingTeams){
        int results;
        if((MatchGroup.MAX_NUM_TEAMS * remainingGroups) <= remainingTeams){
            results = MatchGroup.MAX_NUM_TEAMS;
        }else{
            if(remainingTeams % MatchGroup.MAX_NUM_TEAMS == 0 && ((remainingTeams - MatchGroup.MAX_NUM_TEAMS) == 0 || (remainingTeams - MatchGroup.MAX_NUM_TEAMS) == 4)){
                results = MatchGroup.MAX_NUM_TEAMS;
            }else{
                results = MatchGroup.MIN_NUM_TEAMS;
            }
        }
        return results;
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

    private static void addToWaitList(int deductedTeams, List<Team> teams, List<Team> waitList){
        List<Team> attendingTeams = getAttendingTeams(teams);
        int teamsTotal = attendingTeams.size();
        if(deductedTeams < teamsTotal){
            for(int counter = deductedTeams; counter < teamsTotal; counter++){
                waitList.add(attendingTeams.get(counter));
            }
        }
    }
}
