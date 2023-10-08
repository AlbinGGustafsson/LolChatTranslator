import java.awt.*;

public class ScreenshotInfo {
    private int x;
    private int y;

    private int width;
    private int height;


    public ScreenshotInfo(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }


    public Rectangle toRectangle(){
        return new Rectangle(x, y, width, height);
    }

    @Override
    public String toString() {
        return String.format("x: %d y: %d w: %d h: %d", x, y, width, height);
    }
}
