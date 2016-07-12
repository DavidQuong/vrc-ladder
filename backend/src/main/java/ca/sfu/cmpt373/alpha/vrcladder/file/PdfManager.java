package ca.sfu.cmpt373.alpha.vrcladder.file;

import ca.sfu.cmpt373.alpha.vrcladder.file.logic.ElementsFactory;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * This class is to manage the pdf file.
 */
public class PdfManager {

    public void testingFunction(){
        ITextRenderer renderer = new ITextRenderer();
        FileReader file;
        Map<String, String> values = new HashMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String date = dateFormat.format(new Date());
        values.put("#dateandtime", date);

        String PdfContents = "";



        try {
            file = new FileReader("src\\main\\resources\\pdf\\templates\\layout.html");
            BufferedReader reader = new BufferedReader(file);
            List<String> pdfTags = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                PdfContents += line;
                if(line.contains("#")){
                    pdfTags.addAll(findTags(line));
                }
                PdfContents += "\n";
            }

            PdfContents = replaceTags(PdfContents, pdfTags, values);

        } catch (IOException e) {
            e.printStackTrace();
        }

        renderer.getSharedContext().setReplacedElementFactory(new ElementsFactory(renderer.getSharedContext().getReplacedElementFactory()));
        renderer.setDocumentFromString(PdfContents);
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

    private static List<String> findTags(String currentLine) {
        List<String> results = new ArrayList<>();
        String currentTag = "";
        char[] lineContents = currentLine.toCharArray();
        boolean currentStatus = false;
        boolean addToTags = false;
        for (char c : lineContents) {
            if (addToTags) {
                results.add(currentTag);
                addToTags = false;
                currentTag = "";
            }

            if (c == '#') {
                currentStatus = true;
            }

            if (currentStatus) {
                if (isEndOfTag(c)) {
                    currentStatus = false;
                    addToTags = true;
                }else{
                    currentTag = currentTag + c;
                }
            }
        }

        if (!currentTag.isEmpty()) {
            results.add(currentTag);
        }
        return results;
    }

    private static boolean isEndOfTag(char currentLetter) {
        return (currentLetter == ',' || currentLetter == '/' || currentLetter == '<');
    }

    private String replaceTags(String messageContent, List<String> tags, Map<String, String> values) {
        for (String tag : tags) {
            if (values.containsKey(tag)) {
                String value = values.get(tag);
                messageContent = messageContent.replaceAll(tag, value);
            }
        }
        return messageContent;
    }

}
