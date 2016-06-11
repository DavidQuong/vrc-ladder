package ca.sfu.cmpt373.alpha.vrcladder.util;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;

public class MockTeamGenerator {

    public static Team generateTeam() {
        User player1 = MockUserGenerator.generatePlayer();
        User player2 = MockUserGenerator.generatePlayer();

        Team team = new Team(player1, player2);

        return team;
    }

}
