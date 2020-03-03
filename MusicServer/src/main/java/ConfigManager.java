import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ConfigManager implements Initializable {

    private MainView main;
    private MainController mainController;
    private List<MusicInfo> musicList = new ArrayList<>();

    @FXML
    private FontAwesomeIconView loadIcon;
    @FXML
    private FontAwesomeIconView adderIcon;
    @FXML
    private FontAwesomeIconView editIcon;
    @FXML
    private FontAwesomeIconView removeIcon;
    @FXML
    private FontAwesomeIconView returnIcon;
    @FXML
    private Button loadBtn;
    @FXML
    private Button adderBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Button removeBtn;
    @FXML
    private Button returnBtn;
    @FXML
    private TableView<MusicInfo> musicTable;
    @FXML
    private TableColumn<MusicInfo,String> nameCol;
    @FXML
    private TableColumn<MusicInfo,String> artistCol;
    @FXML
    private TableColumn<MusicInfo,String> albumCol;
    @FXML
    private TableColumn<MusicInfo,String> genreCol;
    @FXML
    private TableColumn<MusicInfo,String> yearCol;
    @FXML
    private TableColumn<MusicInfo,String> durationCol;



    public void init(MainView mv) {
        this.main = mv;
    }

    public void setMainController(MainController mc) {
        this.mainController=mc;
    }

    public void hooverLoad() {
        loadIcon.setFill(Paint.valueOf("#0d8be0"));
        loadBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverAdder() {
        adderIcon.setFill(Paint.valueOf("#0d8be0"));
        adderBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverEdit() {
        editIcon.setFill(Paint.valueOf("#0d8be0"));
        editBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverRemove() {
        removeIcon.setFill(Paint.valueOf("#0d8be0"));
        removeBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverReturn() {
        returnIcon.setFill(Paint.valueOf("#0d8be0"));
        returnBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void exitLoad() {
        loadIcon.setFill(Paint.valueOf("#000000"));
        loadBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitAdder() {
        adderIcon.setFill(Paint.valueOf("#000000"));
        adderBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitEdit() {
        editIcon.setFill(Paint.valueOf("#000000"));
        editBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitRemove() {
        removeIcon.setFill(Paint.valueOf("#000000"));
        removeBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitReturn() {
        returnIcon.setFill(Paint.valueOf("#000000"));
        returnBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void showAdder() {
        this.mainController.getMainView().showConfigAdder();
    }

    public void showEditor() {
        MusicInfo music = this.musicTable.getSelectionModel().getSelectedItem();
        this.mainController.setSelectedMusic(music);
        this.mainController.getMainView().showConfigEditor();
    }

    public void loadMeta() {
       this.musicList = Utilities.readMusicMeta();
       if (this.musicTable.getItems()==null) this.musicTable.getItems().addAll(musicList);
       else {
           this.musicTable.getItems().clear();
           this.musicTable.getItems().addAll(musicList);
       }
    }

    public void removeMeta() {
        MusicInfo selected = musicTable.getSelectionModel().getSelectedItem();
        try{
            File inputFile = new File("Metadata/" + selected.getGenre() + ".meta");
            File tempFile = new File("Metadata/" + "temp" + selected.getGenre() + ".meta");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String lineToRemove = selected.toString();
            String currentLine;
            while((currentLine = reader.readLine()) != null) {
                if(currentLine.trim().equals(lineToRemove)) continue;
                writer.write(currentLine + "\n");

            }
            writer.close();
            reader.close();
            boolean end = tempFile.renameTo(inputFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            musicTable.getItems().remove(selected);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        artistCol.setCellValueFactory(new PropertyValueFactory<>("artist"));
        albumCol.setCellValueFactory(new PropertyValueFactory<>("album"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
    }

    public void goBack() {
        this.mainController.getMainView().showAgentPrototype();
    }
}
