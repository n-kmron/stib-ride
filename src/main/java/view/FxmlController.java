package view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import model.Language;
import model.MetroTime;
import model.Node;
import model.StibRide;
import org.controlsfx.control.SearchableComboBox;
import repository.StationRepository;
import repository.dto.StationDto;
import utils.Observer;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FxmlController implements Observer {

    private StibRide stibRide;

    private String s;

    private String d;

    private Language language;

    private Node source;

    private Node destination;
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

    @FXML
    private RadioMenuItem frOpt;

    @FXML
    private RadioMenuItem nlOpt;

    @FXML
    private TextField first_arrival;

    @FXML
    private TextField second_arrival;


    private void setComboBoxValues() {
        //give list with all the stations' name
        StationRepository stationRepository = new StationRepository(language);
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
        destinationStation.setItems(destinationList);
        addListenerOnComboBox();
    }

    /**
     * Reset the listeners on combo boxes to refresh with the language settings
     */
    private void removeListenerOnComboBox() {
        ChangeListener<String> listenerSource = (observable, oldValue, newValue) -> {
            try {
                s = newValue;
                source = stibRide.findNode(stibRide.findNode(s).getId());
            } catch (Exception e) {
            }
        };
        ChangeListener<String> listenerDest = (observable, oldValue, newValue) -> {
            try {
                d = newValue;
                destination = stibRide.findNode(stibRide.findNode(d).getId());
            } catch (Exception e) {
            }
        };
        sourceStation.getSelectionModel().selectedItemProperty().removeListener(listenerSource);
        destinationStation.getSelectionModel().selectedItemProperty().removeListener(listenerDest);
    }
    /**
     * Add a listener on the combobox to recover at any time the name of the current source and destination stations
     */
    private void addListenerOnComboBox() {
        removeListenerOnComboBox();
        sourceStation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                s = newValue;
                source = stibRide.findNode(stibRide.findNode(s).getId());
            } catch (Exception e) {
            }
        });

        destinationStation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                d = newValue;
                destination = stibRide.findNode(stibRide.findNode(d).getId());
            } catch (Exception e) {
            }
        });
    }

    public void initialize() {
        stibRide = new StibRide();
        stibRide.addObserver(this);

        source = stibRide.findNode("DE BROUCKERE");
        destination = stibRide.findNode("DE BROUCKERE");

        //initialize the tableview
        c1.setCellValueFactory(new PropertyValueFactory<>("name"));
        c2.setCellValueFactory(new PropertyValueFactory<>("line"));

        setComboBoxValues();
        sourceStation.setValue("DE BROUCKERE");
        destinationStation.setValue("DE BROUCKERE");

        language = Language.FR;
    }


    /**
     * Change the active path of the app with a path between values in combobox
     */
    @FXML
    public void handleFind(ActionEvent e) {
        source = stibRide.findNode(source.getId());
        destination = stibRide.findNode(destination.getId());
        stibRide.setActivePath(source, destination);
        stibRide.setNextArrival(source);
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
            controller.setStations(source.getId(), destination.getId());
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

    /**
     * Select a language for the app
     */
    @FXML
    public void handleChangeLanguage(ActionEvent e) throws IOException {
        if(frOpt.isSelected()) {
            this.language = Language.FR;
            stibRide.changeLanguage(Language.FR);
        }
        else {
            this.language = Language.NL;
            stibRide.changeLanguage(Language.NL);
        }
        stibRide.setActivePath(stibRide.findNode(source.getId()), stibRide.findNode(destination.getId()));
    }

    @Override
    public void update() {
        Platform.runLater(() -> {
            setComboBoxValues();
            List<Node> path = stibRide.getActivePath();
            if(path != null) {
                tableview.getItems().clear();
                for(Node node : path) {
                    LineViewer line = new LineViewer(node.getName(), node.getLines());
                    tableview.getItems().add(line);
                }
                MetroTime[] next = stibRide.getNextArrivals();

                String first = next[0].getDuration() + "min - [" + next[0].getLine() + "]";
                first_arrival.setText(first);
                if(next.length > 1) {
                    String second = next[1].getDuration() + "min - [" + next[1].getLine() + "]";
                    second_arrival.setText(second);
                } else {
                    second_arrival.setText("/");
                }
            } else {
                System.out.println("the path is null");
            }
        });
    }

    @Override
    public void update(Object o) {
    }
}
