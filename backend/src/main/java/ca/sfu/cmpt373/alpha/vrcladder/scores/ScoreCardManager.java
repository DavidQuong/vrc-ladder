package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

//TODO: delete this as ScoreCards should only be created from within a MatchGroup (from MatchGroupManager)
public class ScoreCardManager extends DatabaseManager<ScoreCard> {

    public ScoreCardManager(SessionManager sessionManager) {
        super(ScoreCard.class, sessionManager);
    }

    @Override
    public ScoreCard delete(ScoreCard scoreCard) {
        return deleteById(scoreCard.getId());
    }

    @Override
    public ScoreCard deleteById(String id) {
        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();

        ScoreCard scoreCard = session.get(ScoreCard.class, id);
        session.delete(scoreCard);
        session.delete(scoreCard.getMatchGroup());
        transaction.commit();
        session.close();

        return scoreCard;
    }

    public ScoreCard create(MatchGroup matchGroup) {
        return create(new ScoreCard(matchGroup));
    }

    @Override
    protected ScoreCard create(ScoreCard scoreCard) throws ConstraintViolationException {
        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();

        session.save(scoreCard);
        session.save(scoreCard.getMatchGroup());
        transaction.commit();
        session.close();

        return scoreCard;
    }

    @Override
    public ScoreCard update(ScoreCard obj) {
        return super.update(obj);
    }
}
