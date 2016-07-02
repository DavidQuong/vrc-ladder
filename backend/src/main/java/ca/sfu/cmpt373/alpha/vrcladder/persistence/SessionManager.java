package ca.sfu.cmpt373.alpha.vrcladder.persistence;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.scores.ScoreCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.ConfigurationManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Provides interface to create and manage Sessions (connections) to the database (data source).
 */
public class SessionManager {

    private SessionFactory sessionFactory;
    private ConfigurationManager configurationManager;

    private static final String PROPERTY_CONNECTION_URL = "hibernate.connection.url";
    private static final String PROPERTY_DRIVER = "hibernate.connection.driver_class";
    private static final String PROPERTY_USERNAME = "hibernate.connection.username";
    private static final String PROPERTY_PASSWORD = "hibernate.connection.password";
    private static final String PROPERTY_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_CREATE_MODE = "hibernate.hbm2ddl.auto";

    public SessionManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
        sessionFactory = buildSessionFactory();
    }

    public void shutDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    private SessionFactory buildSessionFactory() {
        return new Configuration().configure()
                .addAnnotatedClass(AttendanceCard.class)
                .addAnnotatedClass(MatchGroup.class)
                .addAnnotatedClass(ScoreCard.class)
                .addAnnotatedClass(Team.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Court.class)
                .setProperty(PROPERTY_CONNECTION_URL, configurationManager.getDatabaseUrl())
                .setProperty(PROPERTY_DRIVER, configurationManager.getDatabaseDriver())
                .setProperty(PROPERTY_USERNAME, configurationManager.getDatabaseUsername())
                .setProperty(PROPERTY_PASSWORD, configurationManager.getDatabasePassword())
                .setProperty(PROPERTY_DIALECT, configurationManager.getDatabaseDialect())
                .setProperty(PROPERTY_CREATE_MODE, configurationManager.getDatabaseCreateMode())
                .buildSessionFactory();
    }
}
