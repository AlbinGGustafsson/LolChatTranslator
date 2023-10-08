//Gammal kod för att testa externa tjänsterna

//import app.jackychu.api.simplegoogletranslate.Language;
//import app.jackychu.api.simplegoogletranslate.SimpleGoogleTranslate;
//import net.sourceforge.tess4j.Tesseract;
//import net.sourceforge.tess4j.TesseractException;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class Main {
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//
//        SimpleGoogleTranslate translate = new SimpleGoogleTranslate();
//
//        Tesseract tesseract = new Tesseract();
//        try {
//
//            //Robot robot = new Robot();
//            //Rectangle area = new Rectangle(0, 0, 300, 300); // specify the area
//            //BufferedImage bufferedImage = robot.createScreenCapture(area);
//
//
//            tesseract.setDatapath("tessdata");
//            tesseract.setLanguage("tur+eng");
//
//            // the path of your tess data folder
//            // inside the extracted file
//
//            //File file = new File("test2.png");
//            //BufferedImage blackWhiteImage = BlackAndWhiteConverter.convertToBlackAndWhite(bufferedImage);
//
//
//            String text = tesseract.doOCR(new File("turkey.png"));
//
//            // path of your image file
//            System.out.print(text);
//        }
//        catch (TesseractException e) {
//            e.printStackTrace();
//        }
//
////        String turkish = "Dolmuş taşımacılığı nasıl başlamış, biliyor musunuz?";
////
////        //String result = translate.doTranslate(Language.en, Language.sv, "Hello, World!");
////        //String result = translate.doTranslate(Language.tr, Language.sv, turkish);
////        String result = translate.doTranslate(Language.tr, Language.sv, turkish);
////
////        System.out.println(result);
////        String parsed = result.substring(result.lastIndexOf(",[[\\\"")+5);
////        System.out.println(parsed);
//
//    }
//
//}
