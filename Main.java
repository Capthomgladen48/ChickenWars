package sample;

import javafx.application.Application;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class Main extends Application {

    private ChickenWarsController chickenWarsController;
    private ChickenWarsGui chickenWarsGui;
    private Stage window;


    public void init (){
        chickenWarsController = new ChickenWarsController();
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        chickenWarsGui = new ChickenWarsGui(chickenWarsController);
        window.setTitle("Chicken Wars");
        window.setScene(new Scene(chickenWarsGui.getView(), 1850, 950));
        Image image = new Image(Paths.get("image/sniper.png").toUri().toString());
        window.getScene().getRoot().setCursor(new ImageCursor(image));
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
