package ca.sfu.cmpt373.alpha.vrcladder;

import ca.sfu.cmpt373.alpha.vrcladder.ladder.Ladder;
import ca.sfu.cmpt373.alpha.vrcladder.ladder.LadderTestHelper;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Driver {

    private static List<Team> teams;

    public static void main(String[] args) {
        // Template code

        System.out.printf("%s%n", "Printing...");
        teams = new ArrayList<>();

        Ladder ladder = new Ladder(teams);

        for(int i = 0; i < 10; i++){
        }

        for(int i = 0; i < 3; i++){
            ladder.pushTeam(null);
        }


        ///print the ladder
/*        List<Team> tempLadder = ladder.getLadder();
        for(Team team:tempLadder){
            System.out.println(team.getTeamID());
        }*/

        System.out.println(ladder.getLadderVolume());
        System.out.println(ladder.getLadderTeamCount());

        LadderTestHelper ladderTestHelper = new LadderTestHelper();
        ladderTestHelper.generateTeam();

    }

}
