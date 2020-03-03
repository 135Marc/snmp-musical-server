import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientOptionsDisplay implements Initializable {

    private MainView main;
    private MainController mainController;

    @FXML
    private Button listBtn;
    @FXML
    private Button remoteBtn;
    @FXML
    private Button statsBtn;
    @FXML
    private Button defsBtn;
    @FXML
    private FontAwesomeIconView listIcon;
    @FXML
    private FontAwesomeIconView remoteIcon;
    @FXML
    private FontAwesomeIconView statsIcon;
    @FXML
    private FontAwesomeIconView defsIcon;

    public void init(MainView mv) {
        this.main = mv;
    }

    public void setMainController(MainController mc) {
        this.mainController=mc;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void hooverListen() {
        listIcon.setFill(Paint.valueOf("#0d8be0"));
        listBtn.setTextFill(Paint.valueOf("#0d8be0"));

    }

    public void hooverRemote() {
        remoteIcon.setFill(Paint.valueOf("#0d8be0"));
        remoteBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverStats() {
        statsIcon.setFill(Paint.valueOf("#0d8be0"));
        statsBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverDefs() {
        defsIcon.setFill(Paint.valueOf("#0d8be0"));
        defsBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }

    public void exitListen() {
        listIcon.setFill(Paint.valueOf("#000000"));
        listBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitRemote() {
        remoteIcon.setFill(Paint.valueOf("#000000"));
        remoteBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitStats() {
        statsIcon.setFill(Paint.valueOf("#000000"));
        statsBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitDefs() {
        defsIcon.setFill(Paint.valueOf("#000000"));
        defsBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void showLister() {
        this.mainController.getMainView().showClientMusicServer();
    }

    public void showRemoteListener() {
        this.mainController.getMainView().showClientAudioPlayer();
    }

    public void showStatistics() {
        this.mainController.getMainView().showClientStatisticsDisplay();
    }

    public void showDefinitionChanger() {
        this.mainController.getMainView().showClientSettingsEditor();

    }

    public void loadAgentMIB() throws Exception {
        if (this.mainController.getMainAgentUtilities()==null) {
            AgentUtilities agentStarter = new AgentUtilities();
            Agent a = this.mainController.getMainAgent();
            a.setMusicList(Utilities.readMusicMeta());
            agentStarter.init(a);
            this.mainController.setMainAgent(a);
            this.mainController.setMainAgentUtilities(agentStarter);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Valores Carregados na MIB do Agente");
            alert.setHeaderText("MIB do Agente preenchida com sucesso!");
            alert.setContentText("Dados da coleção musical disponiveis!");
            alert.showAndWait();
        }
    }


}
