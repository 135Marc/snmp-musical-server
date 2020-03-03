import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AgentPrototype implements Initializable {

    private MainView main;
    private MainController mainController;

    @FXML
    private FontAwesomeIconView playerIcon;
    @FXML
    private FontAwesomeIconView configIcon;
    @FXML
    private FontAwesomeIconView serverIcon;
    @FXML
    private FontAwesomeIconView submitIcon;
    @FXML
    private Button playerBtn;
    @FXML
    private Button configBtn;
    @FXML
    private Button serverBtn;
    @FXML
    private Button submitBtn;

    public void init(MainView mv) {
        this.main = mv;
    }

    public void setMainController(MainController mc) {
        this.mainController=mc;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void hooverPlayer() {
        playerIcon.setFill(Paint.valueOf("#0d8be0"));
        playerBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverConfig() {
        configIcon.setFill(Paint.valueOf("#0d8be0"));
        configBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverSubmit() {
        submitIcon.setFill(Paint.valueOf("#0d8be0"));
        submitBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverServer() {
        serverIcon.setFill(Paint.valueOf("#0d8be0"));
        serverBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void exitPlayer() {
        playerIcon.setFill(Paint.valueOf("#000000"));
        playerBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitConfig() {
        configIcon.setFill(Paint.valueOf("#000000"));
        configBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitSubmit() {
        submitIcon.setFill(Paint.valueOf("#000000"));
        submitBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitServer() {
        serverIcon.setFill(Paint.valueOf("#000000"));
        serverBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void showAudioPlayer() {
        this.mainController.getMainView().showDisplay();
    }

    public void showConfigManager() {
        this.mainController.getMainView().showConfigManager();
    }

    public void showAgentMusicServer() { this.mainController.getMainView().showAgentMusicServer();}

    public void loadMIBValues() throws Exception{
        List<MusicInfo> musicList = Utilities.readMusicMeta();
        Agent a = new Agent();
        a.setMusicList(musicList);
        a.setDEFAULT_PORT(6666);
        AgentUtilities agentStarter = new AgentUtilities();
        agentStarter.init(a);
        this.mainController.setMainAgent(a);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Valores Carregados na MIB");
        alert.setHeaderText("MIB preenchida com sucesso!");
        alert.setContentText("Dados da coleção musical disponiveis!");
        alert.showAndWait();
    }



}
