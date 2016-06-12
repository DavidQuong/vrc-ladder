package ca.sfu.cmpt373.alpha.vrcladder.exceptions;

import javax.persistence.PersistenceException;

/**
 * This exception is thrown when an entity cannot be found within the
 * database.
 */
public class EntityNotFoundException extends PersistenceException {
}
