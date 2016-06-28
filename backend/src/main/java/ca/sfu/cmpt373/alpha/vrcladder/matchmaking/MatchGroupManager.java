package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

public class MatchGroupManager extends DatabaseManager<MatchGroup> {

    private static final Class MATCH_CLASS_TYPE = MatchGroup.class;

    private static final String ERROR_NO_MATCH_GROUP = "There was no match group found for the given id";

    public MatchGroupManager(SessionManager sessionManager) {
        super(MATCH_CLASS_TYPE, sessionManager);
    }

    @Override
    public MatchGroup create(MatchGroup matchGroup) {
        return super.create(matchGroup);
    }

    public MatchGroup create(List<Team> teams) {
        MatchGroup matchGroup = new MatchGroup(teams);

        return create(matchGroup);
    }

    public MatchGroup create(Team team1, Team team2, Team team3) {
        MatchGroup matchGroup = new MatchGroup(team1, team2, team3);

        return create(matchGroup);
    }

    public MatchGroup create(Team team1, Team team2, Team team3, Team team4) {
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

    public MatchGroup deleteMatchGroup(IdType matchGroupId) {
        return deleteById(matchGroupId);
    }

	public MatchGroup addTeamToMatchGroup(IdType matchGroupId, Team newTeam) {
		MatchGroup matchGroup = getById(matchGroupId);
		if (matchGroup == null) {
			throw new EntityNotFoundException(ERROR_NO_MATCH_GROUP);
		}
		matchGroup.addTeam(newTeam);

		return update(matchGroup);
	}

	public MatchGroup removeTeamFromMatchGroup(IdType matchGroupId, Team leavingTeam) {
		MatchGroup matchGroup = getById(matchGroupId);
		if (matchGroup == null) {
			throw new EntityNotFoundException(ERROR_NO_MATCH_GROUP);
		}
		matchGroup.removeTeam(leavingTeam);

		return update(matchGroup);
	}

	public List<MatchGroup> tradeTeamsInMatchGroups(IdType matchGroupId1, Team first, IdType matchGroupId2, Team second) {
		MatchGroup matchGroup1 = getById(matchGroupId1);
		if (matchGroup1 == null) {
			throw new EntityNotFoundException(ERROR_NO_MATCH_GROUP);
		}
		MatchGroup matchGroup2 = getById(matchGroupId2);
		if (matchGroup2 == null) {
			throw new EntityNotFoundException(ERROR_NO_MATCH_GROUP);
		}
		matchGroup1.tradeTeams(first, matchGroup2, second);

		Session session = sessionManager.getSession();
		Transaction transaction = session.beginTransaction();

		session.update(matchGroup1);
		session.update(matchGroup2);

		transaction.commit();
		session.close();

		List<MatchGroup> results = new ArrayList<>();
		results.add(matchGroup1);
		results.add(matchGroup2);

		return results;
	}

    @Override
    public List<MatchGroup> getAll() {
        //Criteria.DISTINCT_ROOT_ENTITY is needed so duplicates of results aren't returned
        //FETCHTYPE.EAGER results in duplicate MatchGroups being returned, because of the way the data
        //is represented in sql tables. For instance, a table representing one MatchGroup may have several
        //rows for each MatchGroup since it has to store different tuples representing the relationship between
        //a MatchGroup and its teams. Ex: (matchGroup1Id, team1Id), (matchGroup1Id, team2Id), (matchGroup1Id, team3Id)
        //are all distinct tuples/rows in the sql table, so by default, listing all the items will return three instances
        //of MatchGroup1. We have to tell Hibernate to just give us one instance of matchGroup1, using DISTINCT_ROOT_ENTITY
        Session session = sessionManager.getSession();
        List<MatchGroup> entityList = session
                .createCriteria(MatchGroup.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();
        session.close();

        //return MatchGroups in sorted order
        Collections.sort(entityList);

        return entityList;
    }

    @Override
    public List<MatchGroup> deleteAll() {
        return super.deleteAll();
    }
}
