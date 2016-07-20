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
    private static final TemplateManager template = new TemplateManager();
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
        System.out.println("PDF " + currentFileName + " was created successfully!");
    }

    private void createPdf(ITextRenderer renderer, String fileName) throws IOException, DocumentException {
        if(isDirectoryAvailable()){
            FileOutputStream writerStream = new FileOutputStream(PdfSettings.OUTPUT_PATH + "\\" +  fileName);
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
        renderer.getSharedContext().setReplacedElementFactory(new ElementsFactory(renderer.getSharedContext().getReplacedElementFactory()));
        renderer.layout();
    }

    private void constructColumnContents(Map<String, String> values) throws IOException {
        String columnsContent = columns.getColumns(values);
        values.put("#content", columnsContent);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String date = dateFormat.format(new Date());
        values.put("#dateandtime", date);

        String columnWidth = columns.getColumnsWidth();
        values.put("#columnwidth", columnWidth + "%");
        String pgTotal = getNumberOfPages();
        values.put("#pgtotal", pgTotal);
    }

    private String generateFileName(){
        Long number = new Date().getTime();
        String results = String.valueOf(number);
        return (results + ".pdf");
    }

    private void setPdfContent(Map<String, String> values, List<String> pageContents) {
        String pdfContent = "";
        for(String currentPage : pageContents){
            pdfContent += currentPage;
        }
        values.put("#pdfcontent", pdfContent);
    }

    private List<String> getPages(Map<String, String> values, int numberOfTeams, int teamsPerPage) throws IOException {
        int pgNumber = 1;
        List<String> results = new ArrayList<>();
        for(int counter = 0; counter < numberOfTeams; counter += teamsPerPage){
            values.put("#pgnumber", String.valueOf(pgNumber));
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
