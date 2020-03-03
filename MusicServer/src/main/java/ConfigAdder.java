import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ConfigAdder implements Initializable {

    private MainView main;
    private MainController mainController;

    @FXML
    private FontAwesomeIconView openIcon;
    @FXML
    private FontAwesomeIconView addIcon;
    @FXML
    private FontAwesomeIconView returnIcon;
    @FXML
    private Button openBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button returnBtn;
    @FXML
    private TextField nameArea;
    @FXML
    private TextField artistArea;
    @FXML
    private TextField genreArea;
    @FXML
    private TextField albumArea;
    @FXML
    private TextField yearArea;
    @FXML
    private Label pathLabel;
    @FXML
    private Label durationArea;

    private File musicFile;
    private AudioPlayerComponent audioPlayer;


    public void init(MainView mv) {
        this.main = mv;
    }

    public void setMainController(MainController mc) {
        this.mainController=mc;
    }

    public void hooverOpen() {
        openIcon.setFill(Paint.valueOf("#0d8be0"));
        openBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverAdd() {
        addIcon.setFill(Paint.valueOf("#0d8be0"));
        addBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverReturn() {
        returnIcon.setFill(Paint.valueOf("#0d8be0"));
        returnBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void exitOpen() {
        openIcon.setFill(Paint.valueOf("#000000"));
        openBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitAdd() {
        addIcon.setFill(Paint.valueOf("#000000"));
        addBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitReturn() {
        returnIcon.setFill(Paint.valueOf("#000000"));
        returnBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void writeMusicMeta() {
        createRepos();
        MusicInfo mi = new MusicInfo(nameArea.getText(),artistArea.getText(),albumArea.getText(),genreArea.getText(),yearArea.getText(),durationArea.getText(), musicFile.getAbsolutePath());
        Utilities.writeMusicMeta(mi);
    }

    public void goBack() {
        this.mainController.getMainView().showConfigManager();
    }


    public void selectMusic() {
        File f = this.mainController.getMainView().showMusicChooser();
        pathLabel.setText(f.getName());
        musicFile=f;
        audioPlayer = new AudioPlayerComponent();
        audioPlayer.mediaPlayer().media().start(musicFile.getAbsolutePath());
        audioPlayer.mediaPlayer().controls().pause();
        float length = Math.round(audioPlayer.mediaPlayer().status().length()/1000);
        durationArea.setText(String.valueOf(length));
    }

    public void createRepos() {
        File repo = new File(System.getProperty("user.home") + "/MusicRepo");
        String name = nameArea.getText();
        String artist = artistArea.getText();
        String album = albumArea.getText();
        String genre = genreArea.getText();
        String year = yearArea.getText();
        boolean created;
        if (!repo.exists()) {
            created = repo.mkdir();
            if (created) System.out.println("Repositório Criado!!");
        }
        else {
            File genreFile = new File (System.getProperty("user.home") + "/MusicRepo/" + genre);
            File album_folder = new File(System.getProperty("user.home") + "/MusicRepo/" + genre + "/" + year + "/" + album);
            File yyFile = new File(System.getProperty("user.home") + "/MusicRepo/" + genre + "/" + year);
            if (!genreFile.exists()) {
                boolean insertion = insertNewMusicGenre(genreFile,album_folder,yyFile,artist,name,genre,album,year);
            }

            else {
                boolean insertion = insertInGenre(album_folder,yyFile,artist,name,genre,album,year);
            }

        }
    }

    public boolean insertNewMusicGenre(File genre_folder , File album_folder, File year_folder,String artist, String name,String genre,String album,String year) {
        boolean result = false;
        boolean genre_folder_created = genre_folder.mkdir();
        if (genre_folder_created) {
                boolean y_folder_created = year_folder.mkdir();
                if (y_folder_created) {
                    boolean album_folder_created = album_folder.mkdir();
                    if (album_folder_created) {
                        File artist_folder = new File(System.getProperty("user.home") + "/MusicRepo/" + genre + "/" + year + "/" + album + "/" + artist);
                        boolean artist_folder_created = artist_folder.mkdir();
                        if (artist_folder_created) {
                            File endFile;
                            if (musicFile.getName().contains(".mp3")) {
                                endFile = new File(System.getProperty("user.home") + "/MusicRepo/" + genre + "/" + year + "/" + album + "/" + artist + "/" + name + ".mp3");
                                result = musicFile.renameTo(endFile);
                                musicFile = endFile;
                            }
                            else if (musicFile.getName().contains(".wav")) {
                                endFile=new File(System.getProperty("user.home") + "/MusicRepo/" + genre + "/" + year + "/" + album + "/" + artist + "/" + name + ".wav");
                                result = musicFile.renameTo(endFile);
                                musicFile = endFile;
                            }
                        }
                    }
                }
            }

        return result;
    }

    public boolean insertInGenre(File album_folder, File year_folder,String artist, String name,String genre,String album,String year) {
        boolean result=false;
        if (!year_folder.exists()) {
            boolean year_folder_created = year_folder.mkdir();
            if (year_folder_created) {
                boolean album_folder_created = album_folder.mkdir();
                if (album_folder_created) {
                    File artist_folder = new File(System.getProperty("user.home") + "/MusicRepo/" + genre + "/" + year + "/" + album + "/" + artist);
                    boolean artist_folder_created = artist_folder.mkdir();
                    if (artist_folder_created) {
                        File endFile = new File(System.getProperty("user.home") + "/MusicRepo/" + genre + "/" + year + "/" + album + "/" + artist + "/" + name + ".mp3");
                        result = musicFile.renameTo(endFile);
                        musicFile = endFile;
                    }
                }
            }
        }

        else {
            boolean exists = false;
                for (String albumString : year_folder.list()) {
                    if (albumString.equals(album)) {
                        exists = true;
                        break;
                    }
                }
            if (exists) {
                boolean exists_artist = false;
                 for (String artistString : album_folder.list()) {
                     if (artistString.equals(artist)) {
                         exists_artist = true;
                         break;
                     }
                 }
                if (exists_artist) {
                    File endFile = new File(System.getProperty("user.home") + "/MusicRepo/" + genre + "/" + year + "/" + album + "/" + artist + "/" + name + ".mp3");
                    result = musicFile.renameTo(endFile);
                    musicFile = endFile;
                }
                else {
                    File artist_folder = new File(System.getProperty("user.home") + "/MusicRepo/" + genre + "/" + year + "/" + album + "/" + artist);
                    boolean artist_folder_created = artist_folder.mkdir();
                    if (artist_folder_created) {
                        File endFile = new File(System.getProperty("user.home") + "/MusicRepo/" + genre + "/" + year + "/" + album + "/" + artist + "/" + name + ".mp3");
                        result = musicFile.renameTo(endFile);
                        musicFile = endFile;
                    }
                }
            }

            else {
                boolean album_folder_created = album_folder.mkdir();
                if (album_folder_created) {
                    File artist_folder = new File(System.getProperty("user.home") + "/MusicRepo/" + genre + "/" + year + "/" + album + "/" + artist);
                    boolean artist_folder_created = artist_folder.mkdir();
                    if (artist_folder_created) {
                        File endFile = new File(System.getProperty("user.home") + "/MusicRepo/" + genre + "/" + year + "/" + album + "/" + artist + "/" + name + ".mp3");
                        result = musicFile.renameTo(endFile);
                        musicFile = endFile;
                    }
                }
            }

        }
        return result;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
