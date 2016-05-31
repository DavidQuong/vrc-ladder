import java.util.ArrayList;

class LadderTest
{
	
	private int ATTENDING = 0;
	private int NOT_ATTENDING = 1;
	
	//List of Lists of Teams, first index is Case, second is ATTENDING or NOT_ATTENDING
	ArrayList<ArrayList<ArrayList<Team>>> TestTeams = new ArrayList<ArrayList<Team>>();
	
	LadderTest()
	{
		
		this.GenTeams();
		
	}
	
	private void GenTeams()
	{
		
		//Case 1: All non-attending teams said so, all attending teams present
		//Case 2: All non-attending teams said so, all attending teams late
		//Case 3: All non-attending teams said attendance, all attending teams late
		//Case 4: All non-attending teams said attendance, all attending teams present
		//Case 5: All attending teams late, others vary
		//Case 6: All attending teams present, others vary
		//Case 7: All non-attending teams said so, others vary
		//Case 8: All non-attending teams said attendance, others vary
		//Case 9: All vary (At least one of each)
		
		TeamGenerator TeamGen = new TeamGenerator();
		
		//Generate the teams according to documentation and store them in TestTeams by class and attendance
		
	}
	
	public boolean RunTests()
	{
		
		boolean Success = true;
		
		Success = TestCase1() ? Success : false;
		Success = TestCase2() ? Success : false;
		Success = TestCase3() ? Success : false;
		Success = TestCase4() ? Success : false;
		Success = TestCase5() ? Success : false;
		Success = TestCase6() ? Success : false;
		Success = TestCase7() ? Success : false;
		Success = TestCase8() ? Success : false;
		Success = TestCase9() ? Success : false;
		Success = TestCase10() ? Success : false;
		Success = TestCase11() ? Success : false;
		Success = TestCase12() ? Success : false;
		Success = TestCase13() ? Success : false;
		Success = TestCase14() ? Success : false;
		Success = TestCase15() ? Success : false;
		Success = TestCase16() ? Success : false;
		Success = TestCase17() ? Success : false;
		Success = TestCase18() ? Success : false;
		Success = TestCase19() ? Success : false;
		Success = TestCase20() ? Success : false;
		
		return Success;
		
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
	
	private boolean TestCase1() //Case 1: Groups are directly beside one another, groups of 3 only
	{
		
		boolean Success = true;
		
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
			
			if(SubSuccess)
			{
				
				System.out.println("		Passed");
				
			}
			else
			{
				
				System.out.println("		Failed");
				Success = false;
				
			}
			
		}
		
		return Success;
		
	}
	
	private boolean TestCase2() //Case 2: Groups are directly beside one another, groups of 4 only
	{
		
		
		
	}
	
	private boolean TestCase3() //Case 3: Groups are directly beside one another, groups of 3/4, not predictably interchanging (At least one of each)
	{
		
		//here
		
	}
	
	private boolean TestCase4() //Case 4: Groups have non-attending teams between them, groups of 3 only
	{
		
		//here
		
	}
	
	private boolean TestCase5() //Case 5: Groups have non-attending teams between them, groups of 4 only
	{
		
		//here
		
	}
	
	private boolean TestCase6() //Case 6: Groups have non-attending teams between them, groups of 3/4, not predictably interchanging (At least one of each)
	{
		
		//here
		
	}
	
	private boolean TestCase7() //Case 7: Groups have non-attending teams inside them, groups of 3 only
	{
		
		//here
		
	}
	
	private boolean TestCase8() //Case 8: Groups have non-attending teams inside them, groups of 4 only
	{
		
		//here
		
	}
	
	private boolean TestCase9() //Case 9: Groups have non-attending teams inside them, groups of 3/4, not predictably interchanging (At least one of each)
	{
		
		//here
		
	}
	
	private boolean TestCase10() //Case 10: Single group of 3, no other teams
	{
		
		//here
		
	}
	
	private boolean TestCase11() //Case 11: Single group of 3, other non-attending teams around
	{
		
		//here
		
	}
	
	private boolean TestCase12() //Case 12: Single group of 4, no other teams
	{
		
		//here
		
	}
	
	private boolean TestCase13() //Case 13: Single group of 4, other non-attending teams around
	{
		
		//here
		
	}
	
	private boolean TestCase14() //Case 14: Single group of 3, all other non-attending teams after
	{
		
		//here
		
	}
	
	private boolean TestCase15() //Case 15: Single group of 3, all other non-attending teams before
	{
		
		//here
		
	}
	
	private boolean TestCase16() //Case 16: Single group of 4, all other non-attending teams after
	{
		
		//here
		
	}
	
	private boolean TestCase17() //Case 17: Single group of 4, all other non-attending teams before
	{
		
		//here
		
	}
	
	private boolean TestCase18() //Case 18: Groups have non-attending teams beside and inside them, groups of 3 only, all overlapping
	{
		
		//here
		
	}
	
	private boolean TestCase19() //Case 19: Groups have non-attending teams beside and inside them, groups of 4 only, all overlapping
	{
		
		//here
		
	}
	
	private boolean TestCase20() //Case 20: Groups have non-attending teams beside and inside them, groups of 3/4, all overlapping, not predictably interchanging (At least one of each)
	{
		
		//here
		
	}
	
	//Credits: Got way to get a sublist from http://beginnersbook.com/2013/12/how-to-get-sublist-of-an-arraylist-with-example/
	
}