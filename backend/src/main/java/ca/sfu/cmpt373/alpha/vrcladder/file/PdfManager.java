package ca.sfu.cmpt373.alpha.vrcladder.file;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.DirectoryNotCreatedException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PdfCouldNotBeCreatedException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.TemplateNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.file.logic.ColumnBuilder;
import ca.sfu.cmpt373.alpha.vrcladder.file.logic.ElementsFactory;
import ca.sfu.cmpt373.alpha.vrcladder.file.logic.PdfSettings;
import ca.sfu.cmpt373.alpha.vrcladder.ladder.Ladder;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.TemplateManager;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * This class exports the current ladder into a PDF file. It uses
 * TemplateManager class to convert HTML layout into a PDF file
 * after processing all the data. templates are located in the resources
 * folder.
 */
public class PdfManager {
    private static final TemplateManager template = new TemplateManager();
    private static final ColumnBuilder columns = new ColumnBuilder();
    private final Ladder ladder;


    public PdfManager(Ladder ladder){
        this.ladder = ladder;
    }

    public void exportLadder(){
        ITextRenderer renderer = new ITextRenderer();
        Map<String, String> values = new HashMap<>();
        String currentFileName = getFileName();
        List<Team> teams = ladder.getLadder();
        setPdfValues(values, teams.size());

        try {
            buildContents(renderer, values);
            buildPdfFonts(renderer);
            exportPdf(renderer, currentFileName);
        } catch (IOException e) {
            throw new TemplateNotFoundException();
        } catch (DocumentException e) {
            throw new PdfCouldNotBeCreatedException();
        }
        System.out.println("PDF " + currentFileName + " was created successfully!");
    }

    private void buildColumns(Map<String, String> values) throws IOException {
        String columnsContainer = columns.getColumnsContainer(values);
        columnsContainer = columns.buildColumnsValues(this.ladder, values, columnsContainer);
        values.put("#content", columnsContainer);
    }

    private void setPdfValues(Map<String, String> values, int teamsSize){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String date = dateFormat.format(new Date());
        values.put("#dateandtime", date);

        String columnWidth = columns.getColumnsWidth();
        values.put("#columnwidth", columnWidth + "%");
        String pgTotal = getNumberOfPages(teamsSize);
        values.put("#pgtotal", pgTotal);
    }

    private String getNumberOfPages(int teamsSize){
        int teamsPerPage = PdfSettings.NUMBER_OF_TEAMS_PER_COLUMN * PdfSettings.NUMBER_OF_COLUMNS;
        String pgTotal = String.valueOf((int) Math.ceil(teamsSize/(double) teamsPerPage));
        return pgTotal;
    }

    private void buildContents(ITextRenderer renderer, Map<String, String> values) throws IOException {
        int numberOfTeams = ladder.getTeamCount();
        int teamsPerPage = PdfSettings.NUMBER_OF_TEAMS_PER_COLUMN * PdfSettings.NUMBER_OF_COLUMNS;
        List<String> pageContents = new ArrayList<>();

        int pgNumber = 1;
        for(int counter = 0; counter < numberOfTeams; counter += teamsPerPage){
            values.put("#pgnumber", String.valueOf(pgNumber));
            buildColumns(values);
            String content = template.getContents(PdfSettings.CONTENT_PATH, values);
            pageContents.add(content);
            pgNumber++;
        }

        String pdfContent = "";
        for(String currentPage : pageContents){
            pdfContent += currentPage;
        }

        System.out.println(pdfContent);
        values.put("#pdfcontent", pdfContent);
        String content = template.getContents(PdfSettings.LAYOUT_PATH, values);

        renderer.setDocumentFromString(content);
        renderer.getSharedContext().setReplacedElementFactory(new ElementsFactory(renderer.getSharedContext().getReplacedElementFactory()));
        renderer.layout();
    }

    private void buildPdfFonts(ITextRenderer renderer) throws IOException, DocumentException {
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont(PdfSettings.FONT_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
    }

    private void exportPdf(ITextRenderer renderer, String fileName) throws IOException, DocumentException {
        if(fixDirectoryPath()){
            FileOutputStream writerStream = new FileOutputStream(PdfSettings.OUTPUT_PATH + "\\" +  fileName);
            renderer.createPDF(writerStream);
            writerStream.flush();
            writerStream.close();
        }else{
            throw new DirectoryNotCreatedException();
        }
    }

    private String getFileName(){
        Long number = new Date().getTime();
        String results = String.valueOf(number);
        return (results + ".pdf");
    }

    private boolean fixDirectoryPath() {
        File directory = new File(PdfSettings.OUTPUT_PATH);
        return directory.exists() || directory.mkdir();
    }
}
