package ca.sfu.cmpt373.alpha.vrcrest;

import ca.sfu.cmpt373.alpha.vrcladder.ApplicationManager;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserBuilder;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcrest.routes.MatchGroupRouter;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.PasswordManager;
import ca.sfu.cmpt373.alpha.vrcrest.routes.RestRouter;
import ca.sfu.cmpt373.alpha.vrcrest.routes.TeamRouter;
import ca.sfu.cmpt373.alpha.vrcrest.routes.UserRouter;
import org.apache.commons.lang3.StringUtils;
import spark.servlet.SparkApplication;

import java.util.Arrays;
import java.util.List;


/**
 * A class for deploying Spark on Servers/Servlet containers such as TomCat or GlassFish
 * This is used primarily for Amazon Web Services Deployment
 */
public class RestApplication implements SparkApplication {

    private RestApi restApi;

    @Override
    public void init() {
        SessionManager sessionManager = new SessionManager();
        PasswordManager passwordManager = new PasswordManager();
        UserManager userManager = new UserManager(sessionManager);
        TeamManager teamManager = new TeamManager(sessionManager);
        MatchGroupManager matchGroupManager = new MatchGroupManager(sessionManager);
        ApplicationManager appManager = new ApplicationManager(
                sessionManager,
                passwordManager,
                userManager,
                teamManager,
                matchGroupManager);

        createDummyData(userManager, teamManager, matchGroupManager);

        UserRouter userRouter = new UserRouter(appManager.getPasswordManager(), appManager.getUserManager());
        TeamRouter teamRouter = new TeamRouter(appManager.getTeamManager());
        MatchGroupRouter matchGroupRouter = new MatchGroupRouter(
                appManager.getMatchGroupManager(),
                appManager.getTeamManager());
        List<RestRouter> routers = Arrays.asList(userRouter, teamRouter, matchGroupRouter);
        restApi = new RestApi(appManager, routers);
    }

    private void createDummyData(UserManager userManager, TeamManager teamManager, MatchGroupManager matchGroupManager) {
        //TODO: delete this
        User user1 = userManager.create(new UserBuilder()
                .setUserId("1")
                .setUserRole(UserRole.PLAYER)
                .setFirstName("First")
                .setMiddleName(StringUtils.EMPTY)
                .setLastName("Last")
                .setEmailAddress("Easdfasdmail@test.com")
                .setPhoneNumber("1111111111")
                .buildUser());
        User user2 = userManager.create(new UserBuilder()
                .setUserId("2")
                .setUserRole(UserRole.PLAYER)
                .setFirstName("First")
                .setMiddleName(StringUtils.EMPTY)
                .setLastName("Last")
                .setEmailAddress("Easdfmfgjail@test.com")
                .setPhoneNumber("1111111111")
                .buildUser());
        User user3 = userManager.create(new UserBuilder()
                .setUserId("3")
                .setUserRole(UserRole.PLAYER)
                .setFirstName("First")
                .setMiddleName(StringUtils.EMPTY)
                .setLastName("Last")
                .setEmailAddress("Emasdfasdfil@test.com")
                .setPhoneNumber("1111111111")
                .buildUser());
        User user4 = userManager.create(new UserBuilder()
                .setUserId("4")
                .setUserRole(UserRole.PLAYER)
                .setFirstName("First")
                .setMiddleName(StringUtils.EMPTY)
                .setLastName("Last")
                .setEmailAddress("Easdfmail@test.com")
                .setPhoneNumber("1111111111")
                .buildUser());
        User user5 = userManager.create(new UserBuilder()
                .setUserId("5")
                .setUserRole(UserRole.PLAYER)
                .setFirstName("First")
                .setMiddleName(StringUtils.EMPTY)
                .setLastName("Last")
                .setEmailAddress("Emaasdfasdfil@test.com")
                .setPhoneNumber("1111111111")
                .buildUser());
        User user6 = userManager.create(new UserBuilder()
                .setUserId("6")
                .setUserRole(UserRole.PLAYER)
                .setFirstName("First")
                .setMiddleName(StringUtils.EMPTY)
                .setLastName("Last")
                .setEmailAddress("Emaasdfgggail@test.com")
                .setPhoneNumber("1111111111")
                .buildUser());
        Team team1 = teamManager.create(user1, user2);
        Team team2 = teamManager.create(user3, user4);
        Team team3 = teamManager.create(user5, user6);
        teamManager.updateAttendancePlaytime(team1.getId(), PlayTime.TIME_SLOT_A);
        teamManager.updateAttendancePlaytime(team2.getId(), PlayTime.TIME_SLOT_A);
        teamManager.updateAttendancePlaytime(team3.getId(), PlayTime.TIME_SLOT_A);
        List<MatchGroup> matchGroups = matchGroupManager.getAll();
        MatchGroup matchGroup1 = matchGroupManager.create(Arrays.asList(team1, team2, team3));
        matchGroups = matchGroupManager.getAll();
        List<Team> teams = teamManager.getAll();
    }

    @Override
    public void destroy() {
        restApi.shutDown();
    }

}
