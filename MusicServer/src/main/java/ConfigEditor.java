import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ConfigEditor implements Initializable {

    private MainView main;
    private MainController mainController;

    @FXML
    private Button saveBtn;
    @FXML
    private Button returnBtn;
    @FXML
    private FontAwesomeIconView saveIcon;
    @FXML
    private FontAwesomeIconView returnIcon;
    @FXML
    private Label pathLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField artistField;
    @FXML
    private TextField albumField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField yearField;


    private MusicInfo selectedMusic;

    public void init(MainView mv) {
        this.main = mv;
    }

    public void setMainController(MainController mc) {
        this.mainController=mc;
    }

    public void setSelectedMusic(MusicInfo music) {
        this.selectedMusic = music;
    }


    public void hooverSave() {
        saveBtn.setTextFill(Paint.valueOf("#0d8be0"));
        saveIcon.setFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverReturn() {
        returnBtn.setTextFill(Paint.valueOf("#0d8be0"));
        returnIcon.setFill(Paint.valueOf("#0d8be0"));
    }

    public void exitSave() {
        saveBtn.setTextFill(Paint.valueOf("#000000"));
        saveIcon.setFill(Paint.valueOf("#000000"));
    }

    public void exitReturn() {
        returnBtn.setTextFill(Paint.valueOf("#000000"));
        returnIcon.setFill(Paint.valueOf("#000000"));
    }

    public void goBack() {
        this.mainController.getMainView().showConfigManager();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void loadFields() {
        pathLabel.setText(selectedMusic.getPath());
        durationLabel.setText(selectedMusic.getDuration() + " s");
        nameField.setText(selectedMusic.getName());
        artistField.setText(selectedMusic.getArtist());
        albumField.setText(selectedMusic.getAlbum());
        genreField.setText(selectedMusic.getGenre());
        yearField.setText(selectedMusic.getYear());
    }

    public void replaceMeta() throws IOException {
        String name = nameField.getText();
        String artist = artistField.getText();
        String album = albumField.getText();
        String genreName = selectedMusic.getGenre();
        String genre = genreField.getText();
        String year = yearField.getText();
        String duration = selectedMusic.getDuration();
        String path = selectedMusic.getPath();
        MusicInfo newmusic = new MusicInfo(name, artist, album, genre, year, duration,path);
        if (genreName.equalsIgnoreCase(genre)) {
            File f = new File("Metadata/" + genreName + ".meta");
            Scanner sc = new Scanner(f);
            StringBuffer buffer = new StringBuffer(); //instantiating the StringBuffer class
            while (sc.hasNextLine()) {  //Reading lines of the file and appending them to StringBuffer
                buffer.append(sc.nextLine() + System.lineSeparator());
            }
            String fileContents = buffer.toString();
            sc.close();
            String oldLine = selectedMusic.toString();
            String newLine = newmusic.toString();
            fileContents = fileContents.replace(oldLine, newLine); //Replacing the old line with new line
            FileWriter writer = new FileWriter(f); //instantiating the FileWriter class
            writer.append(fileContents);
            writer.flush();
        }
        else Utilities.writeMusicMeta(newmusic);

    }

}
