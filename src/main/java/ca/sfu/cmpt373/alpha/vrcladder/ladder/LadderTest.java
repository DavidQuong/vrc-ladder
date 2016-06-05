package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import java.util.ArrayList;

class LadderTest
{
	
	private int ATTENDING = 0;
	private int NOT_ATTENDING = 1;
	private boolean initialized = false;
	
	//List of Lists of Teams, first index is Case, second is ATTENDING or NOT_ATTENDING
	ArrayList<ArrayList<ArrayList<Team>>> TestTeams;
	
	LadderTest()
	{
		
		this.GenTeams();
		
	}
	
	private void GenTeams()
	{
		
		//Case 0: All non-attending teams said so,         all attending teams present
		//Case 1: All non-attending teams said so,         all attending teams late
		//Case 2: All non-attending teams said attendance, all attending teams late
		//Case 3: All non-attending teams said attendance, all attending teams present
		//Case 4: Others vary,                             all attending teams late, 
		//Case 5: Others vary,                             all attending teams present
		//Case 6: All non-attending teams said so,         others vary
		//Case 7: All non-attending teams said attendance, others vary
		//Case 8: All vary

		if(!this.initialized)
		{
		
			TeamGenerator TeamGen = new TeamGenerator();
			Random randomInt = new Random();
		
			TestTeams = new ArrayList<ArrayList<ArrayList<Team>>>();

			for(int case = 0;case < 9;case++) //Adding cases
			{

				TestTeams.add(new ArrayList<ArrayList<Team>>());

				for(int attendance = 0;attendance < 2;attendance++) //Adding ATTENDING/NOT_ATTENDING
				{

					TestTeams.get(case).add(new ArrayList<Team>());

					for(int k = 0;k < 25;k++)
					{

						Team creation = TeamGen.generateTeam();

						if(attendance == ATTENDING)
						{

							if(case == 0 || case == 3 || case == 5)
							{

								creation.setAttendance(PRESENT);

							}
							else if(case == 1 || case == 2 || case == 4)
							{

								creation.setAttendance(LATE);

							}
							else if(randomInt.nextInt(100) < 50)
							{

								creation.setAttendance(PRESENT);

							}
							else
							{

								creation.setAttendance(LATE);

							}

						}
						else //NOT_ATTENDING
						{

							if(case == 0 || case == 1 || case == 6)
							{

								creation.setAttendance(ABSENT);

							}
							else if(case == 2 || case == 3 || case == 7)
							{

								creation.setAttendance(NO_SHOW);

							}
							else if(randomInt.nextInt(100) < 50)
							{

								creation.setAttendance(ABSENT);

							}
							else
							{

								creation.setAttendance(NO_SHOW);

							}

						}

						TestTeams.get(case).get(attendance).add(creation);

					}

				}

			}

		}

		this.initialized = true;
		
	}
	
