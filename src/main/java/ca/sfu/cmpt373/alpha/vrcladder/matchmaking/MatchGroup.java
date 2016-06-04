package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import javax.persistence.*;
import java.util.*;

/**
 * A class for sorting teams into groups that will play against each other in matches
 * Groups are strictly limited to 3 or 4 teams
 */
@Entity
@Table (name = PersistenceConstants.TABLE_MATCH_GROUP)
public class MatchGroup {
    public static final int MIN_NUM_TEAMS = 3;
    public static final int MAX_NUM_TEAMS = 4;

    private static final String ERROR_MESSAGE_NUM_TEAMS = "Teams list contains more or less than the min or max number of permissible teams";

    private IdType id;
    private List<Team> teams;

    public MatchGroup () {
        //required by Hibernate
    }

    public MatchGroup(Team team1, Team team2, Team team3) {
        this.teams = Arrays.asList(team1, team2, team3);
        initialize();
    }

    public MatchGroup(Team team1, Team team2, Team team3, Team team4) {
        Arrays.asList(team1, team2, team3, team4);
        initialize();
    }

    /**
     * @throws IllegalStateException if the list contains more or less than the min/max amount of permissible teams
     */
    public MatchGroup(List<Team> teams) {
        if (teams.size() < MIN_NUM_TEAMS || teams.size() > MAX_NUM_TEAMS) {
            throw new IllegalStateException(ERROR_MESSAGE_NUM_TEAMS);
        }
        this.teams = new ArrayList<>(teams);
        initialize();
    }

    //for common constructor initialization
    private void initialize() {
        setId(new IdType());
    }

    @Id
    @Column(name = PersistenceConstants.COLUMN_ID)
    public String getId() {
        return id.getId();
    }

    private void setId(String newId) {
        this.id = new IdType(newId);
    }

    //TODO: research how to avoid requiring private setters
    //private setters required by hibernate
    private void setId(IdType id) {
        this.id = id;
    }

    @OneToMany(cascade = CascadeType.ALL)
    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    private void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    /**
     * @return the @{@link PlayTime} that the majority of the group teams want to play at.
     * In the case of a tie, it will return the preferred play time of the highest ranked player.
     */
    @Transient
    public PlayTime getPreferredGroupPlayTime() {
        Map<PlayTime, Integer> preferredTimeCounts = new HashMap<>();

        //count the 'votes' of preferred time slots for each team in a group
        for (Team team : teams) {
            PlayTime playTime = team.getAttendanceCard().getPreferredPlayTime();
            if (preferredTimeCounts.containsKey(playTime)) {
                preferredTimeCounts.replace(playTime, preferredTimeCounts.get(playTime) + 1);
            } else {
                preferredTimeCounts.put(playTime, 1);
            }
        }

        //find the time slot with the max votes
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

        //if there's a tie in votes, choose the highest ranked player's preference
        if (tie) {
            votedPlayTime = teams.get(0).getAttendanceCard().getPreferredPlayTime();
        }

        return votedPlayTime;
    }
}
