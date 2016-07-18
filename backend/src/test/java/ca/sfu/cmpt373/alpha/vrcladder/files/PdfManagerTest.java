package ca.sfu.cmpt373.alpha.vrcladder.files;


import ca.sfu.cmpt373.alpha.vrcladder.file.PdfManager;
import ca.sfu.cmpt373.alpha.vrcladder.ladder.Ladder;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;

import java.util.ArrayList;
import java.util.List;

public class PdfManagerTest {
    public static void main(String[] args){
        List<Team> teams = new ArrayList<>();
        for(int counter = 0; counter < 100; counter++){
            teams.add(MockTeamGenerator.generateTeam());
        }
        Ladder ladder = new Ladder(teams);

        PdfManager pdf = new PdfManager(ladder);
        pdf.exportLadder();

    }
}
