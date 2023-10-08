import app.jackychu.api.simplegoogletranslate.Language;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainWindow extends JFrame implements NativeKeyListener {

    private JLabel screenshotInfoLabel;
    private JButton translateButton;

    private JButton screenshotInfoButton;
    private static ScreenshotInfo screenshotInfo;

    private JTextArea translatedTextArea;

    private TranslateService translateService = new TranslateService();
    private OcrService ocrService = new OcrService();

    private ConfigService configService = new ConfigService();


    public MainWindow() {
        super("Main Window");

        screenshotInfoLabel = new JLabel("Size and Location Info Here");
        screenshotInfoButton= new JButton("Select screenshot dimensions");
        translatedTextArea = new JTextArea();
        translateButton = new JButton("Translate");
        translateButton.setEnabled(false);

        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                translate();
            }
        });

        screenshotInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScreenshotWindow screenshotWindow = new ScreenshotWindow(MainWindow.this, screenshotInfo);
                screenshotWindow.setVisible(true);
            }
        });

        setLayout(new FlowLayout());
        add(translateButton);
        add(screenshotInfoButton);
        add(screenshotInfoLabel);
        add(translatedTextArea);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 100);

        setInfo(configService.loadScreenshotInfo());
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
                translatedTextArea.append(line + "\n");
            }
            pack();

        });

    }

    public void setInfo(ScreenshotInfo info) {
        screenshotInfoLabel.setText(info.toString());
        screenshotInfo = info;

        translateButton.setEnabled(true);
    }

    public static void main(String[] args) {
        initializeJNativeHook();

        EventQueue.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            GlobalScreen.addNativeKeyListener(mainWindow); // Add the listener here
            mainWindow.setVisible(true);
        });
    }

    private static void initializeJNativeHook() {
        // Disable logging for JNativeHook
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        //Todo kolla på detta
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
        if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_BACK_SLASH) {
            translate();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
}