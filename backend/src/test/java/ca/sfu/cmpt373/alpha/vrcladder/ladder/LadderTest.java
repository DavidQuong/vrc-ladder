package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import java.util.ArrayList;
import java.util.List;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.MatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

class LadderTest {

	private final int ATTENDING = 0;
	private final int NOT_ATTENDING = 1;
	private final int TEST_CASE_COUNT = 9;
	private boolean initialized = false;

	//List of Lists of Teams, first index is Case, second is ATTENDING or NOT_ATTENDING
	ArrayList<ArrayList<ArrayList<Team>>> testTeams;

	LadderTest() {

       this.genTeams();

	}

	private void genTeams() {

		//Case 0: All non-attending teams said so,         all attending teams present
		//Case 1: All non-attending teams said so,         all attending teams late
		//Case 2: All non-attending teams said attendance, all attending teams late
		//Case 3: All non-attending teams said attendance, all attending teams present
		//Case 4: Others vary,                             all attending teams late,
		//Case 5: Others vary,                             all attending teams present
		//Case 6: All non-attending teams said so,         others vary
		//Case 7: All non-attending teams said attendance, others vary
		//Case 8: All vary

		if(!this.initialized) {

			TeamManager teamGen = new TeamManager(new SessionManager());
            int userId = 0;

			this.testTeams = new ArrayList<ArrayList<ArrayList<Team>>>();

			for(int testCase = 1;testCase < TEST_CASE_COUNT;testCase++) { //Adding cases

				testTeams.add(new ArrayList<ArrayList<Team>>());

				for(int attendance = 0;attendance < 2;attendance++) { //Adding ATTENDING/NOT_ATTENDING

					testTeams.get(testCase).add(new ArrayList<Team>());

					for(int i = 0;i < 25;i++) {

                        User newUser1 = new User();
                        newUser1.setUserId(new Integer(userId).toString());
                        newUser1.setUserRole(UserRole.PLAYER);
                        newUser1.setFirstName("jimbo");
                        newUser1.setMiddleName("");
                        newUser1.setLastName("mcgaggen");
                        newUser1.setEmailAddress("jimbo@mcgaggen.net");
                        newUser1.setPhoneNumber("6041234567");

                        userId++;

                        User newUser2 = new User();
                        newUser2.setUserId(new Integer(userId).toString());
                        newUser2.setUserRole(UserRole.PLAYER);
                        newUser2.setFirstName("jimbo");
                        newUser2.setMiddleName("");
                        newUser2.setLastName("mcgaggen");
                        newUser2.setEmailAddress("jimbo@mcgaggen.net");
                        newUser2.setPhoneNumber("6041234567");

                        userId++;

						Team creation = teamGen.create(newUser1, newUser2);

						if(attendance == ATTENDING) {

							if(testCase == 0 || testCase == 3 || testCase == 5) {

								//creation.setAttendance(PRESENT);
                                System.out.println("Set Attendance PRESENT");

							} else if(testCase == 1 || testCase == 2 || testCase == 4) {

								//creation.setAttendance(LATE);
                                System.out.println("Set Attendance LATE");

							} else if(i % 2 == 0) {

								//creation.setAttendance(PRESENT);
                                System.out.println("Set Attendance PRESENT");

							} else {

								//creation.setAttendance(LATE);
                                System.out.println("Set Attendance LATE");

							}

						} else { //NOT_ATTENDING

							if(testCase == 0 || testCase == 1 || testCase == 6) {

                                AttendanceCard status = new AttendanceCard();
                                status.setPreferredPlayTime(PlayTime.NONE);
                                creation.setAttendanceCard(status);

							} else if(testCase == 2 || testCase == 3 || testCase == 7) {

								//creation.setAttendance(NO_SHOW);
                                System.out.println("Set Attendance NO_SHOW");

							} else if(i % 2 == 0) {

                                AttendanceCard status = new AttendanceCard();
                                status.setPreferredPlayTime(PlayTime.NONE);
                                creation.setAttendanceCard(status);

							} else {

								//creation.setAttendance(NO_SHOW);
                                System.out.println("Set Attendance NO_SHOW");

							}

						}

						testTeams.get(testCase).get(attendance).add(creation);

					}

				}

			}

		}

		this.initialized = true;

	}

