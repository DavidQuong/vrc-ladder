package ca.sfu.cmpt373.alpha.vrcladder.game;

import ca.sfu.cmpt373.alpha.vrcladder.game.score.ScoreSheet;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is for creating a game and track its scores.
 * it uses all the classes in score package
 */
public class Game {
    private List<MatchGroup> groups;
    private List<ScoreSheet> scoreSheets = new ArrayList<>();


    public Game(List<MatchGroup> inputGroups){
        this.groups = inputGroups;
        initScoreSheets();
    }

    public ScoreSheet getScoreSheet(User player) {
        ScoreSheet result = findPlayerSheet(player);
        return result;
    }
    public ScoreSheet getScoreSheet(Team player) {
        ScoreSheet result = findPlayerSheet(player.getFirstPlayer());
        return result;
    }


    private ScoreSheet findPlayerSheet(User player){
        ScoreSheet result = new ScoreSheet();
        for(ScoreSheet sheet : scoreSheets) {
            if(sheet.checkTeamPlayer(player)) {
                result = sheet;
            }
        }
        return result;
    }

    private void initScoreSheets() {
        for(MatchGroup group : this.groups) {
            ScoreSheet sheet = new ScoreSheet(group.getTeams());
            this.scoreSheets.add(sheet);
        }
    }

}
