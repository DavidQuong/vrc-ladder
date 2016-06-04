package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import java.util.ArrayList;
import java.util.Collections;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

class LadderTest {
	
	private final int ATTENDING = 0;
	private final int NOT_ATTENDING = 1;
    private final int TEST_CASE_COUNT = 9;
	private boolean initialized = false;
	
	//List of Lists of Teams, first index is Case, second is ATTENDING or NOT_ATTENDING
	ArrayList<ArrayList<ArrayList<Team>>> testTeams;

	LadderTest() {

        this.GenTeams();
		
	}

	private void GenTeams() {
		
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

			TeamGenerator teamGen = new TeamGenerator();

			this.testTeams = new ArrayList<ArrayList<ArrayList<Team>>>();

			for(int testCase = 1;testCase < TEST_CASE_COUNT;testCase++) { //Adding cases

				testTeams.add(new ArrayList<ArrayList<Team>>());

				for(int attendance = 0;attendance < 2;attendance++) { //Adding ATTENDING/NOT_ATTENDING

					testTeams.get(testCase).add(new ArrayList<Team>());

					for(int i = 0;i < 25;i++) {

						Team creation = teamGen.generateTeam();

						if(attendance == ATTENDING) {

							if(testCase == 0 || testCase == 3 || testCase == 5) {

								creation.setAttendance(PRESENT);

							}
							else if(testCase == 1 || testCase == 2 || testCase == 4) {

								creation.setAttendance(LATE);

							}
							else if(i % 2 == 0) {

								creation.setAttendance(PRESENT);

							}
							else {

								creation.setAttendance(LATE);

							}

						}
						else { //NOT_ATTENDING

							if(testCase == 0 || testCase == 1 || testCase == 6) {

								creation.setAttendanceCard(new AttendanceCard(new IdType(), creation, PlayTime::NONE));

							}
							else if(testCase == 2 || testCase == 3 || testCase == 7) {

								creation.setAttendance(NO_SHOW);

							}
							else if(i % 2 == 0) {

								creation.setAttendanceCard(new AttendanceCard(new IdType(), creation, PlayTime::NONE));

							}
							else {

								creation.setAttendance(NO_SHOW);

							}

						}

						testTeams.get(testCase).get(attendance).add(creation);

					}

				}

			}

		}