	//First index is ATTENDING or NOT_ATTENDING
	private ArrayList<ArrayList<Team>> getTeams(int amount, int testCase) {

		ArrayList<ArrayList<Team>> casedList = this.testTeams.get(testCase);

		if(amount >= casedList.size()) {

			throw new RuntimeException("Bad amount in GetTeams (" + amount + " >= " + casedList.size() + ")") ;

		}
		if(amount * 2 >= casedList.size()) {

			ArrayList<ArrayList<Team>> result = new ArrayList<ArrayList<Team>>();
			result.add(new ArrayList<Team>(casedList.get(ATTENDING).subList(amount, casedList.size())));
			result.get(ATTENDING).addAll(new ArrayList<Team>(casedList.get(ATTENDING).subList(0, (amount * 2) - casedList.size())));
			result.add(new ArrayList<Team>(casedList.get(NOT_ATTENDING).subList(amount, casedList.size())));
			result.get(NOT_ATTENDING).addAll(new ArrayList<Team>(casedList.get(NOT_ATTENDING).subList(0, (amount * 2) - casedList.size())));
			return result;

		} else {

			ArrayList<ArrayList<Team>> result = new ArrayList<ArrayList<Team>>();
			result.add(new ArrayList<Team>(casedList.get(ATTENDING).subList(amount, amount * 2)));
			result.add(new ArrayList<Team>(casedList.get(NOT_ATTENDING).subList(amount, amount * 2)));
			return result;

		}

	}

	@Test
	private void TestCase1() { //Case 1: Groups are directly beside one another, groups of 3 only

		int successes = 0;
		MatchGroupGenerator getGroups = new MatchGroupGenerator();

		int teamCount = 9;
		ArrayList<ArrayList<Team>> expectedResults = new ArrayList<ArrayList<Team>>();
		for(int i = 0;i < TEST_CASE_COUNT;i++) {
           ArrayList<Team> sourceResults = getTeams(teamCount, i).get(ATTENDING);
           expectedResults.add(new ArrayList<Team>()); //2, 1, 4, 0, 3, 6, 5, 7, 8
           if(i < 6) {
               expectedResults.get(i).add(sourceResults.get(2));
               expectedResults.get(i).add(sourceResults.get(1));
               expectedResults.get(i).add(sourceResults.get(4));
               expectedResults.get(i).add(sourceResults.get(0));
               expectedResults.get(i).add(sourceResults.get(3));
               expectedResults.get(i).add(sourceResults.get(6));
               expectedResults.get(i).add(sourceResults.get(5));
               expectedResults.get(i).add(sourceResults.get(7));
               expectedResults.get(i).add(sourceResults.get(8));
           } else { //Factoring late
               expectedResults.get(i).add(sourceResults.get(1));
               expectedResults.get(i).add(sourceResults.get(3));
               expectedResults.get(i).add(sourceResults.get(5));
               expectedResults.get(i).add(sourceResults.get(7));
               expectedResults.get(i).add(sourceResults.get(2));
               expectedResults.get(i).add(sourceResults.get(4));
               expectedResults.get(i).add(sourceResults.get(0));
               expectedResults.get(i).add(sourceResults.get(6));
               expectedResults.get(i).add(sourceResults.get(8));
           }
		}

		for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {

			ArrayList<ArrayList<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
			ArrayList<Team> ladderTestTeams = new ArrayList<Team>();

           ladderTestTeams = ladderTestTeamsSeg.get(ATTENDING);

			List<MatchGroup> ladderTestGroups = getGroups.generateMatchGroupings(ladderTestTeams);

			//3 groups, 3-2-1, 2-1-3, 1-2-3
            System.out.println("Add win/loss data");

			Ladder testLadder = new Ladder(ladderTestTeams);
			testLadder.updateLadder(ladderTestGroups.toArray(new MatchGroup[ladderTestGroups.size()])); //Conversion obtained from http://stackoverflow.com/questions/9572795/convert-list-to-array-in-java

			assertTrue(expectedResults.get(testCase).equals(testLadder.ladderList));

		}

	}

