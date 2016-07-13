package ca.sfu.cmpt373.alpha.vrcladder.file;

import ca.sfu.cmpt373.alpha.vrcladder.file.logic.ElementsFactory;
import ca.sfu.cmpt373.alpha.vrcladder.tagging.TagsSystem;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * This class is to manage the pdf file.
 */
public class PdfManager {

    private static final TagsSystem tags = new TagsSystem();

    public void testingFunction(){
        ITextRenderer renderer = new ITextRenderer();
        FileReader file;
        Map<String, String> values = new HashMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String date = dateFormat.format(new Date());
        values.put("#dateandtime", date);

        String pdfContants = "";



        try {
            file = new FileReader("src\\main\\resources\\pdf\\templates\\layout.html");
            BufferedReader reader = new BufferedReader(file);
            List<String> pdfTags = new ArrayList<>();
            pdfContants = tags.buildContentsAndTags(reader, pdfTags);
            pdfContants = tags.replaceTags(pdfContants, pdfTags, values);

        } catch (IOException e) {
            e.printStackTrace();
        }

        renderer.getSharedContext().setReplacedElementFactory(new ElementsFactory(renderer.getSharedContext().getReplacedElementFactory()));
        renderer.setDocumentFromString(pdfContants);
        ITextFontResolver fontResolver = renderer.getFontResolver();
        String paths = "src\\main\\resources\\pdf\\fonts\\Lato-Medium.ttf";
        try {
            fontResolver.addFont(paths, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.layout();
            String outputPath = "PDF\\PDF-FromHtmlString.pdf";
            FileOutputStream outputStream = new FileOutputStream( outputPath );
            renderer.createPDF(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("PDF was created successfully!");
    }

}
