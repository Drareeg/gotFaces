package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    Parser parser;

    @FXML
    Label label;

    @FXML
    Label labelLaatsteRegel;

    @FXML
    WebView webView;

    //TODO
    SRTPlayer srtPlayer = new SRTPlayer();

    @FXML
    private Button playButton;

    @FXML
    private Button selectButton;


    @FXML
    public void playPressed(ActionEvent e) {
        srtPlayer.togglePlay();
    }

    @FXML
    public void selectPressed(ActionEvent e) throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        if (new File("E:\\Downloads\\Game of Thrones S04 Season 4 Complete 720p HDTV x264 [Multi-Sub] [DexzAery]\\Subtitles\\English").exists()) {
            fc.setInitialDirectory(new File("E:\\Downloads\\Game of Thrones S04 Season 4 Complete 720p HDTV x264 [Multi-Sub] [DexzAery]\\Subtitles\\English"));
        }
        File subtitleFile = fc.showOpenDialog(null);
        selectButton.setDisable(true);
        parser = Parser.withFile(subtitleFile);
        parser.doParse();
        srtPlayer.setModel(parser.getModel());
        label.textProperty().bind(srtPlayer.getTimeProperty());
        labelLaatsteRegel.textProperty().bind(srtPlayer.getLastLineProperty());
        playButton.setDisable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ContentDisplayer.INSTANCE.setWebView(webView);
    }
}
