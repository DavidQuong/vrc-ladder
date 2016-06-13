package ca.sfu.cmpt373.alpha.vrcladder.game.score;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;

import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FourTeamScoreCard implements IScoreCard {
    private static final int LAST_ROUND = 4;
    private static final int NUMBER_OF_TEAMS = 4;

    private static final String ERROR_TEAM_NOT_IN_ROUND = "team is not in round";
    private static final String ERROR_ROUNDS_OVER = "All round winners have been recorded";
    private static final String ERROR_MATCHGROUP_SIZE = "matchgroup must be of size 4";

    private MatchGroup matchGroup;
    private Map<Integer, Team> roundWinners = new HashMap<>();

    private Integer currentRound = 1;

    public FourTeamScoreCard(MatchGroup matchGroup) {
        if (matchGroup.getTeams().size() != NUMBER_OF_TEAMS) {
            throw new IllegalArgumentException(ERROR_MATCHGROUP_SIZE);
        }
        this.matchGroup = matchGroup;
    }

    @Override
    public void recordRoundWinner(Team team) {
        if (currentRound > LAST_ROUND) {
            throw new IllegalStateException(ERROR_ROUNDS_OVER);
        }
        verifyTeamIsInRound(team);
        roundWinners.put(currentRound, team);
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
                boolean isWinnerAOrWinnerB = team.equals(roundWinners.get(1)) ||
                        team.equals(roundWinners.get(2));
                throwIfFalse(isWinnerAOrWinnerB);
                break;
            case 4:
                Team loserA = roundWinners.get(1).equals(matchGroup.getTeam1()) ?
                        matchGroup.getTeams().get(3) :
                        matchGroup.getTeam1();
                Team loserB = roundWinners.get(2).equals(matchGroup.getTeam2()) ?
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
//        matches are played in the following order:
//        a) 1V4
//        b) 2V3
//        c) Winner a V Winner b
//        d) Loser a V Loser b
//        ranks are derived from wins/losses of this ordering
        Team winnerA = roundWinners.get(1);
        Team winnerB = roundWinners.get(2);
        Team winnerC = roundWinners.get(3);
        Team winnerD = roundWinners.get(4);
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
