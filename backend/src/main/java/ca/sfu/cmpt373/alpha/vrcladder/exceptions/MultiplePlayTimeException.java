package ca.sfu.cmpt373.alpha.vrcladder.exceptions;

import javax.persistence.PersistenceException;

/**
 * This exception is thrown when a player attempts to set a preferred time
 * of play for their team when one of the two players that makes up that
 * team is already playing with another team.
 */
public class MultiplePlayTimeException extends PersistenceException {

    public MultiplePlayTimeException(String userId, String teamId) {
        super("Player with User ID: " + userId + " is opted-in to play with another team (" + teamId + ").");
    }

}
