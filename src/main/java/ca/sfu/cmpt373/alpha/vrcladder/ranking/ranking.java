package ca.sfu.cmpt373.alpha.vrcladder.ranking;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;

enum status {
    WIN,
    LOSS
}

/**
 * A class to do all the raking calculation after
 * the finish of a game.
 */
public class Ranking {

//    private static final int NO_SHOW_POINTS = 10;
//    private static final int LOSS_POINTS    = 4;
//    private static final int WIN_POINTS     = 1;
//    private Team team;
//
//
//    public Ranking(Team team, String status) {
//        this.team = team;
//        boolean attendance = verifyAttendance();
//        if(attendance){
//            doCalculation();
//        }
//    }
//
//    private boolean verifyAttendance(){
//        AttendanceStatus teamAttendance = this.team.getAttendace();
//        if(teamAttendance == AttendanceStatus.NO_SHOW){
//            int ranking = this.team.getRanking();
//            ranking = ranking - NO_SHOW_POINTS;
//            updateTeamRanking(ranking);
//            return false;
//        }
//        return true;
//    }
//
//    private void doCalculation(){
//        if(this.team.)
//    }
//
//    private void updateTeamRanking(int ranking){
//        this.team.setRanking(ranking);
//    }

}