	public boolean RunTests()
	{
		
		int Successes = 0;
		int MiniSuccesses = 0;
		int TestResults = 0;

		TestResults = TestCase1();
		if(TestResults == 9)
		{

			System.out.println("Test Case 1 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 1 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;
		
		TestResults = TestCase2();
		if(TestResults == 9)
		{

			System.out.println("Test Case 2 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 2 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase3();
		if(TestResults == 9)
		{

			System.out.println("Test Case 3 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 3 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase4();
		if(TestResults == 9)
		{

			System.out.println("Test Case 4 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 4 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase5();
		if(TestResults == 9)
		{

			System.out.println("Test Case 5 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 5 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase6();
		if(TestResults == 9)
		{

			System.out.println("Test Case 6 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 6 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase7();
		if(TestResults == 9)
		{

			System.out.println("Test Case 7 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 7 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase8();
		if(TestResults == 9)
		{

			System.out.println("Test Case 8 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 8 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase9();
		if(TestResults == 9)
		{

			System.out.println("Test Case 9 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 9 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase10();
		if(TestResults == 9)
		{

			System.out.println("Test Case 10 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 10 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase11();
		if(TestResults == 9)
		{

			System.out.println("Test Case 11 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 11 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;
		
		TestResults = TestCase12();
		if(TestResults == 9)
		{

			System.out.println("Test Case 12 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 12 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase13();
		if(TestResults == 9)
		{

			System.out.println("Test Case 13 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 13 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase14();
		if(TestResults == 9)
		{

			System.out.println("Test Case 14 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 14 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase15();
		if(TestResults == 9)
		{

			System.out.println("Test Case 15 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 15 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase16();
		if(TestResults == 9)
		{

			System.out.println("Test Case 16 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 16 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase17();
		if(TestResults == 9)
		{

			System.out.println("Test Case 17 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 17 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase18();
		if(TestResults == 9)
		{

			System.out.println("Test Case 18 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 18 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase19();
		if(TestResults == 9)
		{

			System.out.println("Test Case 19 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 19 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;

		TestResults = TestCase20();
		if(TestResults == 9)
		{

			System.out.println("Test Case 20 Passed, 9/9\n");
			Successes++;

		}
		else
		{

			System.out.println("Test Case 20 Failed, " + TestResults + "/9\n");

		}
		MiniSuccesses += TestResults;
		
		if(Successes == 20)
		{

			return true;

		}

		return false;
		
	}
	
	//First index is ATTENDING or NOT_ATTENDING
	private ArrayList<ArrayList<Team>> GetTeams(int Amount, int Case)
	{
		
		ArrayList<Team> CasedList = TestTeams.get(Case);
		
		if(Amount >= CasedList.size())
		{
			
			throw new RuntimeException("Bad amount in GetTeams (" + Amount + " >= " + CasedList.size() + ")") ;
			
		}
		if(Amount * 2 >= CasedList.size())
		{
		
			return new ArrayList<Team>(CasedList.subList(Amount, CasedList.size())).addAll(new ArrayList<Team>(CasedList.subList(0, (Amount * 2) - CasedList.size())));
					
		}
		else
		{
			
			return new ArrayList<Team>(CasedList.subList(Amount, Amount * 2));
			
		}
		
	}
	
	private int TestCase1() //Case 1: Groups are directly beside one another, groups of 3 only
	{
		
		int Successes = 0;
		
		System.out.println("Starting Test Case 1:");
		
		for(int Case = 0;Case < 9;Case++)
		{
			
			boolean SubSuccess = true;
			
			System.out.println("	Sub Case " + (Case + 1) + ":");
			
			ArrayList<ArrayList<Team>> TheseTeams = GetTeams(<amount>, Case);
			ArrayList<Team> LadderTestTeams = new ArrayList<Team>();
			
			//Manipulate teams
			
			Ladder TestLadder = new Ladder(LadderTestTeams);
			TestLadder.<somemethod>();

			//check result
			
			if(SubSuccess)
			{
				
				System.out.println("		Passed");
				Successes++;
				
			}
			else
			{
				
				System.out.println("		Failed");
				
			}
			
		}
		
		return Successes;
		
	}
	/*
	private int TestCase2() //Case 2: Groups are directly beside one another, groups of 4 only
	{
		
		
		
	}
	
	private int TestCase3() //Case 3: Groups are directly beside one another, groups of 3/4, not predictably interchanging (At least one of each)
	{
		
		//here
		
	}
	
	private int TestCase4() //Case 4: Groups have non-attending teams between them, groups of 3 only
	{
		
		//here
		
	}
	
	private int TestCase5() //Case 5: Groups have non-attending teams between them, groups of 4 only
	{
		
		//here
		
	}
	
	private int TestCase6() //Case 6: Groups have non-attending teams between them, groups of 3/4, not predictably interchanging (At least one of each)
	{
		
		//here
		
	}
	
	private int TestCase7() //Case 7: Groups have non-attending teams inside them, groups of 3 only
	{
		
		//here
		
	}
	
	private int TestCase8() //Case 8: Groups have non-attending teams inside them, groups of 4 only
	{
		
		//here
		
	}
	
	private int TestCase9() //Case 9: Groups have non-attending teams inside them, groups of 3/4, not predictably interchanging (At least one of each)
	{
		
		//here
		
	}
	
	private int TestCase10() //Case 10: Single group of 3, no other teams
	{
		
		//here
		
	}
	
	private int TestCase11() //Case 11: Single group of 3, other non-attending teams around
	{
		
		//here
		
	}
	
	private int TestCase12() //Case 12: Single group of 4, no other teams
	{
		
		//here
		
	}
	
	private int TestCase13() //Case 13: Single group of 4, other non-attending teams around
	{
		
		//here
		
	}
	
	private int TestCase14() //Case 14: Single group of 3, all other non-attending teams after
	{
		
		//here
		
	}
	
	private int TestCase15() //Case 15: Single group of 3, all other non-attending teams before
	{
		
		//here
		
	}
	
	private int TestCase16() //Case 16: Single group of 4, all other non-attending teams after
	{
		
		//here
		
	}
	
	private int TestCase17() //Case 17: Single group of 4, all other non-attending teams before
	{
		
		//here
		
	}
	
	private int TestCase18() //Case 18: Groups have non-attending teams beside and inside them, groups of 3 only, all overlapping
	{
		
		//here
		
	}
	
	private int TestCase19() //Case 19: Groups have non-attending teams beside and inside them, groups of 4 only, all overlapping
	{
		
		//here
		
	}
	
	private int TestCase20() //Case 20: Groups have non-attending teams beside and inside them, groups of 3/4, all overlapping, not predictably interchanging (At least one of each)
	{
		
		//here
		
	}
	*/
	//Credits: Got way to get a sublist from http://beginnersbook.com/2013/12/how-to-get-sublist-of-an-arraylist-with-example/
	
}