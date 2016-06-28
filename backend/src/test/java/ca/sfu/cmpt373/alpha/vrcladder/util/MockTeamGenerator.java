package ca.sfu.cmpt373.alpha.vrcladder.util;

import ca.sfu.cmpt373.alpha.vrcladder.teams.LadderPosition;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;

import java.util.ArrayList;
import java.util.List;

public class MockTeamGenerator {

    private static final Integer START_POSITION = 1;
    private static Integer position;

    static {
        position = START_POSITION;
    }

    public static Team generateTeam() {
        User player1 = MockUserGenerator.generatePlayer();
        User player2 = MockUserGenerator.generatePlayer();
        LadderPosition ladderPosition = new LadderPosition(position);
        position++;

        Team team = new Team(player1, player2, ladderPosition);

        return team;
    }

    public static List<Team> generateTeams(int numTeams) {
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < numTeams; i++) {
            teams.add(generateTeam());
        }
        return teams;
    }

    public static LadderPosition generateLadderPosition() {
        LadderPosition newLadderPosition = new LadderPosition(position);
        position++;

        return newLadderPosition;
    }

    public static void resetTeamCount() {
        position = START_POSITION;
    }

}
