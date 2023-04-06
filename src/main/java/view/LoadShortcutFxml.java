package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Shortcut;
import model.StibRide;
import repository.ShortcutRepository;
import repository.dto.ShortcutDto;

import java.util.List;

public class LoadShortcutFxml {

        private StibRide stibRide;

        private Stage stage;

        public void setStibRide(StibRide stibRide) {
            this.stibRide = stibRide;
        }

        public void setStage(Stage stage) {
            this.stage = stage;
        }

        @FXML
        private VBox container;

        public void initializer() {
            ShortcutRepository shortcutRepository = new ShortcutRepository();
            List<ShortcutDto> shortcuts = shortcutRepository.getAll();
            if(shortcuts.size() == 0) {
                Label noShortcut = new Label("There is no shortcut actually");
                container.getChildren().add(noShortcut);
            } else {
                for(ShortcutDto shortcut : shortcuts) {
                    HBox line = new HBox();
                    line.setSpacing(10);
                    line.setAlignment(Pos.CENTER);
                    line.setPadding(new Insets(10));
                    Label name = new Label(shortcut.getKey());
                    Button loadBtn = new Button("Load");
                    loadBtn.getProperties().put("name", shortcut.getKey());
                    loadBtn.setOnAction(event -> handleLoad((String) loadBtn.getProperties().get("name")));
                    Button deleteBtn = new Button("Delete");
                    deleteBtn.getProperties().put("name", shortcut.getKey());
                    deleteBtn.setOnAction(event -> handleDelete((String) loadBtn.getProperties().get("name")));
                    line.getChildren().addAll(name, loadBtn, deleteBtn);
                    container.getChildren().add(line);
                }
            }
        }

    public void handleLoad(String name) {
            stibRide.loadShortcut(name);
            stage.close();
    }

    public void handleDelete(String name) {
            stibRide.removeShortcut(name);
            container.getChildren().clear();
            initializer();
    }
}
