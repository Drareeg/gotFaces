package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Drareeg on 11.05.15.
 */
public class Parser {

    private File subtitleFile;
    private SubtitleModel model;

    private Parser() {
        model = new SubtitleModel();
    }

    public static Parser withFile(File subtitleFile) {
        Parser parser = new Parser();
        parser.subtitleFile = subtitleFile;
        return parser;
    }

    public void doParse() throws FileNotFoundException {
        if (!fileReady(subtitleFile)) {
            throw new RuntimeException("File wasnt loaded or invalid.");
        }
        Scanner sc = new Scanner(subtitleFile);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            model.offerLine(line);
        }
        model.noMoreOffersComing();


    }

    private boolean fileReady(File file) {
        return file != null && file.exists() && file.isFile();
    }

    public SubtitleModel getModel() {
        return model;
    }
}
