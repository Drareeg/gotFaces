package sample;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Drareeg on 11.05.15.
 */
public class Content {

    long beginTime;
    private boolean finishedBuilding;
    private String index;
    private ArrayList words;

    public Content() {
        finishedBuilding = false;
        words = new ArrayList<String>();
    }

    public static Content fromLine(String line) {
        Content content = new Content();
        content.index = line;
        return content;
    }

    public boolean beginTimeLowerThan(long timePointer) {
        return beginTime < timePointer;
    }

    public boolean isFinishedBuilding() {
        return finishedBuilding;
    }

    public void withLine(String line) {
        if (timeHasBeenRead()) {
            if (isEndOfContent(line)) {
                finishedBuilding = true;
            } else {
                Collections.addAll(words, line.split(" "));
            }
        } else {
            parseBeginTime(line);
        }
    }

    private void parseBeginTime(String line) {
        String formattedTime = line.split("-->")[0].trim();
        StringBuilder sb = new StringBuilder();
        sb.append(formattedTime.charAt(0));
        sb.append(formattedTime.charAt(1));
        sb.append(formattedTime.charAt(3));
        sb.append(formattedTime.charAt(4));
        sb.append(formattedTime.charAt(6));
        sb.append(formattedTime.charAt(7));
        sb.append(formattedTime.charAt(9));
        sb.append(formattedTime.charAt(10));
        sb.append(formattedTime.charAt(11));
        beginTime = Long.parseLong(sb.toString());
    }

    private boolean isEndOfContent(String line) {
        return line.trim().isEmpty();
    }

    private boolean timeHasBeenRead() {
        return beginTime != 0;
    }

    public void onEmptyLine() {
        finishedBuilding = true;


    }

    public ArrayList<String> getWords() {
        return words;
    }
}
