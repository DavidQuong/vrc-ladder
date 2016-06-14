package ca.sfu.cmpt373.alpha.vrcladder.game.score;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ThreeTeamScoreCard extends ScoreCard {
    private static final int LAST_ROUND = 3;
    private static final int NUMBER_OF_TEAMS = 3;

    private static final int INDEX_FIRST_ROUND_WINNER = 0;
    private static final int INDEX_SECOND_ROUND_WINNER = 1;
    private static final int INDEX_THIRD_ROUND_WINNER = 2;

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
        if (currentRound <= LAST_ROUND) {
            throw new IllegalStateException(ERROR_ROUNDS_NOT_OVER);
        }
//        matches are played as follows:
//        a. 1 VS 2
//        b. Winner (a) VS 3
//        c. Loser (a) VS 3
//        ranks are derived from wins/losses of this ordering
        //TODO: simplify this by just counting the number of times each team won, and ordering based on that
        List<Team> rankedTeams = new ArrayList<>();
        Team loserA = roundWinners.get(INDEX_FIRST_ROUND_WINNER).equals(matchGroup.getTeam1()) ?
                matchGroup.getTeam2() :
                matchGroup.getTeam1();
        Team winnerA = roundWinners.get(INDEX_FIRST_ROUND_WINNER);
        Team winnerB = roundWinners.get(INDEX_SECOND_ROUND_WINNER);
        Team winnerC = roundWinners.get(INDEX_THIRD_ROUND_WINNER);
        if (winnerA.equals(winnerB)) {
            rankedTeams.add(winnerA);
            if (matchGroup.getTeam3().equals(winnerC)) {
                rankedTeams.add(matchGroup.getTeam3());
                rankedTeams.add(loserA);
            } else {
                rankedTeams.add(loserA);
                rankedTeams.add(matchGroup.getTeam3());
            }
        } else if (matchGroup.getTeam3().equals(winnerC)){
            rankedTeams.add(matchGroup.getTeam3());
            rankedTeams.add(winnerA);
            rankedTeams.add(loserA);
        } else {
            //everyone won a game, and lost a game so their order doesn't change
            rankedTeams.add(matchGroup.getTeam1());
            rankedTeams.add(matchGroup.getTeam2());
            rankedTeams.add(matchGroup.getTeam3());
        }
        return rankedTeams;
    }

    @Override
    public int getLastRound() {
        return LAST_ROUND;
    }
}
