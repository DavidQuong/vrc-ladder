package ca.sfu.cmpt373.alpha.vrcladder.file.logic;

import ca.sfu.cmpt373.alpha.vrcladder.util.TemplateManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ColumnBuilder {

    private static final TemplateManager template = new TemplateManager();

    public String buildColumnsValues(Map<String, String> values) throws IOException {
        List<String> results = new ArrayList<>();
        String content = template.getContents(PdfSettings.COLUMNS_PATH, values);
        for(int counter = 0; counter < PdfSettings.NUMBER_OF_COLUMNS; counter++){
            values.put("#column" + (counter + 1) + "content", content);
        }
        return content;
    }

    private void buildColumnContent(){

    }
}
