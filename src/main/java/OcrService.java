import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;

public class OcrService {

    private Tesseract tesseract = new Tesseract();

    public OcrService() {
        tesseract.setDatapath("tessdata");
        tesseract.setLanguage("tur+eng");
    }

    public void setLanguage(String language){
        setLanguage(language);
    }

    public String performOcr(BufferedImage image){
        String text = null;
        try {
            text = tesseract.doOCR(image);
        } catch (TesseractException ex) {
            throw new RuntimeException(ex);
        }

        return text;
    }

}
