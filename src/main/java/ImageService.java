import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageService {
    public static BufferedImage convertToBlackAndWhite(BufferedImage colorImage) {
        // Check if the input image is null
        if (colorImage == null) {
            throw new IllegalArgumentException("Input image cannot be null");
        }

        // Create a new grayscale image with the same width and height as the input image
        BufferedImage grayImage = new BufferedImage(colorImage.getWidth(), colorImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        // Get the Graphics2D object from the grayscale image
        Graphics2D g = grayImage.createGraphics();

        // Draw the input color image onto the grayscale image
        // This will convert the color image to grayscale
        g.drawImage(colorImage, 0, 0, null);

        // Dispose the Graphics2D object
        g.dispose();

        // Return the grayscale image
        return grayImage;
    }
}