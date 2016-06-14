package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;

public class ScoreCardManager extends DatabaseManager<ScoreCard> {

    public ScoreCardManager(SessionManager sessionManager) {
        super(ScoreCard.class, sessionManager);
    }

    public ScoreCard create(MatchGroup matchGroup) {
        ScoreCard scoreCard;
        int threeTeamCount = 3;
        if (matchGroup.getTeamCount() == threeTeamCount) {
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
