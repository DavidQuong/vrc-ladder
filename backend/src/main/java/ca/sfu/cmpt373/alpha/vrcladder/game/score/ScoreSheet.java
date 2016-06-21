package ca.sfu.cmpt373.alpha.vrcladder.game.score;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;

import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.List;

/**
 * ScoreSheet class contains all the score cards for the game.
 * each score card is the results of one team.
 */
public class ScoreSheet {

    private List<Team> teams;
    private List<ScoreCard> scoreCards = new ArrayList<>();
    private static final int MAXIMUM_WINS_FOR_THREE_TEAMS    = 2;
    private static final int MAXIMUM_POSITION_FOR_FOUR_TEAMS = 4;
    private static final int FOUR_TEAM_POSITION_CONFLICT     = 1;
    private static final int TEAM_SECOND_PLACE_POSITION      = 2;
    private static final int TEAM_THIRD_PLACE_POSITION       = 3;
    private static final int INITIATING_VALUE                = 0;

    /*public ScoreSheet() {

    }*/

    public ScoreSheet(List<Team> teams) {
        this.teams = teams;
        initScoreCards();
        if(this.teams.size() < MatchGroup.MIN_NUM_TEAMS || this.teams.size() > MatchGroup.MAX_NUM_TEAMS) {
            throw new RuntimeException("Unexpected group size: " + this.teams.size());
        }

    }

    public ScoreCard getScoreCard(int position) {
        return this.scoreCards.get(position);
    }

    public List<Team> getTeams(){
        return this.teams;
    }

    public int getNumberOfTeams(){
        return this.teams.size();
    }

    public boolean checkTeamPlayer(User player) {
        for (Team team : teams) {
            if (team.getFirstPlayer().equals(player) || team.getSecondPlayer().equals(player)) {
                return true;
            }
        }
        return false;
    }
//arranges team in least based on scores
    public List<Team> getScores() {
        List<Team> results = new ArrayList<>(this.teams);

        if(this.getNumberOfTeams() == MatchGroup.MIN_NUM_TEAMS){
            int[] wins = new int[getNumberOfTeams()];
            for(int counter = INITIATING_VALUE; counter < getNumberOfTeams(); counter++){
                wins[counter] = this.doThreeTeamsCalculations(this.getScoreCard(counter));
            }

            if(!isTie(wins)){
                results = reOrderThreeTeamsResults(wins);
            }

        } else if(this.getNumberOfTeams() == MatchGroup.MAX_NUM_TEAMS){
            int[] positions = new int[getNumberOfTeams()];
            for(int counter = INITIATING_VALUE; counter < getNumberOfTeams(); counter++){
                positions[counter] = this.doFourTeamsCalculations(this.getScoreCard(counter));
            }

            results = reOrderFourTeamsResults(positions);
        } else {
            throw new RuntimeException("Unexpected group size: " + this.teams.size()+", "+results.size());
        }

        return results;
    }

    public void recordTeamScore(Team team, int round, boolean score) throws SizeLimitExceededException {
        for(ScoreCard card : scoreCards){
            if(team.equals(card.getTeam())){
                card.setScoreCard(round, score);
            }
        }
    }

    private int doThreeTeamsCalculations(ScoreCard score){
        List<GameScore> teamScores = new ArrayList<>(score.getScores());
        int results = INITIATING_VALUE;

        for(GameScore scores : teamScores){
            if(scores.isScoreEntered()){
                if(scores.getScore()){
                    results++;
                }
            }
        }
        return results;
    }

    private boolean isTie(int[] wins){
        boolean results = false;
        for(int counter = INITIATING_VALUE; counter < wins.length; counter++){
            int nextPosition = counter;
            nextPosition++;
            if(nextPosition < wins.length){
                if(wins[counter] == wins[nextPosition]){
                    results = true;
                }
            }
        }
        return results;
    }

    private int doFourTeamsCalculations(ScoreCard score){
        List<GameScore> teamScores = new ArrayList<>(score.getScores());
        int results = INITIATING_VALUE;
        boolean secondPlace = false;

        for(GameScore scores : teamScores){
            if(scores.isScoreEntered()){
                if(scores.getScore()){
                    if(results == INITIATING_VALUE){
                        secondPlace= true;
                    }
                    results++;
                }
            }
        }

        if(results == FOUR_TEAM_POSITION_CONFLICT){
            if(secondPlace == true){
                results = TEAM_SECOND_PLACE_POSITION;
            }else{
                results = TEAM_THIRD_PLACE_POSITION;
            }
        }else{
            if(results == MAXIMUM_WINS_FOR_THREE_TEAMS){
                results = INITIATING_VALUE;
            }else{
                results = MAXIMUM_POSITION_FOR_FOUR_TEAMS;
            }
        }

        return results;
    }

    private List<Team> reOrderThreeTeamsResults(int[] wins){
        List<Team> results = new ArrayList<>(this.teams);

        for(int counter = INITIATING_VALUE; counter < this.getNumberOfTeams(); counter++){
            int arrayListPosition = MAXIMUM_WINS_FOR_THREE_TEAMS - wins[counter];
            results.set(arrayListPosition, this.teams.get(counter));
        }
        return results;
    }

    private List<Team> reOrderFourTeamsResults(int[] positions){
        List<Team> results = new ArrayList<>(this.teams);

        for(int counter = INITIATING_VALUE; counter < this.getNumberOfTeams(); counter++){
            int arrayListPosition = positions[counter];
            results.set(arrayListPosition, this.teams.get(counter));
        }

        return results;
    }

    private void initScoreCards() {
        for(Team team : this.teams){
            ScoreCard card = new ScoreCard(team);
            this.scoreCards.add(card);
        }
    }
}
