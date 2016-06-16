package ca.sfu.cmpt373.alpha.vrcladder.game.score;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the individual team scores for a game.
 */
public class ScoreCard {
    private static final int MAXIMUM_NUMBER_OF_MATCHES = 5;

    private List<GameScore> scores;
    private Team team;

    public ScoreCard(Team team) {
        this.team = team;
        scores = new ArrayList<>();
        scores.add(new GameScore());
        scores.add(new GameScore());
        scores.add(new GameScore());
    }

    public boolean setScoreCard(int round, boolean score) throws SizeLimitExceededException {
        if(round >= MAXIMUM_NUMBER_OF_MATCHES){
            throw new SizeLimitExceededException("You have reached the maximum number of games!");
        }

        if(this.scores.get(round).isScoreEntered()){
            // TODO: Change this to exception.
            System.err.println("Score already recorded!");
        }

        this.scores.get(round).setGameScore(score);
        return true;
    }

    public Team getTeam(){
        return this.team;
    }

    public List<GameScore> getScores(){
        return this.scores;
    }

}
