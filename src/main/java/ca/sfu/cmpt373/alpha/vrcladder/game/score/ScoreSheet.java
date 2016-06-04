package ca.sfu.cmpt373.alpha.vrcladder.game.score;

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

    public ScoreSheet() {

    }

    public ScoreSheet(List<Team> teams) {
        this.teams = teams;
        initScoreCards();
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

    public boolean checkTeamPlayer(User player){
        for(Team team : teams){
            if(team.checkTeamPlayer(player)){
                return true;
            }
        }
        return false;
    }

    private void initScoreCards() {
        for(Team team : this.teams){
            ScoreCard card = new ScoreCard(team);
            this.scoreCards.add(card);
        }
    }

    private void recordTeamScore(Team team, int round, boolean score) throws SizeLimitExceededException {
        for(ScoreCard card : scoreCards){
            if(team.equals(card.getTeam())){
                card.setScoreCard(round, score);
            }
        }
    }


}
