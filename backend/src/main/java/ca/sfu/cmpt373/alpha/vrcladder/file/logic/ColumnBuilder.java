package ca.sfu.cmpt373.alpha.vrcladder.file.logic;

import ca.sfu.cmpt373.alpha.vrcladder.ladder.Ladder;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.TemplateManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class ColumnBuilder {

    private static final TemplateManager template = new TemplateManager();
    private static final int TABLE_WIDTH_IN_PERCENT = 100;
    private static int teamsCounter = 0;

    public String buildColumnsValues(Ladder ladder, Map<String, String> values, String columnsContainer) throws IOException {
        List<Team> teams = ladder.getLadder();
        String results = columnsContainer;
        initiateDefaultTeamsValues(values);
        results = getColumnContents(values, teams, results);
        return results;
    }

    private String getColumnContents(Map<String, String> values, List<Team> teams, String results) throws IOException {
        for(int counter = 0; counter < PdfSettings.NUMBER_OF_COLUMNS; counter++){
            buildColumnTeams(teams, values);
            String content = template.getContents(PdfSettings.COLUMNS_CONTENT_PATH, values);
            results = results.replace("#column" + (counter + 1) + "content", content);
        }
        return results;
    }

    private void buildColumnTeams(List<Team> teams, Map<String, String> values) throws IOException {
        int currentTeam;
        while(teamsCounter < teams.size()){
            if(teamsCounter >= PdfSettings.NUMBER_OF_TEAMS_PER_COLUMN){
                currentTeam = (teamsCounter % PdfSettings.NUMBER_OF_TEAMS_PER_COLUMN) + 1;
            }else{
                currentTeam = teamsCounter + 1;
            }

            String teamName = teams.get(teamsCounter).getFirstPlayer().getDisplayName();
            teamName += "\n" +  teams.get(teamsCounter).getSecondPlayer().getDisplayName();
            values.replace("#teamName", teamName);
            placeTeamIntoHolder(values, currentTeam);

            teamsCounter++;
            if(teamsCounter % PdfSettings.NUMBER_OF_TEAMS_PER_COLUMN == 0){
                break;
            }
        }
        System.out.println("Done iteration");
    }

    private void placeTeamIntoHolder(Map<String, String> values, int currentTeam) throws IOException {
        String content = template.getContents(PdfSettings.COLUMNS_TEAM_HOLDER, values);
        values.replace("#team" + currentTeam, content);
    }

    private void initiateDefaultTeamsValues(Map<String, String> values) {
        values.put("#teamName", "");
        for(int counter = 0; counter < PdfSettings.NUMBER_OF_TEAMS_PER_COLUMN; counter++){
            values.put("#team" + (counter+1), "");
        }
    }

    public String getColumnsWidth(){
        int numberOfSeparators = PdfSettings.NUMBER_OF_COLUMNS - 1;
        int percentageLeft = TABLE_WIDTH_IN_PERCENT - numberOfSeparators;
        int results = (int) Math.floor(percentageLeft / PdfSettings.NUMBER_OF_COLUMNS);
        return String.valueOf(results);
    }

    public String getColumnsContainer(Map<String, String> values) throws IOException {
        String results = "";
        String columnContainerTemplate = template.getContents(PdfSettings.COLUMNS_CONTAINER_PATH, values);
        String columnSeparatorTemplate = template.getContents(PdfSettings.COLUMNS_SEPARATOR_PATH, values);
        for(int counter = 0; counter < PdfSettings.NUMBER_OF_COLUMNS; counter++){
            String currentColumn = columnContainerTemplate.replace("#columncontent", "#column" + (counter + 1) + "content");
            if(counter != (PdfSettings.NUMBER_OF_COLUMNS - 1)){
                currentColumn += columnSeparatorTemplate;
            }
            results += currentColumn;
        }
        return results;
    }

}
