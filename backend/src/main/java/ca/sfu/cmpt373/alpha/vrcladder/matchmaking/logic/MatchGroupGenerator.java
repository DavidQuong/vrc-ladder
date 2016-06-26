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
    private static final String ERROR_MESSAGE = "There are not enough attending teams to sort into groups of 3 or 4";

    /**
     * preconditions: teams are assumed to be in sorted ranked order
     * Generates groups of three or four teams to play matches against one another
     * @throws MatchMakingException if teams cannot be sorted into groups
     */
    public static List<MatchGroup> generateMatchGroupings(List<Team> teams) {
        List<MatchGroup> matchGroupings = new ArrayList<>();

        List<Team> attendingTeams = getAttendingTeams(teams);
        List<Team> teamsToGroup = new ArrayList<>();

        //figure out how many teams won't fit into groups of three
        int groupCount = MatchGroup.MIN_NUM_TEAMS;
        int extraTeams = attendingTeams.size() % groupCount;

        //while there are teams that won't fit into groups of three, make groups of four instead
        if (extraTeams > 0) {
            groupCount = MatchGroup.MAX_NUM_TEAMS;
        }

        //make groups starting from the bottom of the list so that groups of four are created from the lowest ranked players
        for (int i = attendingTeams.size() - 1; i >= 0; i--) {
            teamsToGroup.add(attendingTeams.get(i));
            if (teamsToGroup.size() == groupCount) {
                //reverse teamsToGroup before adding them to a group, so that they are in ranked order
                Collections.reverse(teamsToGroup);
                matchGroupings.add(new MatchGroup(teamsToGroup));
                teamsToGroup.clear();
                extraTeams--;
                if (extraTeams == 0) {
                    //switch back to groups of three
                    groupCount = MatchGroup.MIN_NUM_TEAMS;
                }
            }
        }

        //put match groupings in sorted order, since they were generated backwards
        Collections.reverse(matchGroupings);

        //this only happens if there are less than three teams, or five teams
        if (extraTeams > 0) {
            throw new MatchMakingException(ERROR_MESSAGE);
        }
        return matchGroupings;
    }

    private static List<Team> getAttendingTeams (List<Team> teams) {
        List<Team> attendingTeams = new ArrayList<>();
        for (Team team : teams) {
            if (team.getAttendanceCard().isAttending()) {
                attendingTeams.add(team);
            }
        }
        if (attendingTeams.size() == 0) {
            throw new MatchMakingException(ERROR_MESSAGE);
        }
        return attendingTeams;
    }
}
