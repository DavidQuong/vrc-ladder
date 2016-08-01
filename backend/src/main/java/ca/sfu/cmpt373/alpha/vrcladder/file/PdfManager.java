package ca.sfu.cmpt373.alpha.vrcladder.file;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.DirectoryNotCreatedException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PdfCouldNotBeCreatedException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.TemplateNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.file.logic.ColumnBuilder;
import ca.sfu.cmpt373.alpha.vrcladder.file.logic.ElementsFactory;
import ca.sfu.cmpt373.alpha.vrcladder.file.logic.PdfSettings;
import ca.sfu.cmpt373.alpha.vrcladder.ladder.Ladder;
import ca.sfu.cmpt373.alpha.vrcladder.util.TemplateManager;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class exports the current ladder into a PDF file. It uses
 * TemplateManager class to convert HTML layout into a PDF file
 * after processing all the data. templates are located in the resources
 * folder.
 */
public class PdfManager {
    private static final String PATH_SEPARATOR    = "/";
    private static final String DAT_EXPRESSION    = "yyyy-MM-dd hh:mm a";
    private static final String PDF_EXTENSION     = ".pdf";
    private static final String CONTENT_TAG       = "#content";
    private static final String DATETIME_TAG      = "#dateandtime";
    private static final String COLUMN_WIDTH_TAG  = "#columnwidth";
    private static final String NUM_OF_PAGES_TAG   = "#pgtotal";
    private static final String PAD_CONTENTS_TAG   = "#pdfcontent";
    private static final String CURRENT_PAGE_TAG   = "#pgnumber";
    private static final TemplateManager template = new TemplateManager();
    private static final String PERCENTAGE_SYMBOL = "%";
    private final ColumnBuilder columns;
    private final Ladder ladder;


    public PdfManager(Ladder ladder){
        this.ladder = ladder;
        columns = new ColumnBuilder(ladder);
    }

    public void exportLadder(){
        ITextRenderer renderer = new ITextRenderer();
        Map<String, String> values = new HashMap<>();
        String currentFileName = generateFileName();
        initializePdfGeneralValues(values);

        try {
            constructPdfContents(renderer, values);
            initializePdfFont(renderer);
            createPdf(renderer, currentFileName);
        } catch (IOException e) {
            throw new TemplateNotFoundException();
        } catch (DocumentException e) {
            throw new PdfCouldNotBeCreatedException();
        }
    }

    private void createPdf(ITextRenderer renderer, String fileName) throws IOException, DocumentException {
        if(isDirectoryAvailable()){
            FileOutputStream writerStream = new FileOutputStream(PdfSettings.OUTPUT_PATH + PATH_SEPARATOR +  fileName);
            renderer.createPDF(writerStream);
            writerStream.flush();
            writerStream.close();
        }else{
            throw new DirectoryNotCreatedException();
        }
    }

    private void constructPdfContents(ITextRenderer renderer, Map<String, String> values) throws IOException {
        int numberOfTeams = ladder.getTeamCount();
        int teamsPerPage = PdfSettings.NUMBER_OF_TEAMS_PER_COLUMN * PdfSettings.NUMBER_OF_COLUMNS;
        List<String> pageContents = getPages(values, numberOfTeams, teamsPerPage);
        setPdfContent(values, pageContents);

        String content = template.getContents(PdfSettings.LAYOUT_PATH, values);
        renderer.setDocumentFromString(content);
        ReplacedElementFactory elementToReplace = renderer.getSharedContext().getReplacedElementFactory();
        ElementsFactory currentElement = new ElementsFactory(elementToReplace);
        renderer.getSharedContext().setReplacedElementFactory(currentElement);
        renderer.layout();
    }

    private void constructColumnContents(Map<String, String> values) throws IOException {
        String columnsContent = columns.getColumns(values);
        values.put(CONTENT_TAG, columnsContent);
    }

    private boolean isDirectoryAvailable() {
        File directory = new File(PdfSettings.OUTPUT_PATH);
        return directory.exists() || directory.mkdir();
    }

    private void initializePdfFont(ITextRenderer renderer) throws IOException, DocumentException {
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont(PdfSettings.FONT_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
    }

    private void initializePdfGeneralValues(Map<String, String> values){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DAT_EXPRESSION);
        String date = dateFormat.format(new Date());
        values.put(DATETIME_TAG, date);

        String columnWidth = columns.getColumnsWidth();
        values.put(COLUMN_WIDTH_TAG, columnWidth + PERCENTAGE_SYMBOL);
        String pgTotal = getNumberOfPages();
        values.put(NUM_OF_PAGES_TAG, pgTotal);
    }

    private String generateFileName(){
        Long number = new Date().getTime();
        String results = String.valueOf(number);
        return (results + PDF_EXTENSION);
    }

    private void setPdfContent(Map<String, String> values, List<String> pageContents) {
        String pdfContent = "";
        for(String currentPage : pageContents){
            pdfContent += currentPage;
        }
        values.put(PAD_CONTENTS_TAG, pdfContent);
    }

    private List<String> getPages(Map<String, String> values, int numberOfTeams, int teamsPerPage) throws IOException {
        int pgNumber = 1;
        List<String> results = new ArrayList<>();
        for(int counter = 0; counter < numberOfTeams; counter += teamsPerPage){
            values.put(CURRENT_PAGE_TAG, String.valueOf(pgNumber));
            constructColumnContents(values);
            String content = template.getContents(PdfSettings.CONTENT_PATH, values);
            results.add(content);
            pgNumber++;
        }
        return results;
    }

    private String getNumberOfPages(){
        int teamsSize = ladder.getTeamCount();
        int teamsPerPage = PdfSettings.NUMBER_OF_TEAMS_PER_COLUMN * PdfSettings.NUMBER_OF_COLUMNS;
        return String.valueOf((int) Math.ceil(teamsSize/(double) teamsPerPage));
    }

}