		this.initialized = true;
		
	}

	public boolean RunTests() {
		
		int successes = 0;
		int miniSuccesses = 0;
		int testResults = 0;

		testResults = TestCase1();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 1 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 1 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;
		
		testResults = TestCase2();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 2 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 2 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase3();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 3 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 3 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase4();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 4 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 4 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase5();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 5 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 5 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase6();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 6 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 6 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase7();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 7 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 7 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase8();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 8 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 8 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase9();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 9 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 9 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase10();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 10 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 10 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		/*testResults = TestCase11();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 11 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 11 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;
		
		testResults = TestCase12();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 12 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 12 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase13();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 13 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 13 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase14();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 14 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 14 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase15();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 15 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 15 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase16();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 16 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 16 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase17();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 17 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 17 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase18();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 18 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 18 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase19();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 19 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 19 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;

		testResults = TestCase20();
		if(testResults == TEST_CASE_COUNT) {

			System.out.println("Test Case 20 Passed, 9/9\n");
			successes++;

		}
		else {

			System.out.println("Test Case 20 Failed, " + testResults + "/9\n");

		}
		miniSuccesses += testResults;*/
		
		if(successes == 10) {

			return true;

		}

		return false;
		
	}
	
	//First index is ATTENDING or NOT_ATTENDING
	private ArrayList<ArrayList<Team>> GetTeams(int amount, int testCase) {
		
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
					
		}
		else {

			ArrayList<ArrayList<Team>> result = new ArrayList<ArrayList<Team>>();
			result.add(new ArrayList<Team>(casedList.get(ATTENDING).subList(amount, amount * 2)));
			result.add(new ArrayList<Team>(casedList.get(NOT_ATTENDING).subList(amount, amount * 2)));
			return result;
			
		}
		
	}

	private int TestCase1() { //Case 1: Groups are directly beside one another, groups of 3 only
		
		int successes = 0;
		GroupGenerator getGroups = new GroupGenerator();
		
		int teamCount = 9;
		ArrayList<ArrayList<Team>> expectedResults = new ArrayList<ArrayList<Team>>();
        ArrayList<Team> sourceResults = GetTeams(teamCount, 0).get(ATTENDING);
		for(int i = 0;i < 9;i++) {
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
            }
            else { //Factoring late
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
		
		System.out.println("Starting Test Case 1:");

		for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {
			
			System.out.println("	Sub Case " + (testCase + 1) + ":");

			ArrayList<ArrayList<Team>> ladderTestTeamsSeg = GetTeams(teamCount, testCase);
			ArrayList<Team> ladderTestTeams = new ArrayList<Team>();

            ladderTestTeams = ladderTestTeamsSeg.get(ATTENDING);

			ArrayList<Group> ladderTestGroups = getGroups.generateMatchGroupings(ladderTestTeams);

			//3 groups, 3-2-1, 2-1-3, 1-2-3

			Ladder testLadder = new Ladder(ladderTestTeams);
			testLadder.updateLadder(ladderTestGroups);

			if(expectedResults.get(testCase).equals(testLadder.ladderList)) {
				
				System.out.println("		Passed");
				successes++;
				
			}
			else {
				
				System.out.println("		Failed");
				
			}
			
		}
		
		return successes;
		
	}

	private int TestCase2() { //Case 2: Groups are directly beside one another, groups of 4 only

        int successes = 0;
        GroupGenerator getGroups = new GroupGenerator();

        int teamCount = 12;
        ArrayList<ArrayList<Team>> expectedResults = new ArrayList<ArrayList<Team>>();
        ArrayList<Team> sourceResults = GetTeams(teamCount, 0).get(ATTENDING);
        for(int i = 0;i < TEST_CASE_COUNT;i++) {
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
            }
            else { //Factoring late
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

        System.out.println("Starting Test Case 1:");

        for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {

            System.out.println("	Sub Case " + (testCase + 1) + ":");

            ArrayList<ArrayList<Team>> ladderTestTeamsSeg = GetTeams(teamCount, testCase);
            ArrayList<Team> ladderTestTeams = new ArrayList<Team>();

            ladderTestTeams = ladderTestTeamsSeg.get(ATTENDING);

            ArrayList<Group> ladderTestGroups = new ArrayList<Group>();
            ladderTestGroups.add(new Group(sourceResults.get(0), sourceResults.get(1), sourceResults.get(2), sourceResults.get(3)));
            ladderTestGroups.add(new Group(sourceResults.get(4), sourceResults.get(5), sourceResults.get(6), sourceResults.get(7)));
            ladderTestGroups.add(new Group(sourceResults.get(8), sourceResults.get(9), sourceResults.get(10), sourceResults.get(11)));

            //1-2-3-4, 3-4-2-1, 2-1-3-4

            Ladder testLadder = new Ladder(ladderTestTeams);
            testLadder.updateLadder(ladderTestGroups);

            if(expectedResults.get(testCase).equals(testLadder.ladderList)) {

                System.out.println("		Passed");
                successes++;

            }
            else {

                System.out.println("		Failed");

            }

        }

        return successes;
		
	}

	private int TestCase3() { //Case 3: Groups are directly beside one another, groups of 3/4, not predictably interchanging (At least one of each)

        int successes = 0;
        GroupGenerator getGroups = new GroupGenerator();

        int teamCount = 17;
        ArrayList<ArrayList<Team>> expectedResults = new ArrayList<ArrayList<Team>>();
        ArrayList<Team> sourceResults = GetTeams(teamCount, 0).get(ATTENDING);
        for(int i = 0;i < TEST_CASE_COUNT;i++) {
            expectedResults.add(new ArrayList<Team>()); //1, 3, 0, 6, 2, 4, 9, 5, 8, 13, 7, 12, 11, 15, 10, 14, 16
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
        }

        System.out.println("Starting Test Case 1:");

        for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {

            System.out.println("	Sub Case " + (testCase + 1) + ":");

            ArrayList<ArrayList<Team>> ladderTestTeamsSeg = GetTeams(teamCount, testCase);
            ArrayList<Team> ladderTestTeams = new ArrayList<Team>();

            ladderTestTeams = ladderTestTeamsSeg.get(ATTENDING);

            ArrayList<Group> ladderTestGroups = new ArrayList<Group>();
            ladderTestGroups.add(new Group(sourceResults.get(0), sourceResults.get(1), sourceResults.get(2), sourceResults.get(3)), sourceResults.get(4));
            ladderTestGroups.add(new Group(sourceResults.get(5), sourceResults.get(6), sourceResults.get(7), sourceResults.get(8)));
            ladderTestGroups.add(new Group(sourceResults.get(9), sourceResults.get(10), sourceResults.get(11), sourceResults.get(12)));
            ladderTestGroups.add(new Group(sourceResults.get(13), sourceResults.get(14), sourceResults.get(15), sourceResults.get(16)), sourceResults.get(17));
            ladderTestGroups.add(new Group(sourceResults.get(18), sourceResults.get(19), sourceResults.get(20), sourceResults.get(21)));

            //2-4-1-3, 3-1-2, 3-2-1, 4-3-2-1, 2-1-3

            Ladder testLadder = new Ladder(ladderTestTeams);
            testLadder.updateLadder(ladderTestGroups);

            if(expectedResults.get(testCase).equals(testLadder.ladderList)) {

                System.out.println("		Passed");
                successes++;

            }
            else {

                System.out.println("		Failed");

            }

        }

        return successes;
		
	}

	private int TestCase4() { //Case 4: Groups have non-attending teams between them, groups of 3 only

        int successes = 0;
        GroupGenerator getGroups = new GroupGenerator();

        int teamCount = 6;
        ArrayList<ArrayList<Team>> expectedResults = new ArrayList<ArrayList<Team>>();
        ArrayList<Team> sourceResults1 = GetTeams(teamCount, 0).get(ATTENDING);
        ArrayList<Team> sourceResults2 = GetTeams(teamCount, 0).get(NOT_ATTENDING);
        for(int i = 0;i < TEST_CASE_COUNT;i++) {
            expectedResults.add(new ArrayList<Team>());
        }
        expectedResults.get(0).add(sourceResults.get(7));

        System.out.println("Starting Test Case 1:");

        for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {

            System.out.println("	Sub Case " + (testCase + 1) + ":");

            ArrayList<ArrayList<Team>> ladderTestTeamsSeg = GetTeams(teamCount, testCase);
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

            ArrayList<Group> ladderTestGroups = getGroups.generateMatchGroupings(ladderTestTeams);

            //3-2-1, 1-2-3
            //2, 1, 3, 0, 4, 5

            Ladder testLadder = new Ladder(ladderTestTeams);
            testLadder.updateLadder(ladderTestGroups);

            if(expectedResults.get(testCase).equals(testLadder.ladderList)) {

                System.out.println("		Passed");
                successes++;

            }
            else {

                System.out.println("		Failed");

            }

        }

        return successes;
		
	}

	private int TestCase5() { //Case 5: Groups have non-attending teams between them, groups of 4 only

        int successes = 0;
        GroupGenerator getGroups = new GroupGenerator();

        int teamCount = ??;
        ArrayList<ArrayList<Team>> expectedResults = new ArrayList<ArrayList<Team>>();
        ArrayList<Team> sourceResults = GetTeams(teamCount, 0).get(ATTENDING);
        for(int i = 0;i < TEST_CASE_COUNT;i++) {
            expectedResults.add(new ArrayList<Team>());
        }
        //define expected results

        System.out.println("Starting Test Case 1:");

        for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {

            System.out.println("	Sub Case " + (testCase + 1) + ":");

            ArrayList<ArrayList<Team>> ladderTestTeamsSeg = GetTeams(teamCount, testCase);
            ArrayList<Team> ladderTestTeams = new ArrayList<Team>();

            //convert ladderTestTeamsSeg to ladderTestTeams

            ArrayList<Group> ladderTestGroups = getGroups.generateMatchGroupings(ladderTestTeams);

            //add win/loss data

            Ladder testLadder = new Ladder(ladderTestTeams);
            testLadder.updateLadder(ladderTestGroups);

            if(expectedResults.get(testCase).equals(testLadder.ladderList)) {

                System.out.println("		Passed");
                successes++;

            }
            else {

                System.out.println("		Failed");

            }

        }

        return successes;
		
	}

	private int TestCase6() { //Case 6: Groups have non-attending teams between them, groups of 3/4, not predictably interchanging (At least one of each)

        int successes = 0;
        GroupGenerator getGroups = new GroupGenerator();

        int teamCount = ??;
        ArrayList<ArrayList<Team>> expectedResults = new ArrayList<ArrayList<Team>>();
        ArrayList<Team> sourceResults = GetTeams(teamCount, 0).get(ATTENDING);
        for(int i = 0;i < TEST_CASE_COUNT;i++) {
            expectedResults.add(new ArrayList<Team>());
        }
        //define expected results

        System.out.println("Starting Test Case 1:");

        for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {

            System.out.println("	Sub Case " + (testCase + 1) + ":");

            ArrayList<ArrayList<Team>> ladderTestTeamsSeg = GetTeams(teamCount, testCase);
            ArrayList<Team> ladderTestTeams = new ArrayList<Team>();

            //convert ladderTestTeamsSeg to ladderTestTeams

            ArrayList<Group> ladderTestGroups = getGroups.generateMatchGroupings(ladderTestTeams);

            //add win/loss data

            Ladder testLadder = new Ladder(ladderTestTeams);
            testLadder.updateLadder(ladderTestGroups);

            if(expectedResults.get(testCase).equals(testLadder.ladderList)) {

                System.out.println("		Passed");
                successes++;

            }
            else {

                System.out.println("		Failed");

            }

        }

        return successes;
		
	}

	private int TestCase7() { //Case 7: Groups have non-attending teams inside them, groups of 3 only
		
		//here
		
	}

	private int TestCase8() { //Case 8: Groups have non-attending teams inside them, groups of 4 only
		
		//here
		
	}

	private int TestCase9() { //Case 9: Groups have non-attending teams inside them, groups of 3/4, not predictably interchanging (At least one of each)
		
		//here
		
	}

	private int TestCase10() { //Case 10: Single group of 3, no other teams
		
		//here
		
	}

	private int TestCase11() { //Case 11: Single group of 3, other non-attending teams around
		
		//here
		
	}

	private int TestCase12() { //Case 12: Single group of 4, no other teams
		
		//here
		
	}

	private int TestCase13() { //Case 13: Single group of 4, other non-attending teams around
		
		//here
		
	}

	private int TestCase14() { //Case 14: Single group of 3, all other non-attending teams after
		
		//here
		
	}

	private int TestCase15() { //Case 15: Single group of 3, all other non-attending teams before
		
		//here
		
	}

	private int TestCase16() { //Case 16: Single group of 4, all other non-attending teams after
		
		//here
		
	}

	private int TestCase17() { //Case 17: Single group of 4, all other non-attending teams before
		
		//here
		
	}

	private int TestCase18() { //Case 18: Groups have non-attending teams beside and inside them, groups of 3 only, all overlapping
		
		//here
		
	}

	private int TestCase19() { //Case 19: Groups have non-attending teams beside and inside them, groups of 4 only, all overlapping
		
		//here
		
	}

	private int TestCase20() { //Case 20: Groups have non-attending teams beside and inside them, groups of 3/4, all overlapping, not predictably interchanging (At least one of each)
		
		//here
		
	}
	
	//Credits: Got way to get a sublist from http://beginnersbook.com/2013/12/how-to-get-sublist-of-an-arraylist-with-example/
	
}