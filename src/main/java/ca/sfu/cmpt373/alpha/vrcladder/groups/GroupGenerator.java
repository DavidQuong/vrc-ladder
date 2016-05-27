package ca.sfu.cmpt373.alpha.vrcladder.groups;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Trevor on 5/26/2016.
 * A class for generating the groups of teams that will play matches against each other each week
 */
public class GroupGenerator {
    /**
     * preconditions: teams are assumed to be in sorted ranked order
     * Generates groups of three or four teams to play matches against one another
     */
    public static List<Group> generateMatchGroupings(List<Team> teams) {
        //TODO: figure out requirements on how to deal with different prefered time slots
        //if (team.getAttendanceInfo().getPreferedTimeSlot() == AttendanceInfo.TimeSlot.FIRST)

        List<Group> matchGroupings = new ArrayList<>();

        List<Team> attendingTeams = getAttendingTeams(teams);
        List<Team> teamsToGroup = new ArrayList<>();

        //figure out how many teams won't fit into groups of three
        int idealTeamCount = 3;
        int extraTeams = attendingTeams.size() % idealTeamCount;

        //while there are teams that won't fit into groups of three, make groups of four instead
        if (extraTeams > 0) {
            idealTeamCount = 4;
        }

        //make groups starting from the bottom of the list so that groups of four are created from the lowest ranked players
        for (int i = attendingTeams.size() - 1; i >= 0; i--) {
            teamsToGroup.add(attendingTeams.get(i));
            if (teamsToGroup.size() == idealTeamCount) {
                //reverse teamsToGroup before adding them to a group, so that they are in ranked order
                Collections.reverse(teamsToGroup);
                matchGroupings.add(new Group(teamsToGroup));
                teamsToGroup = new ArrayList<>();
                extraTeams--;
                if (extraTeams == 0) {
                    //switch back to groups of three
                    idealTeamCount = 3;
                }
            }
        }

        //put match groupings in sorted order, since they were generated backwards
        Collections.reverse(matchGroupings);

        //this only happens if there are less than three teams, or five teams
        //TODO: handle this error more gracefully
        if (extraTeams > 0) {
            throw new IllegalStateException("There are not enough teams to sort into groups of 3 or 4");
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
        return attendingTeams;
    }
}
