package ca.sfu.cmpt373.alpha.vrcladder.persistence;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
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
import org.hibernate.cfg.Configuration;

/**
 * Provides interface to create and manage Sessions (connections) to the database (data source).
 */
public class SessionManager {

    private SessionFactory sessionFactory;

    public SessionManager() {
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
//                .setProperty("hibernate.connection.url", )
                .buildSessionFactory();
    }

//    public String getRemoteConnectionUrl() {
//String dbName = System.getProperty("RDS_DB_NAME");
//    String userName = System.getProperty("RDS_USERNAME");
//    String password = System.getProperty("RDS_PASSWORD");
//    String hostname = System.getProperty("RDS_HOSTNAME");
//    String port = System.getProperty("RDS_PORT");
    //    String jdbcUrl = "jdbc:postgresql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;

//    }

}
