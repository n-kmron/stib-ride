package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import model.Node;
import model.StibRide;
import org.controlsfx.control.SearchableComboBox;
import repository.StationRepository;
import repository.dto.StationDto;
import utils.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FxmlController implements Observer {

    private StibRide stibRide;

    private String s;

    private String d;
    @FXML
    private TableView tableview;

    @FXML
    private TableColumn<LineViewer, String> c1;

    @FXML
    private TableColumn<LineViewer, String> c2;


    private ObservableList<String> sourceList;
    private ObservableList<String> destinationList;

    @FXML
    private SearchableComboBox<String> sourceStation = new SearchableComboBox<>();

    @FXML
    private SearchableComboBox<String> destinationStation = new SearchableComboBox<>();


    public void initialize() {
        stibRide = new StibRide();
        stibRide.addObserver(this);

        //initialize the tableview
        c1.setCellValueFactory(new PropertyValueFactory<>("name"));
        c2.setCellValueFactory(new PropertyValueFactory<>("line"));

        //give list with all the stations' name
        StationRepository stationRepository = new StationRepository();
        List<StationDto> stations = stationRepository.getAll();
        List<String> stringifyStations = new ArrayList<>();
        for(StationDto station : stations) {
            stringifyStations.add(station.getName());
        }

        //initialize the searchable combobox with stations' name
        sourceList = FXCollections.observableArrayList();
        sourceList.addAll(stringifyStations);
        destinationList = FXCollections.observableArrayList();
        destinationList.addAll(stringifyStations);
        sourceStation.setItems(sourceList);
        sourceStation.setValue("DE BROUCKERE");
        destinationStation.setItems(destinationList);
        destinationStation.setValue("DE BROUCKERE");

        //add a listener on the combobox to recover at any time the name of the current source and destination stations
        sourceStation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            s = newValue;
        });

        destinationStation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            d = newValue;
        });
    }


    /**
     * Change the active path of the app with a path between values in combobox
     */
    @FXML
    public void handleFind(ActionEvent e) {
        try {
            if (s != null && d != null) {
                String sourceAsString = s;
                String destAsString = d;
                Node source = stibRide.findNode(sourceAsString);
                Node destination = stibRide.findNode(destAsString);
                stibRide.setActivePath(source, destination);
            }
        } catch(Exception ex) {
            System.out.println("The stations given are wrong");;
        }
    }

    /**
     * exit the app
     */
    @FXML
    public void handleMenuExit(ActionEvent e) {
        stibRide.exit();
    }

    /**
     * Launch a new stage to save a shortcut
     * @throws Alert alert if the path is not correct
     */
    @FXML
    public void handleMenuSaveShortcut(ActionEvent e) throws IOException {
        if(stibRide.getActivePath().size() >= 2) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/save_shortcut.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Cameron - Save Shortcut");
            Scene scene = new Scene(root);
            stage.setScene(scene);

            SaveShortcutFxml controller = loader.getController();
            controller.setStibRide(stibRide);
            controller.setStations(s, d);
            controller.setStage(stage);

            stage.showAndWait();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("The shortcut was successfully added!");

            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("The shortcut has to contains at least two stations!");

            alert.showAndWait();
        }
    }

    /**
     * Launch a new stage to load a shortcut
     */
    @FXML
    public void handleMenuLoadShortcut(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/load_shortcut.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Cameron - Load Shortcut");
        Scene scene = new Scene(root);
        stage.setScene(scene);

        LoadShortcutFxml controller = loader.getController();
        controller.setStibRide(stibRide);
        controller.setStage(stage);
        controller.initializer();

        stage.showAndWait();
    }

    /**
     * Launch a new stage to display the help's menu
     */
    @FXML
    public void handleMenuHelp(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/help.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Cameron - Help Menu");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @Override
    public void update() {
        List<Node> path = stibRide.getActivePath();
        if(path != null) {
            tableview.getItems().clear();
            for(Node node : path) {
                LineViewer line = new LineViewer(node.getName(), node.getLines());
                tableview.getItems().add(line);
            }
        } else {
            System.out.println("the path is null");
        }    }
}
