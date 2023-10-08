//import java.io.OutputStream;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
////https://github.com/dynomake/libretranslate-java
//
//public class LibreTest {
//    private static final String API_URL = "https://translate.argosopentech.com/translate";
//    private static final String API_KEY = "";  // insert your API key here
//
//    public static void main(String[] args) {
//        try {
//            URL url = new URL(API_URL);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//
//            String jsonInputString = String.format("{\"q\":\"hejsan vad gör du\",\"source\":\"auto\",\"target\":\"tr\",\"format\":\"text\",\"api_key\":\"%s\"}", API_KEY);
//
//            try (OutputStream os = connection.getOutputStream()) {
//                byte[] input = jsonInputString.getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//
//            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
//                StringBuilder response = new StringBuilder();
//                String responseLine;
//                while ((responseLine = br.readLine()) != null) {
//                    response.append(responseLine.trim());
//                }
//                System.out.println(response.toString());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}