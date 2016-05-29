package ca.sfu.cmpt373.alpha.vrcladder;

import ca.sfu.cmpt373.alpha.vrcladder.users.ladder.Ladder;
import ca.sfu.cmpt373.alpha.vrcladder.users.ladder.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Driver {

    private static List<Team> teams;

    public static void main(String[] args) {
        // Template code

        System.out.printf("%s", "Printing...");
        teams = new ArrayList<>();

        Ladder ladder = new Ladder(teams);

        UUID uuid;
        for(int i = 0; i < 100; i++){
            uuid = UUID.randomUUID();
            Team team = new Team(uuid);
            ladder.insertTeam(team);
        }
        System.out.printf("%s %n", "Mark...");

        ///print the ladder
        List<Team> tempLadder = ladder.getLadder();
        for(Team team:tempLadder){
            System.out.println(team.getTeamID());
        }
    }

}
