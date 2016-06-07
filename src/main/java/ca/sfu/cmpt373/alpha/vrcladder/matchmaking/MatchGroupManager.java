package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PersistenceException;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

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
        MatchGroup matchGroup = getById(matchGroupId);
        if (matchGroup == null) {
            throw new PersistenceException(ERROR_NO_MATCH_GROUP);
        }

        return matchGroup;
    }

    public MatchGroup deleteMatchGroup(String matchGroupId) {
        return deleteById(matchGroupId);
    }

}