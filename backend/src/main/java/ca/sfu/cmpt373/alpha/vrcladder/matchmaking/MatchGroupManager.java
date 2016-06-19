package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
<<<<<<< HEAD
import org.hibernate.Session;
import org.hibernate.Transaction;
=======
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
import java.util.ArrayList;
>>>>>>> 3841e33... Integrate MatchGroup manual override into database

import java.util.List;

public class MatchGroupManager extends DatabaseManager<MatchGroup> {

    private static final Class MATCH_CLASS_TYPE = MatchGroup.class;
    private static final String ERROR_NO_MATCH_GROUP = "There was no match group found for the given id";

    public MatchGroupManager(SessionManager sessionManager) {
        super(MATCH_CLASS_TYPE, sessionManager);
    }

    public MatchGroup createMatchGroup(List<Team> teams) {
        MatchGroup matchGroup = new MatchGroup(teams);

        return create(matchGroup);
    }

    public MatchGroup createMatchGroup(Team team1, Team team2, Team team3) {
        MatchGroup matchGroup = new MatchGroup(team1, team2, team3);

        return create(matchGroup);
    }

    public MatchGroup createMatchGroup(Team team1, Team team2, Team team3, Team team4) {
        MatchGroup matchGroup = new MatchGroup(team1, team2, team3, team4);

        return create(matchGroup);
    }

    public MatchGroup updateScoreCard(MatchGroup matchGroup) {
        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();

        session.update(matchGroup.getScoreCard());
        transaction.commit();
        session.close();

        return matchGroup;
    }
	
    public MatchGroup deleteMatchGroup(String matchGroupId) {
        return deleteById(matchGroupId);
    }

	public MatchGroup addTeamToMatchGroup(String matchGroupId, Team newTeam) {
		MatchGroup matchGroup = getById(matchGroupId);
		if (matchGroup == null) {
			throw new PersistenceException(ERROR_NO_MATCH_GROUP);
		}
		matchGroup.addTeam(newTeam);

		return update(matchGroup);
	}

	public MatchGroup removeTeamFromMatchGroup(String matchGroupId, Team leavingTeam) {
		MatchGroup matchGroup = getById(matchGroupId);
		if (matchGroup == null) {
			throw new PersistenceException(ERROR_NO_MATCH_GROUP);
		}
		matchGroup.removeTeam(leavingTeam);

		return update(matchGroup);
	}

	public MatchGroup removeTeamFromMatchGroup(String matchGroupId, int leavingTeamIndex) {
		MatchGroup matchGroup = getById(matchGroupId);
		if (matchGroup == null) {
			throw new PersistenceException(ERROR_NO_MATCH_GROUP);
		}
		matchGroup.removeTeam(leavingTeamIndex);

		return update(matchGroup);
	}

	public List<MatchGroup> tradeTeamsInMatchGroups(String matchGroupId1, Team first, String matchGroupId2, Team second) {
		MatchGroup matchGroup1 = getById(matchGroupId1);
		if (matchGroup1 == null) {
			throw new PersistenceException(ERROR_NO_MATCH_GROUP);
		}
		MatchGroup matchGroup2 = getById(matchGroupId1);
		if (matchGroup2 == null) {
			throw new PersistenceException(ERROR_NO_MATCH_GROUP);
		}
		matchGroup1.tradeTeams(first, matchGroup2, second);

		List<MatchGroup> results = new ArrayList<>();
		results.add(update(matchGroup1));
		results.add(update(matchGroup2));

		return results;
	}

	public List<MatchGroup> tradeTeamsInMatchGroups(String matchGroupId1, int firstIndex, String matchGroupId2, int secondIndex) {
		MatchGroup matchGroup1 = getById(matchGroupId1);
		if (matchGroup1 == null) {
			throw new PersistenceException(ERROR_NO_MATCH_GROUP);
		}
		MatchGroup matchGroup2 = getById(matchGroupId1);
		if (matchGroup2 == null) {
			throw new PersistenceException(ERROR_NO_MATCH_GROUP);
		}
		matchGroup1.tradeTeams(firstIndex, matchGroup2, secondIndex);

		List<MatchGroup> results = new ArrayList<>();
		results.add(update(matchGroup1));
		results.add(update(matchGroup2));

		return results;
	}
}
