package ca.sfu.cmpt373.alpha.vrcladder.persistence;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.SecurityManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import ca.sfu.cmpt373.alpha.vrcladder.util.ConfigurationManager;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.junit.Assert;
import org.junit.Test;

import java.security.Key;

public class CsvImporterTest extends BaseTest {
    private static final String CSV_COLUMNS_HEADING = "Last Name,First Name,Player #,Status,,Last Name,First Name,Player #,Status,Ranking\n";
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private Key signatureKey = MacProvider.generateKey();
    private SecurityManager securityManager = new SecurityManager(signatureAlgorithm, signatureKey);
    private SessionManager sessionManager = new SessionManager(new ConfigurationManager());
    private UserManager userManager = new UserManager(sessionManager);
    private TeamManager teamManager = new TeamManager(sessionManager);

    @Test
    public void testAddTeam() {
        String userIdCindy = "9217";
        String userIdAlex = "5447";
        String csvString = CSV_COLUMNS_HEADING +
                "Truong,Cindy," + userIdCindy + ",,&,Wong,Alex," + userIdAlex+ ",,1\n";
        CsvImporter csvImporter = new CsvImporter(userManager, teamManager, securityManager);
        String errorLog = csvImporter.insertData(csvString);

        Assert.assertEquals("", errorLog);

        //en exception will be thrown if these don't exist
        User cindy = userManager.getById(new UserId(userIdCindy));
        User alex = userManager.getById(new UserId(userIdAlex));

        Team createdTeam = teamManager.getAll().get(0);
        Assert.assertEquals(cindy, createdTeam.getFirstPlayer());
        Assert.assertEquals(alex, createdTeam.getSecondPlayer());
    }

    @Test
    public void testAddSamePlayerToMultipleTeams() {
        String userIdCindy = "9217";
        String userIdAlex = "5447";
        String userIdBob = "1234";
        String csvString = CSV_COLUMNS_HEADING +
                "Truong,Cindy," + userIdCindy + ",,&,Wong,Alex," + userIdAlex+ ",,1\n" +
                "Joe,Bob," + userIdBob + ",,&,Truong,Cindy," + userIdCindy + ",,2\n";
        CsvImporter csvImporter = new CsvImporter(userManager, teamManager, securityManager);
        String errorLog = csvImporter.insertData(csvString);

        Assert.assertEquals("", errorLog);

        //en exception will be thrown if these don't exist
        User cindy = userManager.getById(new UserId(userIdCindy));
        User alex = userManager.getById(new UserId(userIdAlex));
        User bob = userManager.getById(new UserId(userIdBob));

        Team firstTeam = teamManager.getAll().get(0);
        Assert.assertEquals(cindy, firstTeam.getFirstPlayer());
        Assert.assertEquals(alex, firstTeam.getSecondPlayer());

        Team secondTeam = teamManager.getAll().get(1);
        Assert.assertEquals(bob, secondTeam.getFirstPlayer());
        Assert.assertEquals(cindy, secondTeam.getSecondPlayer());
    }
}
