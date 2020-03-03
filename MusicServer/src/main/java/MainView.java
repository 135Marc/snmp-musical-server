import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class MainView extends Application {
    @FXML
    private Stage window;

    private MainController mc;

    public MainView() {
        this.window = new Stage();
    }


    @Override
    public void start(Stage stage) throws Exception {
        mc = new MainController(this);
        showClientMainDisplay();
    }

    public void showDisplay() {
        try {
            File f = new File("src/main/java/audioPlayer.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(f.toURL());
            Pane view = loader.load();
            Scene nova = new Scene(view,555,221);
            window.setScene(nova);
            AudioPlayer ap = loader.getController();
            ap.init(this);
            ap.setMainController(mc);
            mc.setAudioPlayer(ap);
            window.setTitle("Audio Player");
            window.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void showClientMainDisplay() {
        try {
            File f = new File("src/main/java/clientMainDisplay.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(f.toURL());
            Pane view = loader.load();
            Scene nova = new Scene(view,482,330);
            window.setScene(nova);
            ClientMainDisplay cmd = loader.getController();
            cmd.init(this);
            cmd.setMainController(mc);
            mc.setClientMainDisplay(cmd);
            window.setTitle("SNMP Music Client");
            window.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public void showConfigAdder() {
        try{
            File f = new File("src/main/java/configAdder.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(f.toURL());
            Pane view = loader.load();
            Scene nova = new Scene(view,421,333);
            window.setScene(nova);
            ConfigAdder ca = loader.getController();
            ca.init(this);
            ca.setMainController(mc);
            mc.setConfigAdder(ca);
            window.setTitle("Collection Adder");
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showConfigEditor() {
        try{
            File f = new File("src/main/java/configEditor.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(f.toURL());
            Pane view = loader.load();
            Scene nova = new Scene(view,429,302);
            window.setScene(nova);
            ConfigEditor ce = loader.getController();
            ce.init(this);
            ce.setMainController(mc);
            ce.setSelectedMusic(this.mc.getSelectedMusic());
            mc.setConfigEditor(ce);
            window.setTitle("Collection Editor");
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showConfigManager() {
        try{
            File f = new File("src/main/java/configManager.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(f.toURL());
            Pane view = loader.load();
            Scene nova = new Scene(view,773,472);
            window.setScene(nova);
            ConfigManager cm = loader.getController();
            cm.init(this);
            cm.setMainController(mc);
            mc.setConfigManager(cm);
            window.setTitle("Collection Manager");
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAgentPrototype() {
        try {
            File f = new File("src/main/java/agentPrototype.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(f.toURL());
            Pane view = loader.load();
            Scene nova = new Scene(view, 445, 269);
            window.setScene(nova);
            AgentPrototype ap = loader.getController();
            ap.init(this);
            ap.setMainController(mc);
            mc.setAgentPrototype(ap);
            window.setTitle("Agent Prototype HUB");
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File showMusicChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Music File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Formats", "*.*"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3"),
                new FileChooser.ExtensionFilter("WAV", "*.wav")
            );
            File example = fileChooser.showOpenDialog(window);
            return example;
    }

    public void showAgentMusicServer() {
        try {
            File f = new File("src/main/java/agentMusicServer.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(f.toURL());
            Pane view = loader.load();
            Scene nova = new Scene(view, 848, 518);
            window.setScene(nova);
            AgentMusicServer ams = loader.getController();
            ams.init(this);
            ams.setMainController(mc);
            mc.setAgentMusicServer(ams);
            window.setTitle("Agent Music Server");
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showClientOptionsDisplay() {
        try {
            File f = new File("src/main/java/clientOptionsDisplay.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(f.toURL());
            Pane view = loader.load();
            Scene nova = new Scene(view, 474, 235);
            window.setScene(nova);
            ClientOptionsDisplay cod = loader.getController();
            cod.init(this);
            cod.setMainController(mc);
            mc.setClientOptionsDisplay(cod);
            window.setTitle("Client Interface Chooser");
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAgentAddDisplay() {
        try {
            File f = new File("src/main/java/agentAddDisplay.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(f.toURL());
            Pane view = loader.load();
            Scene nova = new Scene(view, 541, 445);
            window.setScene(nova);
            AgentAddDisplay aad = loader.getController();
            aad.init(this);
            aad.setMainController(mc);
            mc.setAgentAddDisplay(aad);
            window.setTitle("Add New Agent");
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showClientMusicServer() {
        try {
            File f = new File("src/main/java/clientMusicServer.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(f.toURL());
            Pane view = loader.load();
            Scene nova = new Scene(view, 883, 518);
            window.setScene(nova);
            ClientMusicServer cms = loader.getController();
            cms.init(this);
            cms.setMainController(mc);
            mc.setClientMusicServer(cms);
            window.setTitle("Client Music Server");
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showClientAudioPlayer() {
        try {
            File f = new File("src/main/java/clientAudioPlayer.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(f.toURL());
            Pane view = loader.load();
            Scene nova = new Scene(view, 555, 223);
            window.setScene(nova);
            ClientAudioPlayer cap = loader.getController();
            cap.init(this);
            cap.setMainController(mc);
            mc.setClientAudioPlayer(cap);
            window.setTitle("Client Audio Player");
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showClientStatisticsDisplay() {
        try {
            File f = new File("src/main/java/clientStatisticDisplay.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(f.toURL());
            Pane view = loader.load();
            Scene nova = new Scene(view, 600, 400);
            window.setScene(nova);
            ClientStatisticDisplay csd = loader.getController();
            csd.init(this);
            csd.setMainController(mc);
            mc.setClientStatisticDisplay(csd);
            window.setTitle("Music Statistics");
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showClientSettingsEditor() {
        try {
            File f = new File("src/main/java/clientSettingsEditor.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(f.toURL());
            Pane view = loader.load();
            Scene nova = new Scene(view, 390, 284);
            window.setScene(nova);
            ClientSettingsEditor cse = loader.getController();
            cse.init(this);
            cse.setMainController(mc);
            mc.setClientSettingsEditor(cse);
            window.setTitle("Set Definition Values");
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        launch(args);
    }
}
