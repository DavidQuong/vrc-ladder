package ca.sfu.cmpt373.alpha.vrcladder.game.score;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;

public class ScoreCardManager extends DatabaseManager<ScoreCard> {

    public ScoreCardManager(SessionManager sessionManager) {
        super(ScoreCard.class, sessionManager);
    }

    public ScoreCard create(MatchGroup matchGroup) {
        ScoreCard scoreCard;
        if (matchGroup.getTeams().size() == 3) {
            scoreCard = new ThreeTeamScoreCard(matchGroup);
        } else {
            scoreCard = new FourTeamScoreCard(matchGroup);
        }
        return create(scoreCard);
    }

    @Override
    public ScoreCard update(ScoreCard obj) {
        return super.update(obj);
    }
}
