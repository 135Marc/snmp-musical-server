import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;
import org.snmp4j.smi.OID;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientStatisticDisplay implements Initializable {

    CategoryAxis xAxis  = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    NumberAxis eixoX = new NumberAxis();
    NumberAxis eixoY = new NumberAxis();

    @FXML
    private ScatterChart bcn = new ScatterChart(xAxis,yAxis);
    @FXML
    private PieChart pieChart;
    @FXML
    private BarChart barChart =  new BarChart(xAxis,yAxis);
    @FXML
    private LineChart lineChart = new LineChart(eixoX,eixoY);
    @FXML
    private Button saveBtn;
    @FXML
    private Button returnBtn;
    @FXML
    private FontAwesomeIconView saveIcon;
    @FXML
    private FontAwesomeIconView returnIcon;

    private MainView mv;
    private MainController mc;

    public void init(MainView main) {
        this.mv=main;
    }

    public void setMainController(MainController mc) {this.mc=mc;}

    public void fillPieChart() {
        ObservableList<PieChart.Data> datalist = Utilities.generatePieMap(this.mc.getMainAgent().getMusicList());
        pieChart.setData(datalist);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void hooverSave() {
        saveIcon.setFill(Paint.valueOf("#0d8be0"));
        saveBtn.setTextFill(Paint.valueOf("#0d8be0"));

    }

    public void hooverReturn() {
        returnIcon.setFill(Paint.valueOf("#0d8be0"));
        returnBtn.setTextFill(Paint.valueOf("#0d8be0"));
    }


    public void exitReturn() {
        returnIcon.setFill(Paint.valueOf("#000000"));
        returnBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void exitSave() {
        saveIcon.setFill(Paint.valueOf("#000000"));
        saveBtn.setTextFill(Paint.valueOf("#000000"));
    }

    public void goBack() {
        this.mc.getMainView().showClientOptionsDisplay();
    }

}
