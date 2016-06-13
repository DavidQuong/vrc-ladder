package ca.sfu.cmpt373.alpha.vrcladder.game.score;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;

import java.util.List;

public interface IScoreCard {
    void recordRoundWinner(Team team);
    List<Team> getRankedResults();
}