	@Test
	private void TestCase2() { //Case 2: Groups are directly beside one another, groups of 4 only

       int successes = 0;
       MatchGroupGenerator getGroups = new MatchGroupGenerator();

       int teamCount = 12;
       ArrayList<ArrayList<Team>> expectedResults = new ArrayList<ArrayList<Team>>();
       for(int i = 0;i < TEST_CASE_COUNT;i++) {
           ArrayList<Team> sourceResults = getTeams(teamCount, i).get(ATTENDING);
           expectedResults.add(new ArrayList<Team>()); //0, 1, 2, 6, 3, 7, 5, 9, 4, 8, 10, 11
           if(i < 6) {
               expectedResults.get(i).add(sourceResults.get(0));
               expectedResults.get(i).add(sourceResults.get(1));
               expectedResults.get(i).add(sourceResults.get(2));
               expectedResults.get(i).add(sourceResults.get(6));
               expectedResults.get(i).add(sourceResults.get(3));
               expectedResults.get(i).add(sourceResults.get(7));
               expectedResults.get(i).add(sourceResults.get(5));
               expectedResults.get(i).add(sourceResults.get(9));
               expectedResults.get(i).add(sourceResults.get(4));
               expectedResults.get(i).add(sourceResults.get(8));
               expectedResults.get(i).add(sourceResults.get(10));
               expectedResults.get(i).add(sourceResults.get(11));
           } else { //Factoring late
               expectedResults.get(i).add(sourceResults.get(0));
               expectedResults.get(i).add(sourceResults.get(2));
               expectedResults.get(i).add(sourceResults.get(6));
               expectedResults.get(i).add(sourceResults.get(4));
               expectedResults.get(i).add(sourceResults.get(8));
               expectedResults.get(i).add(sourceResults.get(1));
               expectedResults.get(i).add(sourceResults.get(10));
               expectedResults.get(i).add(sourceResults.get(3));
               expectedResults.get(i).add(sourceResults.get(7));
               expectedResults.get(i).add(sourceResults.get(5));
               expectedResults.get(i).add(sourceResults.get(9));
               expectedResults.get(i).add(sourceResults.get(11));
           }
       }

       for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {

           ArrayList<ArrayList<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
           ArrayList<Team> ladderTestTeams = new ArrayList<Team>();
           ArrayList<Team> sourceResults = getTeams(teamCount, testCase).get(ATTENDING);

           ladderTestTeams = ladderTestTeamsSeg.get(ATTENDING);

           ArrayList<MatchGroup> ladderTestGroups = new ArrayList<MatchGroup>();
           ladderTestGroups.add(new MatchGroup(sourceResults.get(0), sourceResults.get(1), sourceResults.get(2), sourceResults.get(3)));
           ladderTestGroups.add(new MatchGroup(sourceResults.get(4), sourceResults.get(5), sourceResults.get(6), sourceResults.get(7)));
           ladderTestGroups.add(new MatchGroup(sourceResults.get(8), sourceResults.get(9), sourceResults.get(10), sourceResults.get(11)));

           //1-2-3-4, 3-4-2-1, 2-1-3-4
           System.out.println("Add win/loss data");

           Ladder testLadder = new Ladder(ladderTestTeams);
           testLadder.updateLadder(ladderTestGroups.toArray(new MatchGroup[ladderTestGroups.size()]));

           assertTrue(expectedResults.get(testCase).equals(testLadder.ladderList));

       }

	}

