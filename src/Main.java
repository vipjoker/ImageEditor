
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;


public class Main extends Application{
    private ScrollPane pane;
    private VBox hBox;





    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        URL resource = getClass().getResource("resources/converter.fxml");
        Parent load = FXMLLoader.load(resource);

        pane = new ScrollPane();
        pane.setBackground(new Background(new BackgroundFill(Color.BLUE,null,null)));
        pane.setPrefSize(600,600);
        Scene scene = new Scene(load);
        hBox = new VBox();
        pane.setContent(hBox);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

}
