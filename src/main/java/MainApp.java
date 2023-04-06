import config.ConfigManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource("/fxml/stib_ride.fxml"));
            String title = ConfigManager.getInstance().getProperties("app.author") + " - " +
                    ConfigManager.getInstance().getProperties("app.name") + " (" +
                    ConfigManager.getInstance().getProperties("app.version") + ")";
            stage.setTitle(title);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        try {
            ConfigManager.getInstance().load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        launch(args);
    }

    // FIXME : gérer les bugs relatifs aux combobox
    // FIXME : faire un plus bel affichage pour les load et save shortcut
    // TODO : add the next metro
    // TODO : stations en nééerlandais
}
