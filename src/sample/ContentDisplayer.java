package sample;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

/**
 * Created by Drareeg on 11.05.15.
 */
public enum ContentDisplayer {

    INSTANCE;
    private WebView webView;


    public void display(Content content) {
        final StringBuilder contentBuilder = new StringBuilder();
        addBegin(contentBuilder);
        for (String word : content.getWords()) {
            addImageFor(contentBuilder, word);
        }
        addEnd(contentBuilder);


        Runnable runnable = new Runnable() {

            {
                contentString = contentBuilder.toString();
            }

            String contentString;

            @Override
            public void run() {
                WebEngine engine = webView.getEngine();
                engine.loadContent(this.contentString);
            }
        };

        Platform.runLater(runnable);
    }

    private void addImageFor(StringBuilder sb, String word) {

        if (begintMetLetter(word)) {


            String imageUrl = maakImageStringIndienGewild(word);
            if (imageUrl != null) {
                sb.append("<div>");
                sb.append(word + "<img width=\"200px\"  height=\"auto\" src=");
                sb.append(imageUrl);
                sb.append(">");
                sb.append("</div>");

            }
        }

    }

;

    private boolean begintMetLetter(String word) {
        char ch = word.charAt(0);
        return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'));
    }

    private void addEnd(StringBuilder sb) {
        sb.append("</body>");
    }

    private void addBegin(StringBuilder sb) {
        sb.append("<body>");
    }


    public void setWebView(WebView webView) {

        this.webView = webView;
    }

    public String maakImageStringIndienGewild(String word) {
        String query = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + word + "%20game%20of%20thrones";
        System.out.println(query);

        URL url;

        try {
            // get URL content
            StringBuilder sb = new StringBuilder();
            url = new URL(query);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            br.close();
            System.out.print("Voor woord " + word);
            return getBestImageSrcFromJSON(sb.toString());

        } catch (MalformedURLException | UncheckedIOException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private String getBestImageSrcFromJSON(String json) {
        try {
            JsonElement jelement = new JsonParser().parse(json);
            JsonObject jobject = jelement.getAsJsonObject();
            jobject = jobject.getAsJsonObject("responseData");

            JsonObject cursor = jobject.getAsJsonObject("cursor");
            int resultaten = Integer.parseInt(cursor.get("estimatedResultCount").toString().replace('"', '0')) / 10;
            System.out.println(resultaten + " gevonden");
            if (resultaten < 50000 || resultaten > 5000000) {
                return null;
            }
            JsonArray jarray = jobject.getAsJsonArray("results");

            jobject = jarray.get(0).getAsJsonObject();
            //jobject bevat nu het eerste result
            String result = jobject.get("unescapedUrl").toString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
