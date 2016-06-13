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
                throwIfFalse(isTeamOneOrTwo);
                break;
            case 2:
                Team winnerA = roundWinners.get(0);
                boolean isWinnerAOrTeamThree = team == winnerA || team == matchGroup.getTeam3();
                throwIfFalse(isWinnerAOrTeamThree);
                break;
            case 3:
                Team loserA = roundWinners.get(0).equals(matchGroup.getTeam1()) ? matchGroup.getTeam2() : matchGroup.getTeam1();
                boolean isLoserAOrTeamThree = team == loserA || team == matchGroup.getTeam3();
                throwIfFalse(isLoserAOrTeamThree);
                break;
            default:
                assert(false);
        }
    }

    private void throwIfFalse (boolean condition) {
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
        List<Team> rankedTeams = new ArrayList<>();
        Team loserA = roundWinners.get(0).equals(matchGroup.getTeam1()) ? matchGroup.getTeam2() : matchGroup.getTeam1();
        Team winnerA = roundWinners.get(0);
        Team winnerB = roundWinners.get(1);
        Team winnerC = roundWinners.get(2);
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
