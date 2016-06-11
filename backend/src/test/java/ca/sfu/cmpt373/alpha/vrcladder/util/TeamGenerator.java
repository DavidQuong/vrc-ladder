package ca.sfu.cmpt373.alpha.vrcladder.util;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;

public class TeamGenerator {

    public static Team generateTeam() {
        User player1 = UserGenerator.generatePlayer();
        User player2 = UserGenerator.generatePlayer();

        Team team = new Team(player1, player2);

        return team;
    }

}
