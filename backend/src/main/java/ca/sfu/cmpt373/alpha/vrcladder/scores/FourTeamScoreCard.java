package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FourTeamScoreCard extends ScoreCard {
    private static final int LAST_ROUND = 4;
    private static final int NUMBER_OF_TEAMS = 4;

    private static final int INDEX_FIRST_ROUND_WINNER = 0;
    private static final int INDEX_SECOND_ROUND_WINNER = 1;
    private static final int INDEX_THIRD_ROUND_WINNER = 2;
    private static final int INDEX_FOURTH_ROUND_WINNER = 3;
    private static final int INDEX_FOURTH_MATCHGROUP_PLAYER = 3;

    private FourTeamScoreCard() {
        //for hibernate
    }

    public FourTeamScoreCard(MatchGroup matchGroup) {
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
                boolean isTeamOneOrFour = team.equals(matchGroup.getTeam1()) ||
                        team.equals(matchGroup.getTeams().get(INDEX_FOURTH_MATCHGROUP_PLAYER));
                throwTeamNotInRoundIfFalse(isTeamOneOrFour);
                break;
            case 2:
                boolean isTeamTwoOrThree = team.equals(matchGroup.getTeam2()) ||
                        team.equals(matchGroup.getTeam3());
                throwTeamNotInRoundIfFalse(isTeamTwoOrThree);
                break;
            case 3:
                boolean isWinnerAOrWinnerB = team.equals(roundWinners.get(INDEX_FIRST_ROUND_WINNER)) ||
                        team.equals(roundWinners.get(INDEX_SECOND_ROUND_WINNER));
                throwTeamNotInRoundIfFalse(isWinnerAOrWinnerB);
                break;
            case 4:
                Team loserA = roundWinners.get(INDEX_FIRST_ROUND_WINNER).equals(matchGroup.getTeam1()) ?
                        matchGroup.getTeams().get(INDEX_FOURTH_MATCHGROUP_PLAYER) :
                        matchGroup.getTeam1();
                Team loserB = roundWinners.get(INDEX_SECOND_ROUND_WINNER).equals(matchGroup.getTeam2()) ?
                        matchGroup.getTeam3() :
                        matchGroup.getTeam2();
                boolean isLoserAOrB = team.equals(loserA) || team.equals(loserB);
                throwTeamNotInRoundIfFalse(isLoserAOrB);
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
//        matches are played in the following order:
//        a) 1V4
//        b) 2V3
//        c) Winner a V Winner b
//        d) Loser a V Loser b
//        ranks are derived from wins/losses of this ordering
        Team winnerA = roundWinners.get(INDEX_FIRST_ROUND_WINNER);
        Team winnerB = roundWinners.get(INDEX_SECOND_ROUND_WINNER);
        Team winnerC = roundWinners.get(INDEX_THIRD_ROUND_WINNER);
        Team winnerD = roundWinners.get(INDEX_FOURTH_ROUND_WINNER);
        Team loserA = winnerA.equals(matchGroup.getTeam1()) ?
                matchGroup.getTeams().get(INDEX_FOURTH_MATCHGROUP_PLAYER) :
                matchGroup.getTeam1();
        Team loserB = winnerB.equals(matchGroup.getTeam2()) ?
                matchGroup.getTeam3() :
                matchGroup.getTeam2();
        Team loserC = winnerA.equals(winnerC) ? winnerB : winnerA;
        Team loserD = loserA.equals(winnerD) ? loserB : loserA;

        List<Team> rankedTeams = new ArrayList<>();
        rankedTeams.add(winnerC);
        rankedTeams.add(loserC);
        rankedTeams.add(winnerD);
        rankedTeams.add(loserD);
        return rankedTeams;
    }

    @Override
    public int getLastRound() {
        return LAST_ROUND;
    }
}
