package ca.sfu.cmpt373.alpha.vrcladder.persistence;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.scores.ScoreCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Provides interface to create and manage Sessions (connections) to the database (data source).
 */
public class SessionManager {

    private SessionFactory sessionFactory;

    public SessionManager() {
        StandardServiceRegistry serviceRegistry = buildServiceRegistry();
        sessionFactory = buildSessionFactory(serviceRegistry);
    }

    public void shutDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    private StandardServiceRegistry buildServiceRegistry() {
        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder()
            .configure()
            .enableAutoClose();

        return serviceRegistryBuilder.build();
    }

    private SessionFactory buildSessionFactory(StandardServiceRegistry serviceRegistry) {
        MetadataSources metadataSources = addAnnotatedClasses(serviceRegistry);
        Metadata metadata = metadataSources.buildMetadata();

        return metadata.buildSessionFactory();
    }

    private MetadataSources addAnnotatedClasses(StandardServiceRegistry serviceRegistry) {
        MetadataSources metadataSources = new MetadataSources(serviceRegistry)
            .addAnnotatedClass(AttendanceCard.class)
            .addAnnotatedClass(MatchGroup.class)
            .addAnnotatedClass(ScoreCard.class)
            .addAnnotatedClass(Team.class)
            .addAnnotatedClass(User.class);

        return metadataSources;
    }

}
