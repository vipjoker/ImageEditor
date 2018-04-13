
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

        setupPane(pane);
    }

    private void setupPane(ScrollPane pane) {
       pane.setOnDragOver(this::onDragOver);
       pane.setOnDragDropped(this::onDragDropped);
       pane.setOnDragEntered(this::onDragEntered);
       pane.setOnDragExited(this::onDragExited);
    }

    private void onDragExited(DragEvent dragEvent) {
        pane.setBackground(getBackground(Color.BLUE));
    }

    private void onDragEntered(DragEvent dragEvent) {
        pane.setBackground(getBackground(Color.RED));
    }

    private void onDragDropped(DragEvent dragEvent) {
        System.out.println("done");

        Dragboard dragboard = dragEvent.getDragboard();

        if(dragboard.hasFiles()){
            dragboard.getFiles().forEach(f->{
                try {
                    String s = Files.probeContentType(f.toPath());

                    System.out.println(s);
                    Image image = new Image(new FileInputStream(f));
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    hBox.getChildren().addAll(imageView);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e){

                }
            });
        }

    }

    private void onDragOver(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        Dragboard dragboard = dragEvent.getDragboard();
        dragEvent.consume();




  /* data is dragged over the target */
        /* accept it only if it is not dragged from the same node
//         * and if it has a string data */
//        if (dragEvent.getGestureSource() != target &&
//                dragEvent.getDragboard().hasString()) {
//            /* allow for both copying and moving, whatever user chooses */
//
//        }

    }

    public Background getBackground(Color color){
        return new Background(new BackgroundFill(color, null, null));
    }

}
