package ca.sfu.cmpt373.alpha.vrcladder.groups;

import ca.sfu.cmpt373.alpha.vrcladder.db.MockDatabase;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import org.junit.Test;

import java.util.List;

public class GroupGeneratorTest {
    @Test
    public void testGroupGeneration() {
        //TODO: make real assertions
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
