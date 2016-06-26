package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;

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
}
