package ca.sfu.cmpt373.alpha.vrcladder.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides interface to create and manage Sessions (connections) to the database (data source).
 */
public class SessionManager {

    private SessionFactory sessionFactory;
    private List<Session> sessions;

    public SessionManager() {
        StandardServiceRegistry serviceRegistry = buildServiceRegistry();
        sessionFactory = buildSessionFactory(serviceRegistry);

        sessions = new ArrayList<>();
    }

    public void shutDown() {
        if (sessionFactory != null) {
            // Close open sessions.
            for (Session session : sessions) {
                if (session.isConnected()) {
                    session.close();
                }
            }

            sessionFactory.close();
        }
    }

    public Session getSession() {
        Session newSession = sessionFactory.openSession();
        sessions.add(newSession);

        return newSession;
    }

    private StandardServiceRegistry buildServiceRegistry() {
        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
        serviceRegistryBuilder.configure();
        serviceRegistryBuilder.enableAutoClose();

        return serviceRegistryBuilder.build();
    }

    private SessionFactory buildSessionFactory(StandardServiceRegistry serviceRegistry) {
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        Metadata metadata = metadataSources.buildMetadata();

        return metadata.buildSessionFactory();
    }

}
