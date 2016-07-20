package ca.sfu.cmpt373.alpha.vrcladder.file.logic;

import ca.sfu.cmpt373.alpha.vrcladder.ladder.Ladder;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.TemplateManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class ColumnBuilder {
    private static final int TABLE_WIDTH_IN_PERCENT   = 100;
    private static final String COLUMN_CONTENT_TAG    = "#columncontent";
    private static final String TEAM_NAME_TAG         = "#teamName";
    private static final String ATTENDING_STATUS_TAG  = "#attendingStatus";
    private static final String TEAM_ATTENDING        = "attendingTeam";
    private static final String TEAM_NOT_ATTENDING    = "notAttendingTeam";
    private static final String COLUMN_REPLACER_LEFT  = "#column";
    private static final String COLUMN_REPLACER_RIGHT = "content";
    private static final String TEAM_TAG_INDICATOR    = "#team";
    private static int teamsCounter                   = 0;
    private static final TemplateManager template     = new TemplateManager();
    private final Ladder ladder;

    public ColumnBuilder(Ladder ladder){
        this.ladder = ladder;
    }

    public String getColumns(Map<String, String> values) throws IOException {
        String columns = generateColumns(values);
        initiateDefaultTeamsValues(values);
        return getColumnContents(values, columns);
    }

    public String getColumnsWidth(){
        int numberOfSeparators = PdfSettings.NUMBER_OF_COLUMNS - 1;
        int percentageLeft = TABLE_WIDTH_IN_PERCENT - numberOfSeparators;
        int results = (int) Math.floor(percentageLeft / PdfSettings.NUMBER_OF_COLUMNS);
        return String.valueOf(results);
    }

    private String generateColumns(Map<String, String> values) throws IOException {
        String results = "";
        String columnContainerTemplate = template.getContents(PdfSettings.COLUMNS_CONTAINER_PATH, values);
        String columnSeparatorTemplate = template.getContents(PdfSettings.COLUMNS_SEPARATOR_PATH, values);
        for(int counter = 0; counter < PdfSettings.NUMBER_OF_COLUMNS; counter++){
            String currentColumn = columnContainerTemplate.replace(COLUMN_CONTENT_TAG,
                                    COLUMN_REPLACER_LEFT + (counter + 1) + COLUMN_REPLACER_RIGHT);
            if(counter != (PdfSettings.NUMBER_OF_COLUMNS - 1)){
                currentColumn += columnSeparatorTemplate;
            }
            results += currentColumn;
        }
        return results;
    }

    private void constructColumnTeams(Map<String, String> values) throws IOException {
        List<Team> teams = ladder.getLadder();
        int currentTeam;
        while(teamsCounter < teams.size()){
            if(teamsCounter >= PdfSettings.NUMBER_OF_TEAMS_PER_COLUMN){
                currentTeam = (teamsCounter % PdfSettings.NUMBER_OF_TEAMS_PER_COLUMN) + 1;
            }else{
                currentTeam = teamsCounter + 1;
            }

            setAttendingStatus(values, teams.get(teamsCounter));
            String teamName = teams.get(teamsCounter).getFirstPlayer().getDisplayName();
            teamName += "\n" +  teams.get(teamsCounter).getSecondPlayer().getDisplayName();
            values.replace(TEAM_NAME_TAG, teamName);
            setCurrentTeam(values, currentTeam);

            teamsCounter++;
            if(teamsCounter % PdfSettings.NUMBER_OF_TEAMS_PER_COLUMN == 0){
                break;
            }
        }
    }

    private void initiateDefaultTeamsValues(Map<String, String> values) {
        values.put(TEAM_NAME_TAG, "");
        values.put(ATTENDING_STATUS_TAG, "");
        for(int counter = 0; counter < PdfSettings.NUMBER_OF_TEAMS_PER_COLUMN; counter++){
            values.put(TEAM_TAG_INDICATOR + (counter+1), "");
        }
    }

    private void setAttendingStatus(Map<String, String> values, Team team) {
        boolean isAttending = team.getAttendanceCard().isAttending();
        if(isAttending){
            values.replace(ATTENDING_STATUS_TAG, TEAM_ATTENDING);
        }else{
            values.replace(ATTENDING_STATUS_TAG , TEAM_NOT_ATTENDING);
        }
    }

    private void setCurrentTeam(Map<String, String> values, int currentTeam) throws IOException {
        String content = template.getContents(PdfSettings.COLUMNS_TEAM_HOLDER, values);
        values.replace(TEAM_TAG_INDICATOR + currentTeam, content);
    }

    private String getColumnContents(Map<String, String> values, String results) throws IOException {
        for(int counter = 0; counter < PdfSettings.NUMBER_OF_COLUMNS; counter++){
            initiateDefaultTeamsValues(values);
            constructColumnTeams(values);
            String content = template.getContents(PdfSettings.COLUMNS_CONTENT_PATH, values);
            results = results.replace(COLUMN_REPLACER_LEFT + (counter + 1) + COLUMN_REPLACER_RIGHT, content);
        }
        return results;
    }
}