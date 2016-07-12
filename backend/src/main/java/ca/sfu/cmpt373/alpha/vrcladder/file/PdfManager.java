package ca.sfu.cmpt373.alpha.vrcladder.file;

import com.lowagie.text.DocumentException;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileOutputStream;


/**
 * This class is to manage the pdf file.
 */
public class PdfManager {

    public void testingFunction(){
        ITextRenderer renderer = new ITextRenderer();
        FileReader file;

        String PdfContents = "";
        try {
            file = new FileReader("src\\main\\resources\\pdf\\templates\\layout.html");
            BufferedReader reader = new BufferedReader(file);

            String line;
            while ((line = reader.readLine()) != null) {
                PdfContents += line + "\n";
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        renderer.setDocumentFromString( PdfContents );
        renderer.layout();

        String outputPath = "PDF\\PDF-FromHtmlString.pdf";
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream( outputPath );
            renderer.createPDF( outputStream );
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("PDF was created successfully!");
    }

}
