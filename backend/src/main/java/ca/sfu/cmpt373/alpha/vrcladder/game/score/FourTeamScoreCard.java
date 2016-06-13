package ca.sfu.cmpt373.alpha.vrcladder.game.score;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FourTeamScoreCard extends ScoreCard {
    private static final int LAST_ROUND = 4;
    private static final int NUMBER_OF_TEAMS = 4;

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
                        team.equals(matchGroup.getTeams().get(3));
                throwIfFalse(isTeamOneOrFour);
                break;
            case 2:
                boolean isTeamTwoOrThree = team.equals(matchGroup.getTeam2()) ||
                        team.equals(matchGroup.getTeam3());
                throwIfFalse(isTeamTwoOrThree);
                break;
            case 3:
                boolean isWinnerAOrWinnerB = team.equals(roundWinners.get(0)) ||
                        team.equals(roundWinners.get(1));
                throwIfFalse(isWinnerAOrWinnerB);
                break;
            case 4:
                Team loserA = roundWinners.get(0).equals(matchGroup.getTeam1()) ?
                        matchGroup.getTeams().get(3) :
                        matchGroup.getTeam1();
                Team loserB = roundWinners.get(1).equals(matchGroup.getTeam2()) ?
                        matchGroup.getTeam3() :
                        matchGroup.getTeam2();
                boolean isLoserAOrB = team.equals(loserA) || team.equals(loserB);
                throwIfFalse(isLoserAOrB);
                break;
            default:
                assert(false);
        }
    }

    private void throwIfFalse(boolean condition) {
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
        Team winnerA = roundWinners.get(0);
        Team winnerB = roundWinners.get(1);
        Team winnerC = roundWinners.get(2);
        Team winnerD = roundWinners.get(3);
        Team loserA = winnerA.equals(matchGroup.getTeam1()) ? matchGroup.getTeam2() : matchGroup.getTeams().get(3);
        Team loserB = winnerB.equals(matchGroup.getTeam2()) ? matchGroup.getTeam3() : matchGroup.getTeam2();
        Team loserC = winnerA.equals(winnerC) ? winnerB : winnerA;
        Team loserD = loserA.equals(winnerD) ? loserB : loserA;

        List<Team> rankedTeams = new ArrayList<>();
        rankedTeams.add(winnerC);
        rankedTeams.add(loserC);
        rankedTeams.add(winnerD);
        rankedTeams.add(loserD);
        return rankedTeams;
    }
}