	@Test
	private void TestCase3() { //Case 3: Groups are directly beside one another, groups of 3/4, not predictably interchanging (At least one of each)

       int successes = 0;
       MatchGroupGenerator getGroups = new MatchGroupGenerator();

       int teamCount = 17;
       ArrayList<ArrayList<Team>> expectedResults = new ArrayList<ArrayList<Team>>();
       for(int i = 0;i < TEST_CASE_COUNT;i++) {
           ArrayList<Team> sourceResults = getTeams(teamCount, i).get(ATTENDING);
           expectedResults.add(new ArrayList<Team>()); //1, 3, 0, 6, 2, 4, 9, 5, 8, 13, 7, 12, 11, 15, 10, 14, 16
           if(1 < 6) {
               expectedResults.get(i).add(sourceResults.get(1));
               expectedResults.get(i).add(sourceResults.get(3));
               expectedResults.get(i).add(sourceResults.get(0));
               expectedResults.get(i).add(sourceResults.get(6));
               expectedResults.get(i).add(sourceResults.get(2));
               expectedResults.get(i).add(sourceResults.get(4));
               expectedResults.get(i).add(sourceResults.get(9));
               expectedResults.get(i).add(sourceResults.get(5));
               expectedResults.get(i).add(sourceResults.get(8));
               expectedResults.get(i).add(sourceResults.get(13));
               expectedResults.get(i).add(sourceResults.get(7));
               expectedResults.get(i).add(sourceResults.get(12));
               expectedResults.get(i).add(sourceResults.get(11));
               expectedResults.get(i).add(sourceResults.get(15));
               expectedResults.get(i).add(sourceResults.get(10));
               expectedResults.get(i).add(sourceResults.get(14));
               expectedResults.get(i).add(sourceResults.get(16));
           } else {
               expectedResults.get(i).add(sourceResults.get(1));
               expectedResults.get(i).add(sourceResults.get(3));
               expectedResults.get(i).add(sourceResults.get(9));
               expectedResults.get(i).add(sourceResults.get(5));
               expectedResults.get(i).add(sourceResults.get(13));
               expectedResults.get(i).add(sourceResults.get(7));
               expectedResults.get(i).add(sourceResults.get(0));
               expectedResults.get(i).add(sourceResults.get(6));
               expectedResults.get(i).add(sourceResults.get(2));
               expectedResults.get(i).add(sourceResults.get(4));
               expectedResults.get(i).add(sourceResults.get(11));
               expectedResults.get(i).add(sourceResults.get(15));
               expectedResults.get(i).add(sourceResults.get(8));
               expectedResults.get(i).add(sourceResults.get(12));
               expectedResults.get(i).add(sourceResults.get(10));
               expectedResults.get(i).add(sourceResults.get(14));
               expectedResults.get(i).add(sourceResults.get(16));
           }
       }

       for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {

           ArrayList<ArrayList<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
           ArrayList<Team> ladderTestTeams = new ArrayList<Team>();
           ArrayList<Team> sourceResults = getTeams(teamCount, testCase).get(ATTENDING);

           ladderTestTeams = ladderTestTeamsSeg.get(ATTENDING);
           System.out.println("Add win/loss data");

           ArrayList<MatchGroup> ladderTestGroups = new ArrayList<MatchGroup>();
           ladderTestGroups.add(new MatchGroup(sourceResults.get(0), sourceResults.get(1), sourceResults.get(2), sourceResults.get(3)));
           ladderTestGroups.add(new MatchGroup(sourceResults.get(4), sourceResults.get(5), sourceResults.get(6)));
           ladderTestGroups.add(new MatchGroup(sourceResults.get(7), sourceResults.get(8), sourceResults.get(9)));
           ladderTestGroups.add(new MatchGroup(sourceResults.get(10), sourceResults.get(11), sourceResults.get(12), sourceResults.get(13)));
           ladderTestGroups.add(new MatchGroup(sourceResults.get(14), sourceResults.get(15), sourceResults.get(16)));

           //2-4-1-3, 3-1-2, 3-2-1, 4-3-2-1, 2-1-3

           Ladder testLadder = new Ladder(ladderTestTeams);
           testLadder.updateLadder(ladderTestGroups.toArray(new MatchGroup[ladderTestGroups.size()]));

           assertTrue(expectedResults.get(testCase).equals(testLadder.ladderList));

       }

	}

