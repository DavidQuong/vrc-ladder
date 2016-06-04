package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MatchGroupManager {
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
    public MatchGroup getMatchGroup(IdType matchGroupId) {
        return getMatchGroup(matchGroupId.getId());
    }

    public MatchGroup getMatchGroup(String matchGroupId) {
        Session session = sessionManager.getSession();
        return session.get(MatchGroup.class, matchGroupId);
    }

    public void deleteMatchGroup(String matchGroupId) {
        Session session = sessionManager.getSession();

        Transaction transaction = session.getTransaction();
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
