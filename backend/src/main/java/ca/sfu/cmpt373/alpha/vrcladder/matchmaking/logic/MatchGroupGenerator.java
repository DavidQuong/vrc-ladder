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
    private static List<Team> waitlist = new ArrayList<>();

    /**
     * preconditions: teams are assumed to be in sorted ranked order
     * Generates groups of three or four teams to play matches against one another
     * @throws MatchMakingException if teams cannot be sorted into groups
     */
    public static List<MatchGroup> generateMatchGroupings(List<Team> teams, int numberOfGroups) {
        List<MatchGroup> results = new ArrayList<>();
        List<Team> attendingTeams = getAttendingTeams(teams);
        List<Team> teamsToGroup = new ArrayList<>();

        int deductedTeams = 0;
        int teamsTotal = attendingTeams.size();
        if(teamsTotal == 0) {
            throw new MatchMakingException(ERROR_MESSAGE);
        }

        for(int counter = 0; counter < numberOfGroups; counter++) {
            int currentGroupSize;
            int remainingGroups = (numberOfGroups - counter);
            int remainingTeams = (teamsTotal - deductedTeams);

            if((MatchGroup.MAX_NUM_TEAMS * remainingGroups) <= remainingTeams){
                currentGroupSize = MatchGroup.MAX_NUM_TEAMS;
            }else{
                currentGroupSize = MatchGroup.MIN_NUM_TEAMS;
            }

            for(int subCounter = 0; subCounter < currentGroupSize; subCounter++){
                int teamIndex = (subCounter + deductedTeams);
                if(teamIndex < attendingTeams.size()){
                    Team currentTeam = attendingTeams.get(teamIndex);
                    teamsToGroup.add(currentTeam);
                }else{
                    throw new MatchMakingException(ERROR_MESSAGE);
                }
            }

            deductedTeams = deductedTeams + currentGroupSize;
            results.add(new MatchGroup( teamsToGroup));
            teamsToGroup.clear();
        }

        if(!teamsToGroup.isEmpty()){
            throw new MatchMakingException(ERROR_MESSAGE);
        }

        if(deductedTeams < teamsTotal){
            for(int counter = deductedTeams; counter < teamsTotal; counter++){
                waitlist.add(attendingTeams.get(counter));
            }
        }
        return results;
    }

    public static List<Team> getWaitList(){
        return waitlist;
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
