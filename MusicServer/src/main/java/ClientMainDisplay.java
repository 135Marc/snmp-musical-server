import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMainDisplay implements Initializable {


    @FXML
    private Button addBtn;
    @FXML
    private Button acessBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private FontAwesomeIconView addIcon;
    @FXML
    private FontAwesomeIconView acessIcon;
    @FXML
    private FontAwesomeIconView exitIcon;
    @FXML
    private TableView<Agent> agentTableView;

    private MainView main;
    private MainController mainController;

    public void init(MainView mv) {
        this.main = mv;
    }

    public void setMainController(MainController mc) {
        this.mainController=mc;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<Agent,String> nameCol = new TableColumn<>("Address");
        nameCol.setPrefWidth(250.0);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("ip"));
        TableColumn<Agent,Integer> artistCol = new TableColumn<>("Port");
        artistCol.setCellValueFactory(new PropertyValueFactory<>("port"));
        artistCol.setPrefWidth(175.0);
        agentTableView.getColumns().addAll(nameCol,artistCol);
    }

    public void hooverAdd() {
        addBtn.setTextFill(Paint.valueOf("#0d8be0"));
        addIcon.setFill(Paint.valueOf("#0d8be0"));
    }

    public void hooverAcess() {
        acessBtn.setTextFill(Paint.valueOf("#45ef07"));
        acessIcon.setFill(Paint.valueOf("#45ef07"));
    }

    public void hooverExit() {
        exitBtn.setTextFill(Paint.valueOf("#c10911"));
        exitIcon.setFill(Paint.valueOf("#c10911"));
    }

    public void exitAdd() {
        addBtn.setTextFill(Paint.valueOf("#000000"));
        addIcon.setFill(Paint.valueOf("#000000"));
    }

    public void exitAcess() {
        acessBtn.setTextFill(Paint.valueOf("#000000"));
        acessIcon.setFill(Paint.valueOf("#000000"));
    }

    public void exitClose() {
        exitBtn.setTextFill(Paint.valueOf("#000000"));
        exitIcon.setFill(Paint.valueOf("#000000"));
    }

    public void close() {
        System.exit(0);
    }

    public void acessAgent() {
        this.mainController.getMainView().showClientOptionsDisplay();
    }

    public void addAgent() { this.mainController.getMainView().showAgentAddDisplay();}

    public void loadAgents() {
        if (this.mainController.getManagedAgents()!=null) agentTableView.getItems().addAll(this.mainController.getManagedAgents());
    }


}
