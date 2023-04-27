package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Node;
import model.StibRide;

import java.util.ArrayList;
import java.util.List;

public class SaveShortcutFxml {

    private StibRide stibRide;

    private List<Integer> stations;

    private Stage stage;

    public void setStibRide(StibRide stibRide) {
        this.stibRide = stibRide;
    }

    public void setStations(int source, int destination) {
        this.stations = new ArrayList<>();
        stations.add(source);
        stations.add(destination);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private TextField shortcutName;

    @FXML
    public void handleSave(ActionEvent e) {
        String name = shortcutName.getText();
        if(name.length() > 0) {
            stibRide.saveShortcut(name, stations);
            stage.close();
        }
    }
}
