import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintWriter;

public class SpawnedWindow extends JFrame implements NativeKeyListener {
    private MainWindow mainWindow;

    private Point initialClick;

    public SpawnedWindow(MainWindow mainWindow, ScreenshotInfo screenshotInfo) {
        super("Spawned Window");

        GlobalScreen.addNativeKeyListener(this);

        this.mainWindow = mainWindow;

        setUndecorated(true);
        setOpacity(0.5f);
        getRootPane().setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));

        JButton cancelButton = new JButton("Cancel");

        JButton sendInfoButton = new JButton("Send Info");
        sendInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendInfo();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JButton increaseWidthButton = new JButton("Increase Width");
        increaseWidthButton.addActionListener(e -> setSize(getWidth() + 30, getHeight()));

        JButton decreaseWidthButton = new JButton("Decrease Width");
        decreaseWidthButton.addActionListener(e -> setSize(getWidth() - 30, getHeight()));

        JButton increaseHeightButton = new JButton("Increase Height");
        increaseHeightButton.addActionListener(e -> setSize(getWidth(), getHeight() + 30));

        JButton decreaseHeightButton = new JButton("Decrease Height");
        decreaseHeightButton.addActionListener(e -> setSize(getWidth(), getHeight() - 30));

        // Adding mouse listener for window dragging
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                int x = thisX + xMoved;
                int y = thisY + yMoved;
                setLocation(x, y);
            }
        });

        setLayout(new FlowLayout());
        add(sendInfoButton);
        add(increaseWidthButton);
        add(decreaseWidthButton);
        add(increaseHeightButton);
        add(decreaseHeightButton);
        add(cancelButton);
        pack();
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        setSize(screenshotInfo.getWidth(), screenshotInfo.getHeight());
        setLocation(screenshotInfo.getX(), screenshotInfo.getY());

    }


    private void sendInfo() {

        Point location = getLocation();
        Dimension size = getSize();

        ScreenshotInfo screenshotInfo = new ScreenshotInfo(location.x, location.y, size.width, size.height);
        mainWindow.setInfo(screenshotInfo);

        // Save ScreenshotInfo to file
        try {
            PrintWriter writer = new PrintWriter("ScreenshotInfo.txt", "UTF-8");
            writer.println(screenshotInfo.getX() + ":" + screenshotInfo.getY() + ":" + screenshotInfo.getWidth() + ":" + screenshotInfo.getHeight());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();  // Handle exceptions appropriately
        }

        dispose();
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        int keyCode = nativeKeyEvent.getKeyCode();

        if (keyCode == NativeKeyEvent.VC_UP) {
            setSize(getWidth(), getHeight() - 15);
        } else if (keyCode == NativeKeyEvent.VC_DOWN) {
            setSize(getWidth(), getHeight() + 15);
        } else if (keyCode == NativeKeyEvent.VC_LEFT) {
            setSize(getWidth() - 15, getHeight());
        } else if (keyCode == NativeKeyEvent.VC_RIGHT) {
            setSize(getWidth() + 15, getHeight());
        } else if (keyCode == NativeKeyEvent.VC_ENTER) {
            sendInfo();
        } else if (keyCode == NativeKeyEvent.VC_ESCAPE) {
            dispose();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
}