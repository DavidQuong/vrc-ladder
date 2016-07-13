package ca.sfu.cmpt373.alpha.vrcladder.file;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.DirectoryNotCreatedException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PdfCoundNotBeCreatedException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.TemplateNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.file.logic.ElementsFactory;
import ca.sfu.cmpt373.alpha.vrcladder.template.TemplateManager;
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
    private static final String LAYOUT_PATH = "src\\main\\resources\\pdf\\templates\\layout.html";
    private static final String FONT_PATH   = "src\\main\\resources\\pdf\\fonts\\Lato-Medium.ttf";
    private static final String OUTPUT_PATH = "PDF";

    public void exportLadder(){
        ITextRenderer renderer = new ITextRenderer();
        Map<String, String> values = new HashMap<>();
        String currentFileName = getFileName();
        setDateValue(values);

        String pdfContents = "";
        try {
            buildContents(renderer, values, pdfContents);
            buildPdfFonts(renderer);
            exportPdf(renderer, currentFileName);
        } catch (IOException e) {
            throw new TemplateNotFoundException();
        } catch (DocumentException e) {
            throw new PdfCoundNotBeCreatedException();
        }
        System.out.println("PDF " + currentFileName + " was created successfully!");
    }

    private void setDateValue(Map<String, String> values){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String date = dateFormat.format(new Date());
        values.put("#dateandtime", date);
    }

    private void buildContents(ITextRenderer renderer, Map<String, String> values, String content) throws IOException {
        content = template.getContents(LAYOUT_PATH, values);
        renderer.setDocumentFromString(content);
        renderer.getSharedContext().setReplacedElementFactory(new ElementsFactory(renderer.getSharedContext().getReplacedElementFactory()));
    }

    private void buildPdfFonts(ITextRenderer renderer) throws IOException, DocumentException {
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
    }

    private void exportPdf(ITextRenderer renderer, String fileName) throws IOException, DocumentException {
        if(fixDirectoryPath()){
            FileOutputStream writerStream = new FileOutputStream(OUTPUT_PATH + "\\" +  fileName);
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
        File directory = new File(OUTPUT_PATH);
        if(!directory.exists()){
            return directory.mkdir();
        }
        return false;
    }
}
