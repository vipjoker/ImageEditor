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
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainControler implements Initializable {


    public FlowPane flowPane;
    public ListView listView;
    public ScrollPane scrollPane;
    public Label lblFolder;
    public TableView<Value> tableView;
    public TextField txtHeight;
    public TextField txtWidth;
    public StackPane stacPaneDropHere;
    public AnchorPane contentAnchorPane;
    DecimalFormat format = new DecimalFormat("#.0");


    private File folder;
    ObservableList<String> fileNameList = FXCollections.observableArrayList();
    ObservableList<Value> imageList = FXCollections.observableArrayList();
    private Map<String, File> fileHashMap = new HashMap<>();
    private Map<String, Image> imageHashMap = new HashMap<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupPane(scrollPane);
        setupListView(listView);
        setupTableView(tableView);
        setUpTextField();

    }

    private void setUpTextField() {
        setNumericTextFormatter(txtHeight);
        setNumericTextFormatter(txtWidth);
    }


    private void setNumericTextFormatter(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(c ->
        {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                return null;
            } else {
                return c;
            }
        }));
    }

    private void setupPane(ScrollPane pane) {
        contentAnchorPane.prefWidthProperty().bind(scrollPane.widthProperty());
        pane.setOnDragOver(this::onDragOver);
        pane.setOnDragDropped(this::onDragDropped);
        pane.setOnDragEntered(this::onDragEntered);
        pane.setOnDragExited(this::onDragExited);
    }

    private void onDragExited(DragEvent dragEvent) {
        stacPaneDropHere.setVisible(false);
    }

    private void onDragEntered(DragEvent dragEvent) {
        stacPaneDropHere.setVisible(true);

    }

    private void onDragDropped(DragEvent dragEvent) {

        Dragboard dragboard = dragEvent.getDragboard();

        if (dragboard.hasFiles()) {
            dragboard.getFiles().forEach(f -> {
                try {

                    fileNameList.add(f.getName());
                    Image image = new Image(new FileInputStream(f));
                    fileHashMap.put(f.getName(), f);
                    imageHashMap.put(f.getName(), image);
                    ImageView imageView = new ImageView(image);
                    imageView.setPreserveRatio(true);
                    imageView.setFitWidth(150);
                    imageView.setFitHeight(150);
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
        dragboard.getFiles().forEach(f -> {
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
        upListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        upListView.setOnMouseClicked(this::onItemListClicked);
        upListView.setItems(fileNameList);
    }

    private void onItemListClicked(MouseEvent mouseEvent) {
        String selectedItem = (String)listView.getSelectionModel().getSelectedItem();

        if(selectedItem != null){
            imageList.clear();
            Image image = imageHashMap.get(selectedItem);
            imageList.add(new Value("Name", selectedItem));
            imageList.add(new Value("Width", String.valueOf(image.getWidth())));
            imageList.add(new Value("Height", String.valueOf(image.getHeight())));
        }

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


        fileHashMap.forEach((k, v) -> {
            File file = new File(folder, k);
            try {


                int height = txtHeight.getText().isEmpty() ? 100 : Integer.parseInt(txtHeight.getText());
                int width = txtHeight.getText().isEmpty() ? 100 : Integer.parseInt(txtWidth.getText());


                Image image = new Image(new FileInputStream(v), width, height, true, true);
                FileTypeHelper.ImageType imageType = FileTypeHelper.detectFileType(file);
                writeImageToFile(image, file, imageType);
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

    public void chooseFolder(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Window theStage = source.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Set directory to save fileHashMap");
        folder = directoryChooser.showDialog(theStage);
    }

    public void setupTableView(TableView upTableView) {
        TableColumn<Value, String> name = new TableColumn<>("Name");
        TableColumn<Value, String> value = new TableColumn<>("Value");


        tableView.getColumns().addAll(name, value);

        name.setCellValueFactory(param -> param.getValue().getName());
        value.setCellValueFactory(param -> param.getValue().getValue());


        name.prefWidthProperty().bind(tableView.widthProperty().divide(2));
        value.prefWidthProperty().bind(tableView.widthProperty().divide(2));

        tableView.setItems(imageList);

    }
}
