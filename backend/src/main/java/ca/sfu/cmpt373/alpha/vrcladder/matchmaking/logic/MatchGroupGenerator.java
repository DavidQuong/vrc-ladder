package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MatchMakingException;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.WaitlistManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for generating the groups of teams that will play matches against each other each week
 */
public class MatchGroupGenerator {
    private static final String ERROR_MESSAGE    = "There are not enough teams to sort into groups of 3 or 4";
    private static final int MIN_REMAINING_TEAMS = 1;
    private static final int MAX_REMAINING_TEAMS = 2;

    private static WaitlistManager<Team> waitListManager = new WaitlistManager<>();

    /**
     * preconditions: teams are assumed to be in sorted ranked order
     * Generates groups of three or four teams to play matches against one another
     * @throws MatchMakingException if teams cannot be sorted into groups
     */
    public static List<MatchGroup> generateMatchGroupings(List<Team> teams, int maxTeams) {
        List<MatchGroup> results = new ArrayList<>();
        List<Team> attendingTeams = getAttendingTeams(teams);
        List<Team> teamsToGroup = new ArrayList<>();

        int currentGroupSize = decideCurrentGroupSize(maxTeams);
        int remainingTeams;
        boolean addToWaitlist = false;
        for(int counter = 0; counter < attendingTeams.size(); counter++) {

            addToWaitlist = (counter >= maxTeams);
            teamsToGroup.add(attendingTeams.get(counter));
            if(teamsToGroup.size() == currentGroupSize && !addToWaitlist) {
                results.add(new MatchGroup( teamsToGroup));
                remainingTeams = attendingTeams.size() - (counter + 1);
                currentGroupSize = decideCurrentGroupSize(remainingTeams);
                teamsToGroup.clear();
            }
        }

        if(!teamsToGroup.isEmpty()){
            if(addToWaitlist){
                for(Team team : teamsToGroup){
                    PlayTime time = team.getAttendanceCard().getPreferredPlayTime();
                    waitListManager.addToWaitlist(team, time);
                }
            }else{
                throw new MatchMakingException(ERROR_MESSAGE);
            }
        }
        return results;
    }

    private static int decideCurrentGroupSize(int teamsSize){
        int results = teamsSize % MatchGroup.MIN_NUM_TEAMS;
        int futureRemainingTeams = teamsSize - MatchGroup.MIN_NUM_TEAMS;
        if(results == 0){
            return MatchGroup.MIN_NUM_TEAMS;
        }else{
            if(futureRemainingTeams > MIN_REMAINING_TEAMS || futureRemainingTeams > MAX_REMAINING_TEAMS){
                return MatchGroup.MIN_NUM_TEAMS;
            }
        }
        return MatchGroup.MAX_NUM_TEAMS;
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
