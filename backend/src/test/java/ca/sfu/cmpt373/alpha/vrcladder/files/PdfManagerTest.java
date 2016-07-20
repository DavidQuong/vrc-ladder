package ca.sfu.cmpt373.alpha.vrcladder.files;


import ca.sfu.cmpt373.alpha.vrcladder.file.PdfManager;
import ca.sfu.cmpt373.alpha.vrcladder.ladder.Ladder;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;

import java.util.ArrayList;
import java.util.List;

public class PdfManagerTest {
    public static void main(String[] args){
        List<Team> teams = new ArrayList<>();
        for(int counter = 0; counter < 33; counter++){
            teams.add(MockTeamGenerator.generateTeam());
        }
        Ladder ladder = new Ladder(teams);
        Team firstTeam = ladder.getLadder().get(0);
        firstTeam.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);

        PdfManager pdf = new PdfManager(ladder);
        pdf.exportLadder();
    }
}
