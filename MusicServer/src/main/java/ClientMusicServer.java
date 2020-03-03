import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import org.snmp4j.smi.OID;

import javax.naming.ldap.HasControls;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ClientMusicServer implements Initializable {

    private MainView main;
    private MainController mainController;
    private Map<Integer,String> albumMap = new HashMap<>();
    private Map<Integer,String> genreMap = new HashMap<>();
    private Map<Integer,String> artistMap = new HashMap<>();
    private Map<Integer,MusicInfo> musicMap = new HashMap<>();

    @FXML
    private Button loadBtn;
    @FXML
    private Button returnBtn;
    @FXML
    private Button findBtn;
    @FXML
    private FontAwesomeIconView loadIcon;
    @FXML
    private FontAwesomeIconView returnIcon;
    @FXML
    private FontAwesomeIconView findIcon;
    @FXML
    private TableView<MusicInfo> musicInfoTable;
    @FXML
    private ChoiceBox<String> filterBox;
    @FXML
    private TextField searchField;

    public void init(MainView mv) {
        this.main = mv;
    }

    public void setMainController(MainController mc) {
        this.mainController=mc;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<MusicInfo,String> nameCol = new TableColumn<>("Name");
        nameCol.setPrefWidth(140.0);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<MusicInfo,String> artistCol = new TableColumn<>("Artist");
        artistCol.setCellValueFactory(new PropertyValueFactory<>("artist"));
        artistCol.setPrefWidth(129.0);
        TableColumn<MusicInfo,String> albumCol = new TableColumn<>("Album");
        albumCol.setPrefWidth(161.0);
        albumCol.setCellValueFactory(new PropertyValueFactory<>("album"));
        TableColumn<MusicInfo,String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreCol.setPrefWidth(184.0);
        TableColumn<MusicInfo,String> yearCol = new TableColumn<>("Year");
        yearCol.setPrefWidth(88.0);
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        TableColumn<MusicInfo,String> durationCol = new TableColumn<>("Duration(s)");
        durationCol.setPrefWidth(100.0);
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        musicInfoTable.getColumns().addAll(nameCol,artistCol,albumCol,genreCol,yearCol,durationCol);
        filterBox.getItems().add("Name");
        filterBox.getItems().add("Artist");
        filterBox.getItems().add("Album");
        filterBox.getItems().add("Genre");
    }

    public void hooverLoad() {
        loadIcon.setFill(Paint.valueOf("#0d8be0"));
        loadBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverFind() {
        findIcon.setFill(Paint.valueOf("#0d8be0"));
        findBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverReturn() {
        returnIcon.setFill(Paint.valueOf("#0d8be0"));
        returnBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void exitReturn() {
        returnIcon.setFill(Paint.valueOf("#000000"));
        returnBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitLoad() {
        loadIcon.setFill(Paint.valueOf("#000000"));
        loadBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitFind() {
        findIcon.setFill(Paint.valueOf("#000000"));
        findBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void goBack() {
        this.mainController.getMainView().showClientOptionsDisplay();
    }

    public void loadMusics() {
       loadMaps();
       generateMusicInfo();
       musicInfoTable.getItems().addAll(musicMap.values());
    }

    public void loadMaps() {
        this.mainController.getMainAgent().asyncWalk(new OID("1.3.6.1.3.2020.1.4"));
        List<String> results = this.mainController.getMainAgent().getResultList();
        results.remove(results.size()-1);
        Utilities.populate_albumMap(results,albumMap);
        this.mainController.getMainAgent().clearResults();
        this.mainController.getMainAgent().asyncWalk(new OID("1.3.6.1.3.2020.1.5"));
        results = this.mainController.getMainAgent().getResultList();
        results.remove(results.size()-1);
        Utilities.populate_genreMap(results,genreMap);
        this.mainController.getMainAgent().clearResults();
        this.mainController.getMainAgent().asyncWalk(new OID("1.3.6.1.3.2020.1.6"));
        results.remove(results.size()-1);
        Utilities.populate_artistMap(results,artistMap);
        this.mainController.getMainAgent().clearResults();
    }

    public void generateMusicInfo() {
        // Parse music IDs
        this.mainController.getMainAgent().asyncBulkGet(new OID("1.3.6.1.3.2020.1.3.1.1"));
        List<String> results = this.mainController.getMainAgent().getResultList();
        for(int i=0;i<results.size();i++) {
            musicMap.put(i,new MusicInfo());
        }
        this.mainController.getMainAgent().clearResults();
        // Parse music Names
        this.mainController.getMainAgent().asyncBulkGet(new OID("1.3.6.1.3.2020.1.3.1.2"));
        results = this.mainController.getMainAgent().getResultList();
        for (int i=0;i<musicMap.size();i++) {
            musicMap.get(i).setName(results.get(i));
        }
        this.mainController.getMainAgent().clearResults();
        // Parse music Artists
        this.mainController.getMainAgent().asyncBulkGet(new OID("1.3.6.1.3.2020.1.3.1.3"));
        results = this.mainController.getMainAgent().getResultList();
        for (int i=0;i<musicMap.size();i++) {
            String artist = artistMap.get(Integer.parseInt(results.get(i)));
            musicMap.get(i).setArtist(artist);
        }
        this.mainController.getMainAgent().clearResults();
        // Parse music Albums
        this.mainController.getMainAgent().asyncBulkGet(new OID("1.3.6.1.3.2020.1.3.1.4"));
        results = this.mainController.getMainAgent().getResultList();
        for (int i=0;i<musicMap.size();i++) {
            String album = albumMap.get(Integer.parseInt(results.get(i)));
            musicMap.get(i).setAlbum(album);
        }
        this.mainController.getMainAgent().clearResults();
        // Parse music year
        this.mainController.getMainAgent().asyncBulkGet(new OID("1.3.6.1.3.2020.1.3.1.5"));
        results = this.mainController.getMainAgent().getResultList();
        for (int i=0;i<musicMap.size();i++) {
            musicMap.get(i).setYear(results.get(i));
        }
        this.mainController.getMainAgent().clearResults();
        // Parse music Genre
        this.mainController.getMainAgent().asyncBulkGet(new OID("1.3.6.1.3.2020.1.3.1.6"));
        results = this.mainController.getMainAgent().getResultList();
        for (int i=0;i<musicMap.size();i++) {
            String genre = genreMap.get(Integer.parseInt(results.get(i)));
            musicMap.get(i).setGenre(genre);
        }
        this.mainController.getMainAgent().clearResults();
        // Parse music Duration
        this.mainController.getMainAgent().asyncBulkGet(new OID("1.3.6.1.3.2020.1.3.1.7"));
        results = this.mainController.getMainAgent().getResultList();
        for (int i=0;i<musicMap.size();i++) {
            musicMap.get(i).setDuration(results.get(i));
        }
        this.mainController.getMainAgent().clearResults();

    }

    public void searchInMap() {
        String input = searchField.getText();
        List<MusicInfo> filtered;
        if (filterBox.getSelectionModel().getSelectedItem().equals("Name")) {
           filtered = musicMap.values().stream().filter(mi -> mi.getName().contains(input)).collect(Collectors.toList());
        }
        else if (filterBox.getSelectionModel().getSelectedItem().equals("Artist")) {
            filtered = musicMap.values().stream().filter(mi -> mi.getArtist().contains(input)).collect(Collectors.toList());
        }
        else if (filterBox.getSelectionModel().getSelectedItem().equals("Album")) {
            filtered = musicMap.values().stream().filter(mi -> mi.getAlbum().contains(input)).collect(Collectors.toList());
        }
        else {
            filtered = musicMap.values().stream().filter(mi -> mi.getGenre().contains(input)).collect(Collectors.toList());
        }
        musicInfoTable.getItems().clear();
        musicInfoTable.getItems().addAll(filtered);
    }

    public void reloadTable() {
        musicInfoTable.getItems().clear();
        musicInfoTable.getItems().addAll(musicMap.values());
    }


}
