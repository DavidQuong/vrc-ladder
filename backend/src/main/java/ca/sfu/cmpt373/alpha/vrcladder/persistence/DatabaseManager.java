package ca.sfu.cmpt373.alpha.vrcladder.persistence;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

/**
 * The DatabaseManager implements both read and delete (by ID) operations,
 * but requires users of the class to implement both create and update operations
 * on their own.
 *
 * Note that the visibility of some methods are set to protected, this is to ensure
 * that only the extending classes may make use of such and catch errors that may
 * arise when those methods are used.
 */
public abstract class DatabaseManager<T> {

    private final Class<T> STORED_CLASS_TYPE;
    protected SessionManager sessionManager;

    public DatabaseManager(Class<T> storedClassType, SessionManager sessionManager) {
        this.STORED_CLASS_TYPE = storedClassType;
        this.sessionManager = sessionManager;
    }

    protected T create(T obj) throws ConstraintViolationException {
        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();

        session.save(obj);
        transaction.commit();
        session.close();

        return obj;
    }

    public T getById(String id) {
        Session session = sessionManager.getSession();
        T obj = session.get(STORED_CLASS_TYPE, id);
        session.close();

        return obj;
    }

    protected T update(T obj) {
        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();

        session.update(obj);
        transaction.commit();
        session.close();

        return obj;
    }

    protected T update(T obj, Session session) {
        Transaction transaction = session.beginTransaction();
        session.update(obj);
        transaction.commit();

        return obj;
    }

    public T delete(T obj) {
        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();

        session.delete(obj);
        transaction.commit();
        session.close();

        return obj;
    }

    public T deleteById(String id) {
        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();

        T obj = session.get(STORED_CLASS_TYPE, id);
        session.delete(obj);
        transaction.commit();
        session.close();

        return obj;
    }

    public List<T> getAll() {
        return sessionManager.getSession().createCriteria(STORED_CLASS_TYPE).list();
    }

}
