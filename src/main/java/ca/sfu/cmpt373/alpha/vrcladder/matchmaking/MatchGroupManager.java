package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PersistenceException;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MatchGroupManager {
    private static final String ERROR_NO_MATCH_GROUP = "There was no match group found for the given id";

    public SessionManager sessionManager;

    public MatchGroupManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public MatchGroup createMatchGroup(List<Team> teams) {
        MatchGroup matchGroup = new MatchGroup(teams);
        saveMatchGroup(matchGroup);
        return matchGroup;
    }

    public MatchGroup createMatchGroup(Team team1, Team team2, Team team3) {
        MatchGroup matchGroup = new MatchGroup(team1, team2, team3);
        saveMatchGroup(matchGroup);
        return matchGroup;
    }

    public MatchGroup createMatchGroup(Team team1, Team team2, Team team3, Team team4) {
        MatchGroup matchGroup = new MatchGroup(team1, team2, team3, team4);
        saveMatchGroup(matchGroup);
        return matchGroup;
    }

    /**
     * @throws PersistenceException if there is no object stored in the database for the given id
     */
    public MatchGroup getMatchGroup(IdType matchGroupId) {
        return getMatchGroup(matchGroupId.getId());
    }

    /**
     * @throws PersistenceException if there is no object stored in the database for the given id
     */
    public MatchGroup getMatchGroup(String matchGroupId) {
        Session session = sessionManager.getSession();
        MatchGroup matchGroup = session.get(MatchGroup.class, matchGroupId);
        if (matchGroup == null) {
            throw new PersistenceException(ERROR_NO_MATCH_GROUP);
        }
        return matchGroup;
    }

    public void deleteMatchGroup(String matchGroupId) {
        Session session = sessionManager.getSession();

        Transaction transaction = session.beginTransaction();
        MatchGroup matchGroup = getMatchGroup(matchGroupId);

        session.delete(matchGroup);
        transaction.commit();
        session.close();
    }

    private void saveMatchGroup(MatchGroup matchGroup) {
        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(matchGroup);
        transaction.commit();
        session.close();
    }

}
