//TODO
//Kanske testar att använda denna translate tjänst istället

import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

//https://github.com/dynomake/libretranslate-java

public class LibreTest {

    private static OcrService ocrService = new OcrService();
    private static ImageService imageService = new ImageService();
    private static final String API_URL = "https://translate.argosopentech.com/translate";
    private static final String API_KEY = "";  // insert your API key here

    public static void main(String[] args) throws IOException {

        String filePath = "turkey.png";

        // Load the BufferedImage from the file path
        BufferedImage image = ImageIO.read(new File(filePath));
        BufferedImage bw = imageService.convertToBlackAndWhite(image);

        String ocrText = ocrService.performOcr(bw);



        try {
            HttpClient client = HttpClient.newHttpClient();

            JSONObject json = new JSONObject();
            json.put("q", ocrText);
            json.put("source", "tr");
            json.put("target", "en");
            json.put("format", "text");
            json.put("api_key", API_KEY);

            String jsonInputString = json.toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            //System.out.println(response.body());


            JSONObject jsonObject = new JSONObject(response.body());

            // Extract the "translatedText" value and print it
            String translatedText = jsonObject.getString("translatedText");
            System.out.println(translatedText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}