	@Test
	private void TestCase4() { //Case 4: Groups have non-attending teams between them, groups of 3 only

       int successes = 0;
       MatchGroupGenerator getGroups = new MatchGroupGenerator();

       int teamCount = 6;
       ArrayList<ArrayList<Team>> expectedResults = new ArrayList<ArrayList<Team>>();
       for(int i = 0;i < TEST_CASE_COUNT;i++) {
           ArrayList<Team> sourceResults1 = getTeams(teamCount, i).get(ATTENDING);
           ArrayList<Team> sourceResults2 = getTeams(teamCount, i).get(NOT_ATTENDING);
           expectedResults.add(new ArrayList<Team>()); //2, 1, 3, 0, 4, 5
           switch(i) {
               case(0):
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults1.get(5));
                   expectedResults.get(i).add(sourceResults2.get(2));
               case(1):
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults1.get(5));
               case(2):
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults1.get(5));
               case(3):
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults1.get(5));
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults2.get(2));
               case(4):
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults1.get(5));
               case(5):
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults1.get(5));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults2.get(1));
               case(6):
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(5));
               case(7):
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults1.get(5));
               case(8):
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(5));
           }
       }

       for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {

           ArrayList<ArrayList<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
           ArrayList<Team> ladderTestTeams = new ArrayList<Team>();

           ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(0));
           ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(0));
           ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(1));
           ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(2));
           ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(1));
           ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(3));
           ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(4));
           ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(5));
           ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(2));

           List<MatchGroup> ladderTestGroups = getGroups.generateMatchGroupings(ladderTestTeams);

           //3-2-1, 1-2-3
           System.out.println("Add win/loss data");

           Ladder testLadder = new Ladder(ladderTestTeams);
           testLadder.updateLadder(ladderTestGroups.toArray(new MatchGroup[ladderTestGroups.size()]));

           assertTrue(expectedResults.get(testCase).equals(testLadder.ladderList));

       }

	}

	@Test
	private void TestCase5() { //Case 5: Groups have non-attending teams between them, groups of 4 only

       int successes = 0;
       MatchGroupGenerator getGroups = new MatchGroupGenerator();

       int teamCount = 8;
       ArrayList<ArrayList<Team>> expectedResults = new ArrayList<ArrayList<Team>>();
       ArrayList<Team> sourceResults = getTeams(teamCount, 0).get(ATTENDING);
       for(int i = 0;i < TEST_CASE_COUNT;i++) {
           ArrayList<Team> sourceResults1 = getTeams(teamCount, i).get(ATTENDING);
           ArrayList<Team> sourceResults2 = getTeams(teamCount, i).get(NOT_ATTENDING);
           expectedResults.add(new ArrayList<Team>()); //2, 0, 1, 4, 3, 5, 7, 6
           switch(i) {
               case(0):
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(5));
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults1.get(7));
                   expectedResults.get(i).add(sourceResults1.get(6));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults2.get(3));
               case(1):
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults2.get(3));
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(5));
                   expectedResults.get(i).add(sourceResults1.get(7));
                   expectedResults.get(i).add(sourceResults1.get(6));
               case(2):
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults2.get(3));
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(5));
                   expectedResults.get(i).add(sourceResults1.get(7));
                   expectedResults.get(i).add(sourceResults1.get(6));
               case(3):
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(5));
                   expectedResults.get(i).add(sourceResults1.get(7));
                   expectedResults.get(i).add(sourceResults1.get(6));
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults2.get(3));
               case(4):
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults2.get(3));
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(5));
                   expectedResults.get(i).add(sourceResults1.get(7));
                   expectedResults.get(i).add(sourceResults1.get(6));
               case(5):
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(5));
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults1.get(7));
                   expectedResults.get(i).add(sourceResults1.get(6));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults2.get(3));
               case(6):
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults1.get(6));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(5));
                   expectedResults.get(i).add(sourceResults2.get(3));
                   expectedResults.get(i).add(sourceResults1.get(7));
               case(7):
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults1.get(6));
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(5));
                   expectedResults.get(i).add(sourceResults1.get(7));
                   expectedResults.get(i).add(sourceResults2.get(3));
               case(8):
                   expectedResults.get(i).add(sourceResults1.get(2));
                   expectedResults.get(i).add(sourceResults1.get(0));
                   expectedResults.get(i).add(sourceResults1.get(4));
                   expectedResults.get(i).add(sourceResults2.get(0));
                   expectedResults.get(i).add(sourceResults1.get(6));
                   expectedResults.get(i).add(sourceResults2.get(2));
                   expectedResults.get(i).add(sourceResults1.get(1));
                   expectedResults.get(i).add(sourceResults2.get(1));
                   expectedResults.get(i).add(sourceResults2.get(3));
                   expectedResults.get(i).add(sourceResults1.get(3));
                   expectedResults.get(i).add(sourceResults1.get(5));
                   expectedResults.get(i).add(sourceResults1.get(7));
           }
       }

       for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {

           ArrayList<ArrayList<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
           ArrayList<Team> ladderTestTeams = new ArrayList<Team>();

           ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(0));
           ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(1));
           ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(2));
           ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(3));
           ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(0));
           ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(1));
           ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(4));
           ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(5));
           ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(6));
           ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(7));
           ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(2));
           ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(3));

           List<MatchGroup> ladderTestGroups = getGroups.generateMatchGroupings(ladderTestTeams);

           //3-1-2-4, 1-2-4-3
           System.out.println("Add win/loss data");

           Ladder testLadder = new Ladder(ladderTestTeams);
           testLadder.updateLadder(ladderTestGroups.toArray(new MatchGroup[ladderTestGroups.size()]));

           assertTrue(expectedResults.get(testCase).equals(testLadder.ladderList));

       }

	}

	@Test
	private void TestCase6() { //Case 6: Single group of 3, other non-attending teams around

		//here

	}

	@Test
	private void TestCase7() { //Case 7: Single group of 4, other non-attending teams around

		//here

	}

	@Test
	private void TestCase8() { //Case 8: Groups have non-attending teams beside and inside them, groups of 3 only, all overlapping

		//here

	}

	@Test
	private void TestCase9() { //Case 9: Groups have non-attending teams beside and inside them, groups of 4 only, all overlapping

		//here

	}

	@Test
	private void TestCase10() { //Case 10: Groups have non-attending teams beside and inside them, groups of 3/4, all overlapping, not predictably interchanging (At least one of each)

		//here

	}

	//Credits: Got way to get a sublist from http://beginnersbook.com/2013/12/how-to-get-sublist-of-an-arraylist-with-example/

}