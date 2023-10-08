import java.io.*;

public class ConfigService {

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

}
