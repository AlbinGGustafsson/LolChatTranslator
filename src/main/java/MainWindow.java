import app.jackychu.api.simplegoogletranslate.Language;
import app.jackychu.api.simplegoogletranslate.SimpleGoogleTranslate;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.rmi.dgc.Lease;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainWindow extends JFrame implements NativeKeyListener {

    private SimpleGoogleTranslate translate = new SimpleGoogleTranslate();

    private Tesseract tesseract = new Tesseract();

    private JLabel label;
    private JLabel counterLabel;
    private JButton button2;
    private static ScreenshotInfo screenshotInfo;

    private JTextArea textArea;

    TranslateService translateService = new TranslateService();
    OcrService ocrService = new OcrService();

    ConfigService configService = new ConfigService();


    public MainWindow() {
        super("Main Window");

        counterLabel = new JLabel();
        label = new JLabel("Size and Location Info Here");
        JButton button = new JButton("Select screenshot dimensions");
        textArea = new JTextArea();
        button2 = new JButton("Translate");
        button2.setEnabled(false);

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                translate();
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScreenshotWindow screenshotWindow = new ScreenshotWindow(MainWindow.this, screenshotInfo);
                screenshotWindow.setVisible(true);
            }
        });

        setLayout(new FlowLayout());
        add(button2);
        add(button);
        add(label);
        add(counterLabel);
        add(textArea);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 100);

        setInfo(loadScreenshotInfo());
    }

    private void translate() {

        SwingUtilities.invokeLater(() -> {

            Robot robot = null;
            try {
                robot = new Robot();
            } catch (AWTException ex) {
                throw new RuntimeException(ex);
            }
            Rectangle area = screenshotInfo.toRectangle(); // specify the area
            BufferedImage bufferedImage = robot.createScreenCapture(area);
            BufferedImage blackWhiteImage = ImageService.convertToBlackAndWhite(bufferedImage);
            String ocrText = ocrService.performOcr(bufferedImage);
            ArrayList<String> translatedText = translateService.translate(ocrText, Language.tr, Language.sv);
            for (String line : translatedText) {
                textArea.append(line + "\n");
            }
            pack();



        });

//        SwingUtilities.invokeLater(() -> {
//            Robot robot = null;
//            try {
//                robot = new Robot();
//            } catch (AWTException ex) {
//                throw new RuntimeException(ex);
//            }
//            Rectangle area = screenshotInfo.toRectangle(); // specify the area
//            BufferedImage bufferedImage = robot.createScreenCapture(area);
//
//            tesseract.setDatapath("tessdata");
//            tesseract.setLanguage("tur+eng");
//
//            BufferedImage blackWhiteImage = ImageService.convertToBlackAndWhite(bufferedImage);
//
//            String text = null;
//            try {
//                text = tesseract.doOCR(blackWhiteImage);
//            } catch (TesseractException ex) {
//                throw new RuntimeException(ex);
//            }
//
//            String[] textArray = text.split("\n");
//            ArrayList<String> resultList = new ArrayList<>();
//
//            for (String line : textArray) {
//
//                try {
//                    String result = translate.doTranslate(Language.tr, Language.sv, line);
//                    resultList.add(result);
//
//                } catch (IOException | InterruptedException ex) {
//                    System.out.println(line + " could not be translated");
//                }
//            }
//
//            ArrayList<String> swedishSentences = extractResult(resultList);
//            textArea.setText("");
//
//            for (String line : swedishSentences) {
//                textArea.append(line + "\n");
//            }
//            pack();
//        });

    }

    public static ArrayList<String> extractResult(ArrayList<String> inputList) {
        ArrayList<String> sentences = new ArrayList<>();

        for (String line : inputList) {

            try {
                int start = line.lastIndexOf("[[\\\"");
                sentences.add(line.substring(start + 4));

            } catch (Exception e) {
                //Gör inget
            }
        }
        return sentences;
    }


    public void setInfo(ScreenshotInfo info) {
        label.setText(info.toString());
        screenshotInfo = info;

        button2.setEnabled(true);

    }

    public static void main(String[] args) {
        initializeJNativeHook();

        EventQueue.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            GlobalScreen.addNativeKeyListener(mainWindow); // Add the listener here
            mainWindow.setVisible(true);
        });
    }

    public ScreenshotInfo loadScreenshotInfo() {
        File file = new File("screenshotinfo.txt");
        if (!file.exists()) { // Check if file does not exist
            try {
                PrintWriter writer = new PrintWriter(file, "UTF-8");
                writer.println("0:0:500:500"); // Write default values
                writer.close();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception
            }
        }

        // Now, try to read from the file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine(); // Read the first line in the file
            if (line != null) {
                String[] parts = line.split(":");
                if (parts.length == 4) { // Ensure there are exactly 4 values
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    int width = Integer.parseInt(parts[2]);
                    int height = Integer.parseInt(parts[3]);

                    ScreenshotInfo info = new ScreenshotInfo(x, y, width, height);
                    return info; // Create and return new object
                } else {
                    System.err.println("File format is incorrect!");
                }
            } else {
                System.err.println("File is empty!");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception
        }
        return null;
    }

    private static void initializeJNativeHook() {
//        // Disable logging for JNativeHook
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
//
//        // Change the logging level to off for the console handler
//        Handler[] handlers = Logger.getLogger("").getHandlers();
//        for (Handler handler : handlers) {
//            handler.setLevel(Level.OFF);
//        }

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        //System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()));

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        //System.out.println("Key Released: " + NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()));

        if (NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()).equals("Back Slash")) {
            translate();
        }

    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
}