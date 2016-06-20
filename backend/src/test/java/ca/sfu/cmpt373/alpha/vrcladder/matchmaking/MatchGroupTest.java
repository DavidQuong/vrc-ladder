package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import org.junit.Assert;
import org.junit.Test;

public class MatchGroupTest {

	@Test
	public void testAddTeam() {
		Team firstTeam = MockTeamGenerator.generateTeam();
		Team secondTeam = MockTeamGenerator.generateTeam();
		Team thirdTeam = MockTeamGenerator.generateTeam();
		Team fourthTeam = MockTeamGenerator.generateTeam();

		MatchGroup testGroup = new MatchGroup(firstTeam, secondTeam, thirdTeam);
		testGroup.addTeam(fourthTeam);

		MatchGroup expectedResults = new MatchGroup(firstTeam, secondTeam, thirdTeam, fourthTeam);

		Assert.assertEquals(testGroup.getTeams(), expectedResults.getTeams());
	}

	@Test
	public void testRemoveTeam() {
		Team firstTeam = MockTeamGenerator.generateTeam();
		Team secondTeam = MockTeamGenerator.generateTeam();
		Team thirdTeam = MockTeamGenerator.generateTeam();
		Team fourthTeam = MockTeamGenerator.generateTeam();

		MatchGroup testGroup = new MatchGroup(firstTeam, secondTeam, thirdTeam, fourthTeam);
		testGroup.removeTeam(thirdTeam);

		MatchGroup expectedResults = new MatchGroup(firstTeam, secondTeam, fourthTeam);

		Assert.assertEquals(testGroup.getTeams(), expectedResults.getTeams());
	}

	@Test(expected = IllegalStateException.class)
	public void testAddTeamError() {
		Team firstTeam = MockTeamGenerator.generateTeam();
		Team secondTeam = MockTeamGenerator.generateTeam();
		Team thirdTeam = MockTeamGenerator.generateTeam();
		Team fourthTeam = MockTeamGenerator.generateTeam();
		Team fifthTeam = MockTeamGenerator.generateTeam();

		MatchGroup testGroup = new MatchGroup(firstTeam, secondTeam, thirdTeam, fourthTeam);
		testGroup.addTeam(fifthTeam);
	}

	@Test(expected = IllegalStateException.class)
	public void testRemoveTeamError() {
		Team firstTeam = MockTeamGenerator.generateTeam();
		Team secondTeam = MockTeamGenerator.generateTeam();
		Team thirdTeam = MockTeamGenerator.generateTeam();

		MatchGroup testGroup = new MatchGroup(firstTeam, secondTeam, thirdTeam);
		testGroup.removeTeam(thirdTeam);
	}

	@Test
	public void testTradeTeam() {
		Team firstTeam = MockTeamGenerator.generateTeam();
		Team secondTeam = MockTeamGenerator.generateTeam();
		Team thirdTeam = MockTeamGenerator.generateTeam();
		Team fourthTeam = MockTeamGenerator.generateTeam();
		Team fifthTeam = MockTeamGenerator.generateTeam();
		Team sixthTeam = MockTeamGenerator.generateTeam();
		Team seventhTeam = MockTeamGenerator.generateTeam();

		MatchGroup testGroup1 = new MatchGroup(firstTeam, secondTeam, thirdTeam);
		MatchGroup testGroup2 = new MatchGroup(fourthTeam, fifthTeam, sixthTeam, seventhTeam);
		testGroup1.tradeTeams(thirdTeam, testGroup2, fifthTeam);

		MatchGroup expectedResults1 = new MatchGroup(firstTeam, secondTeam, fifthTeam);
		MatchGroup expectedResults2 = new MatchGroup(fourthTeam, sixthTeam, seventhTeam, thirdTeam);

		Assert.assertEquals(testGroup1.getTeams(), expectedResults1.getTeams());
		Assert.assertEquals(testGroup2.getTeams(), expectedResults2.getTeams());
	}

	@Test(expected = TeamNotFoundException.class)
	public void testTradeTeamError() {
		Team firstTeam = MockTeamGenerator.generateTeam();
		Team secondTeam = MockTeamGenerator.generateTeam();
		Team thirdTeam = MockTeamGenerator.generateTeam();
		Team fourthTeam = MockTeamGenerator.generateTeam();
		Team fifthTeam = MockTeamGenerator.generateTeam();
		Team sixthTeam = MockTeamGenerator.generateTeam();
		Team seventhTeam = MockTeamGenerator.generateTeam();

		MatchGroup testGroup1 = new MatchGroup(firstTeam, secondTeam, thirdTeam);
		MatchGroup testGroup2 = new MatchGroup(fourthTeam, fifthTeam, sixthTeam);
		testGroup1.tradeTeams(secondTeam, testGroup2, seventhTeam);
	}
}
