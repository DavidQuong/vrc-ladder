package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import java.util.*;


public class Ladder {
    private List<Team> ladder;
    private List <Team> attendingLadder;
    final static private int ATTENDANCE_PENALTY = 2;
    final static private int LATE_PENALTY =4;
    final static private int NO_SHOW_PENALTY =10;

    public Ladder() {
        ladder = new ArrayList<>();
    }

    public Ladder(List<Team> newLadder) {
        ladder = newLadder;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");

        for (int i = 0; i < ladder.size(); i++) {
            stringBuilder.append(i + " :");
            stringBuilder.append(ladder.get(i).toString() + "\n ");
        }

        return stringBuilder.toString();
    }

    public List<Team> getLadder(){return ladder;}
    public Team findTeamAtPosition(int teamPosition) {
        return ladder.get(teamPosition);
    }

    public int findTeamPosition(Team team) throws NoSuchElementException {
        int teamPosition = ladder.indexOf(team);
        if (teamPosition == -1) {
            throw new NoSuchElementException();
        }
        return teamPosition;
    }



    public void addTeam(Team team) {
        ladder.add(team);
    }

    public void insertTeamAtPosition(int position, Team team) {
        ladder.add(position, team);

    }



    public int getLadderTeamCount() {
        return ladder.size();
    }

    public void swapTeams(Team team1, Team team2) {
        int team1Position;
        int team2Position;
        try {
            team1Position = findTeamPosition(team1);
            team2Position = findTeamPosition(team2);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        swapTeams(team1Position, team2Position);
    }

    public void swapTeams(int team1Position, int team2Position) {
        if (!verifyPositions(team1Position) || !verifyPositions(team2Position)) {
            return;
        }
        Collections.swap(ladder, team1Position, team2Position);

    }

    private boolean verifyPositions(int position) {
        return (position < 0 || position >= ladder.size());
    }


    //swap teams in a match based on rank
    public void updateLadder(MatchGroup[] matchGroup){
        setupAttendance();
        arrangeMatchResults(matchGroup[0]);

        for (int matchIndex =1; matchIndex<matchGroup.length; matchIndex++){
            arrangeMatchResults(matchGroup[matchIndex]);
        }
        //switch highest team of match with lowest team of previous match
        for (int matchIndex =0; matchIndex<matchGroup.length; matchIndex++) {
            swapBetweenMatchGroup(matchGroup, matchIndex);
        }
        applyAttendancePenalty();
        applyNoShowPenalty();
        applyLatePenalty();
    }
    private void setupAttendance(){
        attendingLadder.clear();
        int nullLocation = 0;
        for (Team team: ladder){

            if(!team.getAttendanceCard().late()){
                nullLocation = ladder.indexOf(team);
                attendingLadder.add(team);
                ladder.add(nullLocation, null);
                nullLocation++;//increment to remove the
                ladder.remove(nullLocation);
            }

        }
    }

    private void combineLadders() {
        int i = 0;
        for (Team teams: ladder ){
            if(teams==null){
                ladder.add(i, teams);
                ladder.remove(i+1);
            }
            i++;

        }

    }
    private void applyLatePenalty() {
        int ladderSize;
        ladderSize = this.getLadderTeamCount();
        for (int k =0;k<ladderSize; k++){
            if(ladder.get(k).getAttendanceCard().late()) {
                Team tempTeam = ladder.get(k);
                ladder.remove(k);
                if (k < ladderSize - LATE_PENALTY){
                    ladder.add(k + LATE_PENALTY, tempTeam);
                } else {
                    ladder.add(tempTeam);
                }
            }

        }
    }

    private void applyNoShowPenalty() {
        int ladderSize;
        ladderSize = this.getLadderTeamCount();
        for (int k =0;k<ladderSize; k++){
            if(ladder.get(k).getAttendanceCard().noShow()){
                Team tempTeam = ladder.get(k);
                ladder.remove(k);
                if (k < ladderSize - NO_SHOW_PENALTY){
                    ladder.add(k+NO_SHOW_PENALTY, tempTeam);
                } else {
                    ladder.add(tempTeam);
                }
            }

        }
    }

    private void applyAttendancePenalty() {
        int ladderSize = this.getLadderTeamCount();
        for (int k =0;k<ladderSize; k++){
            if(!ladder.get(k).getAttendanceCard().isAttending()){
                Team tempTeam = ladder.get(k);
                ladder.remove(k);
                if (k < ladderSize - ATTENDANCE_PENALTY){
                ladder.add(k+ATTENDANCE_PENALTY, tempTeam);
                } else {
                    ladder.add(tempTeam);
                }
                
            }

        }
    }

    private void swapBetweenMatchGroup(MatchGroup[] matchGroups, int index) {
        if (matchGroups.length >= index||index==0){
            return;
        }

        List<Team> teamOrder1 = matchGroups[index].getPlacement();
        List<Team> teamOrder2 = matchGroups[index - 1].getPlacement();

        if (teamOrder1.size() > 3){
            swapTeams(teamOrder1.get(3), teamOrder2.get(0));
        }
        else
            swapTeams(teamOrder1.get(2), teamOrder2.get(0));

    }


    private void arrangeMatchResults(MatchGroup match){
        if (match.getTeams().size()==match.MIN_NUM_TEAMS){
            arrange3Teams(match);
        }
        else if(match.getTeams().size()==match.MAX_NUM_TEAMS){
            arrange4Teams(match);
        }

    }

    private void arrange4Teams(MatchGroup match){
            List<Team> teamOrder = match.getPlacement();
            int matchSize = teamOrder.size();

            int[] teamPos={
                    findTeamPosition(teamOrder.get(0)),
                    findTeamPosition(teamOrder.get(1)),
                    findTeamPosition(teamOrder.get(2)),
                    findTeamPosition(teamOrder.get(3))
            };
            Arrays.sort(teamPos);
            //inserts the four teams in their new positions
            for (int i=0;i<matchSize;i++) {
                ladder.add(teamPos[i], teamOrder.get(i));
                ladder.remove(teamPos[i+1]);
            }


    }

    private void arrange3Teams(MatchGroup match){
        List<Team> TeamOrder = match.getPlacement();
        int matchSize = TeamOrder.size();

            int[] teamPos = {
                    findTeamPosition(TeamOrder.get(0)),
                    findTeamPosition(TeamOrder.get(1)),
                    findTeamPosition(TeamOrder.get(2))
            };
        //sort the teamPos array
            Arrays.sort(teamPos);
        //insert the teams at their new positions
            for (int i=0;i<matchSize;i++) {
                ladder.add(teamPos[i], TeamOrder.get(i));
                ladder.remove(teamPos[i+1]);
            }
        }



}
