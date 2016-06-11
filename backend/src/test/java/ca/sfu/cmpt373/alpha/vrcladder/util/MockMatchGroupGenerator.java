package ca.sfu.cmpt373.alpha.vrcladder.util;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;

public class MockMatchGroupGenerator {

    public static MatchGroup generateThreeTeamMatchGroup() {
        Team team1 = MockTeamGenerator.generateTeam();
        Team team2 = MockTeamGenerator.generateTeam();
        Team team3 = MockTeamGenerator.generateTeam();

        return new MatchGroup(team1, team2, team3);
    }

    public static MatchGroup generateFourTeamMatchGroup() {
        Team team1 = MockTeamGenerator.generateTeam();
        Team team2 = MockTeamGenerator.generateTeam();
        Team team3 = MockTeamGenerator.generateTeam();
        Team team4 = MockTeamGenerator.generateTeam();

        return new MatchGroup(team1, team2, team3, team4);
    }

}
