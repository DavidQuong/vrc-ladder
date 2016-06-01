package ca.sfu.cmpt373.alpha.vrcladder;

import ca.sfu.cmpt373.alpha.vrcladder.ladder.Ladder;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;

import java.util.ArrayList;
import java.util.List;

public class Driver {

    private static List<Team> teams;

    public static void main(String[] args) {
        // Template code

        System.out.printf("%s%n", "Printing...");
        teams = new ArrayList<>();

        Ladder ladder = new Ladder(teams);

        TeamGenerator teamGenerator = new TeamGenerator();
        for (int i = 0; i < 4; i++) {
            ladder.pushTeam(teamGenerator.generateTeam());
        }

        //ladder.insertTeamAtPosition(4, null);
        System.out.printf("%s", ladder.toString());

        ladder.swapTeams(0,3);
        System.out.println("\n\n");
        System.out.printf("%s", ladder.toString());



        ///print the ladder
/*        List<Team> tempLadder = ladder.getLadder();
        for(Team team:tempLadder){
            System.out.println(team.getTeamID());
        }*/

        System.out.println(ladder.getLadderVolume());
        System.out.println(ladder.getLadderTeamCount());

    }

}
