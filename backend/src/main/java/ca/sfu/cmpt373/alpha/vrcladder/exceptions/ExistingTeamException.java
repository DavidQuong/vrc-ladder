package ca.sfu.cmpt373.alpha.vrcladder.exceptions;

import javax.persistence.PersistenceException;

/**
 * This exception is thrown one attempts to create a team with the exact
 * same two players as another team.
 */
public class ExistingTeamException extends PersistenceException {
}