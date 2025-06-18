package bioskopkuy.app;

import bioskopkuy.controller.BioskopController;
import bioskopkuy.model.BioskopModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        BioskopModel model = new BioskopModel();
        BioskopController controller = new BioskopController(model, primaryStage);
        controller.mulaiAplikasi();
    }

    public static void main(String[] args) {
        launch(args);
    }
}