package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.CourtsFullException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.UnplayablePlayTimeException;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchScheduler {
    public static void scheduleMatches(List<Court> courts, List<MatchGroup> matchGroups) {
        //TODO: get rid of break statements!
        for (MatchGroup matchGroup : matchGroups) {
            //get the play time that the majority of teams in a group want to play at
            PlayTime preferredPlayTime = getPreferredGroupPlayTime(matchGroup);
            //TODO: delete
            Log.i("" + preferredPlayTime);
            checkPlayablePlayTime(preferredPlayTime);

            //try to schedule a match group at their preferred time
            boolean isPreferredPlayTimeScheduled = false;
            for (Court court : courts) {
                if (court.isPlayTimeFree(preferredPlayTime)) {
                    //TODO: delete
                    Log.i("ideal match scheduled");
                    court.scheduleMatch(matchGroup, preferredPlayTime);
                    isPreferredPlayTimeScheduled = true;
                    break;
                }
            }

            //if no courts have the available preferred play time for a group, choose another play time to schedule them in
            if (!isPreferredPlayTimeScheduled) {
                boolean isAnyPlayTimeScheduled = false;
                for (Court court : courts) {
                    for (PlayTime playTime : PlayTime.values()) {
                        if (playTime.isPlayable() && court.isPlayTimeFree(playTime)) {
                            //TODO: delete
                            Log.i("undesired match scheduled");
                            court.scheduleMatch(matchGroup, playTime);
                            isAnyPlayTimeScheduled = true;
                            break;
                        }
                    }
                    if (isAnyPlayTimeScheduled) {
                        break;
                    }
                }
                if (!isAnyPlayTimeScheduled) {
                    throw new CourtsFullException();
                }
            }
            //TODO: remove
            Log.i("");
        }
    }

    //TODO: move this to the Group class as a method
    private static PlayTime getPreferredGroupPlayTime(MatchGroup matchGroup) {
        List<Team> matchTeams = matchGroup.getTeams();
        Map<PlayTime, Integer> preferredTimeCounts = new HashMap<>();

        //count the 'votes' of preferred time slots for each team in a group
        for (Team team : matchTeams) {
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
            votedPlayTime = matchTeams.get(0).getAttendanceCard().getPreferredPlayTime();
        }

        return votedPlayTime;
    }

    private static void checkPlayablePlayTime(PlayTime playTime) {
        if (!playTime.isPlayable()) {
            throw new UnplayablePlayTimeException(playTime);
        }
    }
}
