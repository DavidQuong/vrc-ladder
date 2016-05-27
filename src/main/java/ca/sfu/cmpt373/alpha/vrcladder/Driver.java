package ca.sfu.cmpt373.alpha.vrcladder;

import ca.sfu.cmpt373.alpha.vrcladder.db.MockDatabase;
import ca.sfu.cmpt373.alpha.vrcladder.groups.Group;
import ca.sfu.cmpt373.alpha.vrcladder.groups.GroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;

import java.util.List;

public class Driver {

    public static void main(String[] args) {
        // Template code
        System.out.println("Hello, world!");
        testGroupGeneration();
    }

    //TODO: make real JUnit testcases
    private static void testGroupGeneration() {
        List<Group> groups = GroupGenerator.generateMatchGroupings(MockDatabase.getRankedLadderTeams(10));
        for (Group group : groups) {
            List<Team> teams = group.getTeams();
            for (Team team : teams) {
                System.out.println("Team " + team.getRanking());
            }
            System.out.println();
        }
    }
}
