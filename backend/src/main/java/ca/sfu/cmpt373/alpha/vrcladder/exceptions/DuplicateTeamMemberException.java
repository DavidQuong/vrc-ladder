package ca.sfu.cmpt373.alpha.vrcladder.exceptions;

import javax.persistence.PersistenceException;

/**
 * This exception is thrown when a player attempts to create a team with
 * themselves by using their own user ID.
 */
public class DuplicateTeamMemberException extends PersistenceException {

}
