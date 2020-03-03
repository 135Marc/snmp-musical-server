import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientSettingsEditor implements Initializable {

    @FXML
    private TextField refreshField;
    @FXML
    private Label lastLabel;
    @FXML
    private Button returnBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private FontAwesomeIconView returnIcon;
    @FXML
    private FontAwesomeIconView saveIcon;


    private MainView mv;
    private MainController mc;

    public void init(MainView main) {
        this.mv=main;
    }

    public void setMainController(MainController mc) {this.mc=mc;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void loadRefresh() {

        this.mc.getMainAgent().asyncWalk(new OID("1.3.6.1.3.2020.1.1"));
        List<String> results = this.mc.getMainAgent().getResultList();
        this.refreshField.setText(results.get(0));
        this.mc.getMainAgent().clearResults();
        this.mc.getMainAgent().asyncWalk(new OID("1.3.6.1.3.2020.1.2"));
        results = this.mc.getMainAgent().getResultList();
        this.lastLabel.setText(results.get(0));
        this.mc.getMainAgent().clearResults();
    }

    public void updateRefresh() {
        String input = refreshField.getText();
        this.mc.getMainAgentUtilities().updateRefresh(input);
        System.out.println("UPDATE DEU CERTO!!");
    }

    public void goBack() {
        this.mc.getMainView().showClientOptionsDisplay();
    }


}
