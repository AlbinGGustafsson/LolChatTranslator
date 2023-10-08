import app.jackychu.api.simplegoogletranslate.Language;
import app.jackychu.api.simplegoogletranslate.SimpleGoogleTranslate;

import java.io.IOException;
import java.util.ArrayList;

public class TranslateService {

    private SimpleGoogleTranslate translate = new SimpleGoogleTranslate();

    public ArrayList<String> translate(String text, Language srcLang, Language targetLang) {

        String[] textArray = text.split("\n");
        ArrayList<String> resultList = new ArrayList<>();

        for (String line : textArray) {

            try {
                String result = translate.doTranslate(srcLang, targetLang, line);
                resultList.add(result);

            } catch (IOException | InterruptedException ex) {
                System.out.println(line + " could not be translated");
            }
        }
        return extractResult(resultList);
    }

    private ArrayList<String> extractResult(ArrayList<String> inputList) {
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

}
