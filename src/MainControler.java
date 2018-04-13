import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainControler implements Initializable {


    public FlowPane flowPane;
    public ListView listView;
    public ScrollPane scrollPane;
    public Label lblFolder;
    public TableView tableView;
    public TextField txtHeight;
    public TextField txtWidth;
    DecimalFormat format = new DecimalFormat( "#.0" );


    private File folder;
    ObservableList<String> fileNameList = FXCollections.observableArrayList();
    private Map<String, File> images = new HashMap<>();
    private TableView upTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("inited");
        setupPane(scrollPane);
        setupListView(listView);
        setupTableView(tableView);
        setUpTextField();
    }

    private void setUpTextField() {
        setNumericTextFormatter(txtHeight);
        setNumericTextFormatter(txtWidth);
    }


    private void setNumericTextFormatter(TextField textField){
        textField.setTextFormatter( new TextFormatter<>(c ->
        {
            if ( c.getControlNewText().isEmpty() )
            {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition( 0 );
            Object object = format.parse( c.getControlNewText(), parsePosition );

            if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
            {
                return null;
            }
            else
            {
                return c;
            }
        }));
    }

    private void setupPane(ScrollPane pane) {
        pane.setOnDragOver(this::onDragOver);
        pane.setOnDragDropped(this::onDragDropped);
        pane.setOnDragEntered(this::onDragEntered);
        pane.setOnDragExited(this::onDragExited);
    }

    private void onDragExited(DragEvent dragEvent) {
        scrollPane.setBackground(getBackground(Color.BLUE));
    }

    private void onDragEntered(DragEvent dragEvent) {
        scrollPane.setBackground(getBackground(Color.RED));
    }

    private void onDragDropped(DragEvent dragEvent) {
        System.out.println("done");

        Dragboard dragboard = dragEvent.getDragboard();

        if (dragboard.hasFiles()) {
            dragboard.getFiles().forEach(f -> {
                try {

                    fileNameList.add(f.getName());
                    Image image = new Image(new FileInputStream(f));
                    images.put(f.getName(), f);

                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    flowPane.getChildren().add(imageView);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    private void onDragOver(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);

        Dragboard dragboard = dragEvent.getDragboard();
        dragEvent.consume();
        dragboard.getFiles().forEach(f->{
            FileTypeHelper.ImageType imageType = FileTypeHelper.detectFileType(f);
        });



  /* data is dragged over the target */
        /* accept it only if it is not dragged from the same node
//         * and if it has a string data */
//        if (dragEvent.getGestureSource() != target &&
//                dragEvent.getDragboard().hasString()) {
//            /* allow for both copying and moving, whatever user chooses */
//
//        }

    }

    public Background getBackground(Color color) {
        return new Background(new BackgroundFill(color, null, null));
    }

    public void setupListView(ListView<String> upListView) {

        upListView.setItems(fileNameList);
    }

    public void writeImageToFile(Image image, File file, FileTypeHelper.ImageType imageType) {

        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);

        try {
            ImageIO.write(bImage, imageType.toString(), file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onButtonConvertPressed(ActionEvent actionEvent) {
        if (folder == null) {
            chooseFolder(actionEvent);
        }


        images.forEach((k,v)->{
            File file = new File (folder,k);
            try {


                int height = txtHeight.getText().isEmpty() ? 100: Integer.parseInt( txtHeight.getText());
                int width = txtHeight.getText().isEmpty() ? 100: Integer.parseInt( txtWidth.getText());



                Image image = new Image(new FileInputStream(v),width,height,true,true);
                FileTypeHelper.ImageType imageType = FileTypeHelper.detectFileType(file);
                writeImageToFile(image,file,imageType);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

    }



    public void onFolderChoosePressed(ActionEvent actionEvent) {

       chooseFolder(actionEvent);
        if (folder != null)
            lblFolder.setText(folder.getPath());
    }

    public void chooseFolder(ActionEvent actionEvent){
        Node source = (Node) actionEvent.getSource();
        Window theStage = source.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Set directory to save images");
        folder = directoryChooser.showDialog(theStage);
    }

    public void setupTableView(TableView upTableView) {
        tableView.getColumns().addAll(
                new TableColumn<String,String>("Name"),
                new TableColumn<String,String>("Width"),
                new TableColumn<String,String>("Height")
        );
    }
}
