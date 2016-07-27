package ca.sfu.cmpt373.alpha.vrcladder.file.logic;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;
import spark.utils.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is to create a replacement factory which will
 * replace the images in the HTML layout file into an actual
 * image that we can use in the pdf file.
 * REFERENCE : http://stackoverflow.com/questions/11477065/using-flying-saucer-to-render-images-to-pdf-in-memory
 *             (Alex, 1st Answer)
 */
public class ElementsFactory implements ReplacedElementFactory {

    private final ReplacedElementFactory superFactory;
    private static final String ERROR_IMAGE_NO_SOURCE = "Found an image without a source path!";
    private static final String ERROR_IMAGE_FROM_PATH = "There was a problem in getting the image from the path.";
    private static final String IMAGE_HTML_TAG = "img";

    public ElementsFactory(ReplacedElementFactory superFactory) {
        this.superFactory = superFactory;
    }

    @Override
    public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
        Element element = box.getElement();
        if (element == null) {
            return null;
        }
        String nodeName = element.getNodeName();
        if (IMAGE_HTML_TAG.equals(nodeName)) {
            isElementValid(element);
            try {
                final FSImage fsImage = getImage(element);
                boolean scaleImage = scaleImage(fsImage, cssWidth, cssHeight);
                if(scaleImage){
                    return new ITextImageElement(fsImage);
                }
            } catch (Exception e) {
                throw new RuntimeException(ERROR_IMAGE_FROM_PATH, e);
            }
        }
        return this.superFactory.createReplacedElement(c, box, uac, cssWidth, cssHeight);
    }

    private void isElementValid(Element element) {
        if (!element.hasAttribute(PdfSettings.IMAGE_SOURCE)) {
            throw new RuntimeException(ERROR_IMAGE_NO_SOURCE);
        }
    }

    @Override
    public void reset() {
        this.superFactory.reset();
    }

    @Override
    public void remove(Element e) {
        this.superFactory.remove(e);
    }

    @Override
    public void setFormSubmissionListener(FormSubmissionListener listener) {
        this.superFactory.setFormSubmissionListener(listener);
    }

    private FSImage getImage(Element element) throws IOException, BadElementException {
        InputStream input = new FileInputStream(PdfSettings.IMAGE_PATH + element.getAttribute(PdfSettings.IMAGE_SOURCE));
        final byte[] bytes = IOUtils.toByteArray(input);
        final Image image = Image.getInstance(bytes);
        return new ITextFSImage(image);
    }

    private boolean scaleImage(FSImage image, int cssWidth, int cssHeight){
        if (image != null) {
            if ((cssWidth != -1) || (cssHeight != -1)) {
                image.scale(cssWidth, cssHeight);
            }
            return true;
        }
        return false;
    }
}
