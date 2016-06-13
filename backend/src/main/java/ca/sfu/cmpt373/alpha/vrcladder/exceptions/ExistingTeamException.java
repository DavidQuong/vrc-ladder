package ca.sfu.cmpt373.alpha.vrcladder.exceptions;

import javax.persistence.PersistenceException;

/**
 * This exception is thrown when an attempt to create a team with the
 * exact same two players that make up another team is made.
 */
public class ExistingTeamException extends PersistenceException {
}
