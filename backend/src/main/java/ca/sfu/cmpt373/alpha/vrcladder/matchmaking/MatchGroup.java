package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.scores.ScoreCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A class for sorting teams into groups that will play against each other in matches
 * Groups are strictly limited to 3 or 4 teams
 */
@Entity
@Table(name = PersistenceConstants.TABLE_MATCH_GROUP)
public class MatchGroup {

    public static final int MIN_NUM_TEAMS = 3;
    public static final int MAX_NUM_TEAMS = 4;

    private static final String ERROR_MESSAGE_NUM_TEAMS = "Teams list contains more or less than the min or max " +
        "number of permissible teams";

    private IdType id;
    private List<Team> teams;
    private ScoreCard scoreCard;

    private MatchGroup () {
        setTeams(new ArrayList<>());
        init();
    }

    public MatchGroup(Team team1, Team team2, Team team3) {
        this.teams = Arrays.asList(team1, team2, team3);
        init();
    }

    public MatchGroup(Team team1, Team team2, Team team3, Team team4) {
        this.teams = Arrays.asList(team1, team2, team3, team4);
        init();
    }

    /**
     * @throws IllegalStateException if the list contains more or less than the min/max amount of permissible teams
     */
    public MatchGroup(List<Team> teams) {
        if (teams.size() < MIN_NUM_TEAMS || teams.size() > MAX_NUM_TEAMS) {
            throw new IllegalStateException(ERROR_MESSAGE_NUM_TEAMS);
        }
        this.teams = new ArrayList<>(teams);
        init();
    }

    private void init() {
        setId(new IdType());
        scoreCard = new ScoreCard(this);
    }

    @Id
    @Column(name = PersistenceConstants.COLUMN_ID)
    public String getId() {
        return id.getId();
    }

    private void setId(String newId) {
        this.id = new IdType(newId);
    }

    private void setId(IdType id) {
        this.id = id;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    private void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @OneToOne (cascade = CascadeType.ALL)
    public ScoreCard getScoreCard() {
        return this.scoreCard;
    }

    private void setScoreCard(ScoreCard scoreCard) {
        this.scoreCard = scoreCard;
    }

    /**
     * @return the @{@link PlayTime} that the majority of the group teams want to play at.
     * In the case of a tie, it will return the preferred play time of the highest ranked player.
     */
    @Transient
    public PlayTime getPreferredGroupPlayTime() {
        Map<PlayTime, Integer> preferredTimeCounts = new HashMap<>();

        // Initialize counters to zero
        for (Team team : teams) {
            PlayTime playTime = team.getAttendanceCard().getPreferredPlayTime();
            preferredTimeCounts.put(playTime, 0);
        }

        // Count the 'votes' of preferred time slots for each team in a group.
        for (Team team : teams) {
            PlayTime playTime = team.getAttendanceCard().getPreferredPlayTime();
            preferredTimeCounts.replace(playTime, preferredTimeCounts.get(playTime) + 1);
        }

        // Find the time slot with the max votes.
        PlayTime votedPlayTime = null;
        int maxVoteCount = 0;
        boolean tie = false;
        for (PlayTime playTime : preferredTimeCounts.keySet()) {
            int playTimeVotes = preferredTimeCounts.get(playTime);
            if (playTimeVotes == maxVoteCount) {
                tie = true;
            }
            if (playTimeVotes > maxVoteCount) {
                votedPlayTime = playTime;
                maxVoteCount = playTimeVotes;
                tie = false;
            }
        }

        // If there's a tie in votes, choose the highest ranked player's preference.
        if (tie) {
            votedPlayTime = getTeam1().getAttendanceCard().getPreferredPlayTime();
        }

        return votedPlayTime;
    }

    @Transient
    public Team getTeam1(){
        int firstTeamIndex = 0;
        return teams.get(firstTeamIndex);
    }

    @Transient
    public Team getTeam2(){
        int secondTeamIndex = 1;
        return teams.get(secondTeamIndex);
    }

    @Transient
    public Team getTeam3(){
        int thirdTeamIndex = 2;
        return teams.get(thirdTeamIndex);
    }

    @Transient
    public int getTeamCount() {
        return teams.size();
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        MatchGroup otherMatchGroup = (MatchGroup) otherObj;

        return id.equals(otherMatchGroup.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
