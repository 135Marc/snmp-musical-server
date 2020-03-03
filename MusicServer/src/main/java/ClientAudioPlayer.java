import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.snmp4j.smi.OID;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class ClientAudioPlayer implements Initializable {

    @FXML
    private Label lengthLabel;
    @FXML
    private Label elapsedLabel;
    @FXML
    private FontAwesomeIconView playpause;
    @FXML
    private FontAwesomeIconView volumeIcon;
    @FXML
    private FontAwesomeIconView forwardIcon;
    @FXML
    private FontAwesomeIconView backwardIcon;
    @FXML
    private FontAwesomeIconView stop;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider timeSlider;
    @FXML
    private ListView<String> songNames;
    @FXML
    private Button returnBtn;
    @FXML
    private FontAwesomeIconView returnIcon;
    @FXML
    private Button loopBtn;
    @FXML
    private FontAwesomeIconView loopIcon;

    // Player logic related variables
    private List<MusicInfo> musicList;
    private float rate = 1.0f;
    private File musicFile;
    private boolean playing=false;
    private AudioPlayerComponent mediaPlayerComponent = new AudioPlayerComponent();
    // MVC related variables
    private MainView main;
    private MainController mainController;

    public void init(MainView mv) {
        this.main = mv;
    }

    public void setMainController(MainController mc) {
        this.mainController=mc;
    }

    public void readMusicPaths() {
        musicList = this.mainController.getMainAgent().getMusicList();
        if (songNames.getItems()!=null) songNames.getItems().clear();
        for(MusicInfo mi : musicList) {
            songNames.getItems().add(mi.getArtist() + " - " + mi.getName() + " (" + mi.getYear() + ")");
        }
    }

    public void changeMusic() {
        if (songNames.getSelectionModel().getSelectedItem()!=null) {
            int selectedIndex = songNames.getSelectionModel().getSelectedIndex();
            String path = musicList.get(selectedIndex).getPath();
            musicFile = new File(path);
            mediaPlayerComponent.mediaPlayer().media().start(musicFile.getAbsolutePath());
            lengthLabel.setText(musicDuration(mediaPlayerComponent.mediaPlayer().status().length()));
            timeSlider.setValue(0);
            playpause.glyphNameProperty().setValue("PAUSE_CIRCLE");
            mediaPlayerComponent.mediaPlayer().audio().setVolume(100);
            volumeSlider.setValue(100.0);
            this.playing=true;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    public void hooverPause() {
        playpause.setFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverStop() { stop.setFill(Paint.valueOf("#c10911")); }


    public void hooverVolume() {
        if (mediaPlayerComponent.mediaPlayer().audio().isMute()) {
            volumeIcon.setFill(Paint.valueOf("#0d8be0"));
        }
        else  volumeIcon.setFill(Paint.valueOf("#c10911"));
    }

    public void hooverReturn() {
        returnIcon.setFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverLoop() {
        loopIcon.setFill(Paint.valueOf("#0d8be0"));
    }

    public void exitReturn() {
        returnIcon.setFill(Paint.valueOf("#000000"));
    }

    public void exitStop() {
        stop.setFill(Paint.valueOf("#08c44d"));
    }

    public void exitSelection() {
        playpause.setFill(Paint.valueOf("#08c44d"));
    }

    public void enterForward() {
        forwardIcon.setFill(Paint.valueOf("#0d8be0"));
    }

    public void enterBackward() {
        backwardIcon.setFill(Paint.valueOf("#0d8be0"));
    }

    public void exitForward() {
        forwardIcon.setFill(Paint.valueOf("#08c44d"));
    }

    public void exitBackward() {
        backwardIcon.setFill(Paint.valueOf("#08c44d"));
    }


    public void exitVolume() {
        volumeIcon.setFill(Paint.valueOf("#000000"));
    }

    public void exitLoop() {
        loopIcon.setFill(Paint.valueOf("#08c44d"));
    }

    public void goBack() {
        this.mediaPlayerComponent.mediaPlayer().release();
        this.mainController.getMainView().showClientOptionsDisplay();
    }

    public void setRepeat() {
        boolean onRepeat = mediaPlayerComponent.mediaPlayer().controls().getRepeat();
        if(!onRepeat) this.mediaPlayerComponent.mediaPlayer().controls().setRepeat(true);
        else this.mediaPlayerComponent.mediaPlayer().controls().setRepeat(false);
    }


    public void play() {
       if (!playing) {
           if (!mediaPlayerComponent.mediaPlayer().media().isValid()) {
               mediaPlayerComponent.mediaPlayer().media().start(musicFile.getAbsolutePath());
           }
           else {
               mediaPlayerComponent.mediaPlayer().controls().setPause(false);
           }
           playpause.glyphNameProperty().setValue("PAUSE_CIRCLE");
           mediaPlayerComponent.mediaPlayer().audio().setVolume(100);
           volumeSlider.setValue(100.0);
           lengthLabel.setText(musicDuration(mediaPlayerComponent.mediaPlayer().status().length()));
       }
       else {
           mediaPlayerComponent.mediaPlayer().controls().setPause(true);
           playpause.glyphNameProperty().setValue("PLAY_CIRCLE");
       }
       this.playing=!this.playing;
    }

    public void setVolume() {
            volumeSlider.valueChangingProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    int vol = (int) Math.round(volumeSlider.getValue());
                    mediaPlayerComponent.mediaPlayer().audio().setVolume(vol);
                }
            });
    }

    public void setMute() {
       if (!mediaPlayerComponent.mediaPlayer().audio().isMute()) {
           mediaPlayerComponent.mediaPlayer().audio().mute();
           volumeIcon.glyphNameProperty().setValue("VOLUME_OFF");
       }
       else {
           mediaPlayerComponent.mediaPlayer().audio().setMute(false);
           volumeIcon.glyphNameProperty().setValue("VOLUME_UP");
       }
    }

    public void skipTime() {
        rate+=0.5f;
        mediaPlayerComponent.mediaPlayer().controls().setRate(rate);
    }


    public void backTime() {
        rate-=0.5;
        mediaPlayerComponent.mediaPlayer().controls().setRate(rate);
    }

    public void stopMusic() {
        mediaPlayerComponent.mediaPlayer().controls().stop();
    }

    public String musicDuration(long total) {
        StringBuilder sb = new StringBuilder();
        long durationInSeconds = TimeUnit.MILLISECONDS.toSeconds(total);
        long mins = TimeUnit.SECONDS.toMinutes(durationInSeconds);
        long secs = durationInSeconds % 60;
        if (mins<10) {
            sb.append("0")
                    .append(mins);
        }
        else sb.append(mins);

        sb.append(":");
        if (secs<10) sb.append("0");
        sb.append(secs);
        return sb.toString();
    }


    public void changeElapsed() {
        boolean playing = mediaPlayerComponent.mediaPlayer().status().isPlaying();
        if (playing) {
            String dur = musicDuration(mediaPlayerComponent.mediaPlayer().status().time());
            elapsedLabel.setText(dur);
        }
    }

    public void changeSlider() {
        boolean playing = mediaPlayerComponent.mediaPlayer().status().isPlaying();
        if (playing) {
            timeSlider.valueChangingProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    float pos = Math.round(timeSlider.getValue());
                    mediaPlayerComponent.mediaPlayer().controls().setPosition(pos/100);
                }
            });
        }
    }

    public void updateSlider() {
        boolean playing = mediaPlayerComponent.mediaPlayer().status().isPlaying();
        if (playing) {
            double pos = mediaPlayerComponent.mediaPlayer().status().position() * 100;
            timeSlider.setValue(pos);
        }
    }

    public void elapsedUpdate() {
        Timeline timeholder = new Timeline(new KeyFrame(Duration.seconds(1) , new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeElapsed();
                updateSlider();
            }
        }));
        timeholder.setCycleCount(Timeline.INDEFINITE);
        timeholder.play();
    }

    public void incrementListenCounts() {
        if (songNames.getSelectionModel().getSelectedItem()!=null) {
            int index = songNames.getSelectionModel().getSelectedIndex();
            this.mainController.getMainAgent().asyncBulkGet(new OID("1.3.6.1.3.2020.1.3.1.8"));
            List<String> results = this.mainController.getMainAgent().getResultList();
            long counter = Long.parseLong(results.get(index)) + 1;
            this.mainController.getMainAgentUtilities().updateMusicCounter(counter, index);
            this.mainController.getMainAgent().clearResults();
        }
    }

    public void updateListens() {
        incrementListenCounts();
        changeMusic();
    }



}
