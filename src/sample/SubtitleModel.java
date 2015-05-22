package sample;

import javafx.beans.value.ObservableValue;

import java.util.Stack;

/**
 * Created by Drareeg on 11.05.15.
 */
public class SubtitleModel {


    Stack<Content> firstContentAtTheTop;
    Stack<Content> firstAtTheBottom;
    Content buildingContent;

    public SubtitleModel() {
        firstContentAtTheTop = new Stack<Content>();
        firstAtTheBottom = new Stack<Content>();

    }

    public void offerLine(String line) {
        System.out.println("line offered" + line);
        if (buildingContent == null) {
            buildingContent = Content.fromLine(line);
        } else {
            if (buildingContent.isFinishedBuilding()) {
                firstAtTheBottom.push(buildingContent);
                buildingContent = Content.fromLine(line);
            } else {
                buildingContent.withLine(line);
            }
        }

    }

    public void noMoreOffersComing() {
        offerLine("");
        while (!firstAtTheBottom.empty()) {
            firstContentAtTheTop.push(firstAtTheBottom.pop());
        }
    }

    public Content nextContent() {
        return firstContentAtTheTop.pop();
    }

    public boolean hasNewContentForTime(long timePointer) {
        if(firstContentAtTheTop.empty()){
            return false;
        }
        return firstContentAtTheTop.peek().beginTimeLowerThan(timePointer);
    }
}
