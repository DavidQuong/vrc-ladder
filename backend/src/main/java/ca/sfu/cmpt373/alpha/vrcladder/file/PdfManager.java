package ca.sfu.cmpt373.alpha.vrcladder.file;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.DirectoryNotCreatedException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PdfCouldNotBeCreatedException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.TemplateNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.file.logic.ColumnBuilder;
import ca.sfu.cmpt373.alpha.vrcladder.file.logic.ElementsFactory;
import ca.sfu.cmpt373.alpha.vrcladder.file.logic.PdfSettings;
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

    public void exportLadder(){
        ColumnBuilder columns = new ColumnBuilder();
        ITextRenderer renderer = new ITextRenderer();
        Map<String, String> values = new HashMap<>();
        String currentFileName = getFileName();
        setDateValue(values);

        String pdfContents = "";
        try {
            String columnContents = columns.buildColumnsValues(values);
            buildContents(renderer, values, pdfContents);
            buildPdfFonts(renderer);
            exportPdf(renderer, currentFileName);
        } catch (IOException e) {
            throw new TemplateNotFoundException();
        } catch (DocumentException e) {
            throw new PdfCouldNotBeCreatedException();
        }
        System.out.println("PDF " + currentFileName + " was created successfully!");
    }

    private void setDateValue(Map<String, String> values){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String date = dateFormat.format(new Date());
        values.put("#dateandtime", date);
    }

    private void buildContents(ITextRenderer renderer, Map<String, String> values, String content) throws IOException {
        content = template.getContents(PdfSettings.LAYOUT_PATH, values);
        renderer.setDocumentFromString(content);
        renderer.getSharedContext().setReplacedElementFactory(new ElementsFactory(renderer.getSharedContext().getReplacedElementFactory()));
    }

    private void buildPdfFonts(ITextRenderer renderer) throws IOException, DocumentException {
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont(PdfSettings.FONT_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
    }

    private void exportPdf(ITextRenderer renderer, String fileName) throws IOException, DocumentException {
        if(fixDirectoryPath()){
            FileOutputStream writerStream = new FileOutputStream(PdfSettings.OUTPUT_PATH + "\\" +  fileName);
            renderer.layout();
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

    private boolean fixDirectoryPath(){
        File directory = new File(PdfSettings.OUTPUT_PATH);
        if(!directory.exists()){
            return directory.mkdir();
        }
        return true;
    }
}
