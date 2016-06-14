package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class ThreeTeamScoreCard extends ScoreCard {
    private static final int LAST_ROUND = 3;
    private static final int NUMBER_OF_TEAMS = 3;

    private static final int INDEX_FIRST_ROUND_WINNER = 0;

    private ThreeTeamScoreCard() {
        //for hibernate
    }

    public ThreeTeamScoreCard(MatchGroup matchGroup) {
        super(matchGroup);
        if (matchGroup.getTeams().size() != NUMBER_OF_TEAMS) {
            throw new IllegalArgumentException(ERROR_MATCHGROUP_SIZE);
        }
    }

    @Override
    public void recordRoundWinner(Team team) {
        if (currentRound > LAST_ROUND) {
            throw new IllegalStateException(ERROR_ROUNDS_OVER);
        }
        verifyTeamIsInRound(team);
        roundWinners.add(team);
        currentRound++;
    }

    private void verifyTeamIsInRound(Team team) {
        switch (currentRound) {
            case 1:
                boolean isTeamOneOrTwo = matchGroup.getTeam1().equals(team) || matchGroup.getTeam2().equals(team);
                throwTeamNotInRoundIfFalse(isTeamOneOrTwo);
                break;
            case 2:
                Team winnerA = roundWinners.get(INDEX_FIRST_ROUND_WINNER);
                boolean isWinnerAOrTeamThree = team == winnerA || team == matchGroup.getTeam3();
                throwTeamNotInRoundIfFalse(isWinnerAOrTeamThree);
                break;
            case 3:
                Team loserA = roundWinners.get(INDEX_FIRST_ROUND_WINNER).equals(matchGroup.getTeam1()) ?
                        matchGroup.getTeam2() :
                        matchGroup.getTeam1();
                boolean isLoserAOrTeamThree = team == loserA || team == matchGroup.getTeam3();
                throwTeamNotInRoundIfFalse(isLoserAOrTeamThree);
                break;
            default:
                throw new IllegalStateException(ERROR_INVALID_DEFAULT_CASE);
        }
    }

    private void throwTeamNotInRoundIfFalse(boolean condition) {
        if (!condition) {
            throw new IllegalStateException(ERROR_TEAM_NOT_IN_ROUND);
        }
    }

    @Override
    public List<Team> getRankedResults() {
        //Count the number of wins for each team.
        //Winner will have at most two wins since each team only plays two games
        //in the event of a tie, team orderings should remain unchanged

        if (currentRound <= LAST_ROUND) {
            throw new IllegalStateException(ERROR_ROUNDS_NOT_OVER);
        }

        Map<Team, Integer> teamWinCounts = new HashMap<>();

        //initialize win counts for each team to zero
        for (Team team : matchGroup.getTeams()) {
            teamWinCounts.put(team, 0);
        }

        //count wins for each team
        for (Team team : roundWinners) {
            teamWinCounts.put(team, teamWinCounts.get(team) + 1);
        }

        //add teams with the most wins first
        int lowestPossibleWinCount = 0;
        List<Team> rankedTeams = new ArrayList<>();
        for (int targetWinCount = 2; targetWinCount >= lowestPossibleWinCount; targetWinCount--) {
            for (Team team : matchGroup.getTeams()) {
                if (teamWinCounts.get(team).equals(targetWinCount)) {
                    rankedTeams.add(team);
                }
            }
        }
        return rankedTeams;
    }

    @Override
    public int getLastRound() {
        return LAST_ROUND;
    }
}
