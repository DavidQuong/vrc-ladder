package ca.sfu.cmpt373.alpha.vrcladder.persistence;

import org.hibernate.Session;

/**
 * Manages connections to the database.
 * TODO - Provide an interface to using the underlying Hibernate Sessions.
 */
public class DatabaseManager {

    private SessionManager sessionManager;
    private Session currentSession;

    public DatabaseManager() {
        this.sessionManager = new SessionManager();
        this.currentSession = sessionManager.getSession();
    }

    public void shutDown() {
        sessionManager.shutDown();
    }

    public Session getCurrentSession() {
        return currentSession;
    }

}
