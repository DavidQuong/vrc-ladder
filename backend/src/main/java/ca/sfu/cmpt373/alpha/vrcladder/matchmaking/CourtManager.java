package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import org.hibernate.Criteria;
import org.hibernate.Session;

import java.util.List;

public class CourtManager extends DatabaseManager<Court> {
    public CourtManager(SessionManager sessionManager) {
        super(Court.class, sessionManager);
    }

    @Override
    public Court create(Court court) {
        return super.create(court);
    }

    @Override
    public Court update(Court court) {
        return super.update(court);
    }

    @Override
    public List<Court> getAll() {
        //due to EAGER FetchType, duplicates are returned when getting all Courts
        //see MatchGroupManager's getAll() for an explanation of why this behaviour happens
        Session session = sessionManager.getSession();
        List<Court> entityList = session
                .createCriteria(Court.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();
        session.close();

        return entityList;
    }

    @Override
    public List<Court> deleteAll() {
        return super.deleteAll();
    }
}
