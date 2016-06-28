package ca.sfu.cmpt373.alpha.vrcladder.persistance;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.LadderPosition;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserBuilder;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.Password;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder for the actual database to provide dummy data for implementation testing purposes
 *
 * TODO - Delete this as it is being replaced by the Mock<Object>Generator classes.
 */
public class MockDatabase {

    private static List<User> generateMockUsers (int numUsers) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < numUsers; i++) {
            users.add(new UserBuilder()
                    .setUserId("" + i)
                    .setEmailAddress("test@test.com")
                    .setFirstName("TestUser " + i)
                    .setMiddleName("MiddleName")
                    .setLastName("LastName")
                    .setPhoneNumber("778-235-4841")
                    .setUserRole(UserRole.PLAYER)
                    .setPassword(new Password("testHash"))
                    .buildUser());
        }
        return users;
    }

    private static List<Team> generateMockTeams(List<User> users) {
        List<Team> teams = new ArrayList<>();
        User previousUser = null;
        //ranking is just incremented after a team is made
        for (User user : users) {
            if (previousUser == null) {
                previousUser = user;
            } else {
                Team team = new Team(previousUser, user, new LadderPosition(1));
                team.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
                teams.add(team);
                previousUser = null;
            }
        }

        if (previousUser != null) {
            throw new IllegalStateException("Odd number of users. One user not sorted into a team");
        }

        return teams;
    }

    /**
     * @return  a list of teams in the order corresponding to team rankings on the ladder
     */
    public static List<Team> getRankedLadderTeams(int numTeams) {
        //number of users should be twice the number of teams since each team needs two users
        int numUsers = numTeams * 2;
        return generateMockTeams(generateMockUsers(numUsers));
    }

    /**
     * In the real database, the interface would not let the user select the number of courts.
     * The courts would be fixed to 6 courts (what vrc has)
     * * @return a list of court objects
     */
    public static List<MatchGroup> getMockMatchGroups(int matchGroupCount) {
        int playersNeededToFormGroup = 6;
        List<MatchGroup> matchGroups = new ArrayList<>();
        for (int i = 0; i < matchGroupCount; i++) {
            matchGroups.add(new MatchGroup(
                    generateMockTeams(generateMockUsers(playersNeededToFormGroup))));
        }
        return matchGroups;
    }
}
