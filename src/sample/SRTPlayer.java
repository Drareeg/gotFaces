package sample;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Drareeg on 11.05.15.
 */
public class SRTPlayer {
    private SubtitleModel model;

    private boolean playing = false;
    private long timePassedBeforeThisPlay;
    private long startTime;
    private long timePointer;
    private SimpleStringProperty timeProperty = new SimpleStringProperty();
    private SimpleStringProperty lastLineProperty = new SimpleStringProperty();


    public void setModel(SubtitleModel model) {
        this.model = model;
    }

    public synchronized void togglePlay() {
        if (playing) {
            pause();
            playing = false;
        } else {
            play();
            playing = true;
        }

    }

    private void play() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startTime = System.currentTimeMillis();

                while (playing) {
                    wachtEenBeetje();
                    kijkOfJeIetsMoetDisplayen();
                    updateTimePointer();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void updateTimePointer() {
        timePointer = timePassedBeforeThisPlay + (System.currentTimeMillis() - startTime);
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timePointer),
                TimeUnit.MILLISECONDS.toMinutes(timePointer) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timePointer)),
                TimeUnit.MILLISECONDS.toSeconds(timePointer) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timePointer)));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                timeProperty.set(hms);
            }
        });

    }

    private void kijkOfJeIetsMoetDisplayen() {
        Content content = null;
        while (model.hasNewContentForTime(timePointer)) {
            content = model.nextContent();
        }
        final Content contentToShow = content;
        if (content != null) {
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    lastLineProperty.set(contentToShow.getWords().stream().collect(Collectors.joining(" ")));
                }


            });
            ContentDisplayer.INSTANCE.display(content);
        }
    }

    private void wachtEenBeetje() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void pause() {
        updateTimePointer();
        timePassedBeforeThisPlay = timePointer;
    }

    public ObservableValue<? extends String> getTimeProperty() {
        return timeProperty;
    }

    public ObservableValue<? extends String> getLastLineProperty() {
        return lastLineProperty;
    }
}
