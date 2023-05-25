<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Building Slides from Screenshots App in JavaFX

## Getting Started

First, make sure to have Java 20+ installed on your development machine. You
might need:

- [SDKMAN](https://sdkman.io).
- [Java installer from foojay.io](https://foojay.io/download).
- [IntelliJ IDEA](https://www.jetbrains.com/idea).
- [Scene Builder](https://gluonhq.com/products/scene-builder).

I highly suggest using the **Zulu (FX) distribution** to get the FX mods out of
the box!

For creating a new JavaFX app, follow
[Beginning JavaFX Applications with IntelliJ IDE \| foojay.io](https://foojay.io/today/beginning-javafx-with-intellij)
with one of the approach given by the author. I suggest using the "Plain"
approach for this project.

Our app package name is `engineer.mathsoftware.blog.slides`.

The following is the initial app project.

`module-info.java`

```java
module engineer.mathsoftware.blog.slides {
    requires javafx.controls;
    requires javafx.fxml;
    opens engineer.mathsoftware.blog.slides to javafx.fxml;
    exports engineer.mathsoftware.blog.slides;
}
```

<figcaption>
<p align="center"><strong>Definition of Application Modules</strong></p>
</figcaption>

As given above, it's required JavaFX `controls`, and `fxml` mods since I decided
to use FXML with the Scene Builder. Then, the main app package has to be opened
to the `fxml` mod, so it can use reflection on our app, as well as exporting it
so JavaFX in general can see our app via reflection.

Now, we move forward the main package.

`package-info.java`

```java
/**
 * Provides a desktop application that converts screenshots into professional
 * slides that tell a story.
 */
package engineer.mathsoftware.blog.slides;
```

<figcaption>
<p align="center"><strong>Main Application Package</strong></p>
</figcaption>

`Main.java`

```java
public class Main extends Application {
    private static final double WINDOW_WIDTH = 1360.0;
    private static final double WINDOW_HEIGHT = 640.0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        var root = new VBox();
        var scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        var btn = new Button();

        btn.setText("Hello World");
        btn.setOnAction(actionEvent -> System.out.println("Hello World"));

        root.setAlignment(Pos.CENTER);
        root.getChildren()
            .add(btn);

        primaryStage.setTitle("Slides");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
```

<figcaption>
<p align="center"><strong>Initial Hello World App</strong></p>
</figcaption>

![Hello World](images/hello-world.png)

<figcaption>
<p align="center"><strong>Hello World</strong></p>
</figcaption>

### Initial Master-View-Detail Layout with Drag-and-Drop ListView

I already developed the initial application layout (and logic) —which are
relatively heavy—, so I'll add the FXML version here with a preview to see
what's being developed.

This is the
[initial layout app.fxml file](https://github.com/tobiasbriones/blog/blob/main/swe/dev/java/javafx/drawing/productivity/building-slides-from-screenshots-app-in-javafx/slides/src/main/resources/app.fxml)
that looks like:

![Scene Builder: Initial app.fxml](images/scene-builder-.-initial-app.fxml.png)

<figcaption>
<p align="center"><strong>Scene Builder: Initial app.fxml</strong></p>
</figcaption>

The layout tree (briefly) consists of:

- A split pane that defines the Master-View-Detail:
    - A **master view** with a user input pane to manage the image files.
    - A **view pane** that will show the slides rendered.
    - A **detail pane** that will hold the properties for generating the slides.

It requires events for the file drag-and-drop, and some buttons.

It also has a menu bar that can be trivially implemented later.

The current layout tree consists of:

```
├── VBox
    ├── MenuBar
        ├── Menu (File)
            ├── MenuItem (New)
            ├── MenuItem (Open Working Directory)
            ├── SeparatorMenuItem
            ├── MenuItem (Clear)
            ├── SeparatorMenuItem
            └── MenuItem (Quit)
        └── Menu (Help)
            └── MenuItem (About Slides EP)
    ├── SplitPane
        ├── VBox
            ├── VBox
                ├── Label (Screenshots)
                └── HBox
                    ├── Button (ADD)
                    └── HBox
                        └── Button (CLEAR)
            └── ListView
        ├── VBox
            ├── Label (Slides)
            └── ScrollPane
                └── AnchorPane
                    └── HBox
                        └── ImageView
        └── AnchorPane
            └── Label (Details)
    └── HBox
        ├── HBox
            └──Label (Slides App)
        ├── HBox
            └── Label (Build a presentation from screenshots)
        └── HBox
```

For integrating this layout, the root view has to be loaded from the FXML
resource `app.fxml` in the root of the `resources` project's directory.

`start | Main`

```java
var root = FXMLLoader.<Parent>load(
    Objects.requireNonNull(getClass().getResource("/app.fxml"))
);
```

<figcaption>
<p align="center"><strong>Loading the FXML Resource</strong></p>
</figcaption>

Then, `AppController` will handle the input events.

```java
public class AppController {
    @FXML
    public void initialize() {}

    @FXML
    private void onDragOver(DragEvent dragEvent) {}

    @FXML
    private void onDragDropped(DragEvent dragEvent) {}

    @FXML
    private void onDragExited(DragEvent dragEvent) {}

    @FXML
    private void onAddButtonAction(ActionEvent event) {}

    @FXML
    private void onClearButtonAction() {}
}
```

<figcaption>
<p align="center"><strong>Initial Application Controller</strong></p>
</figcaption>

## Application Data

The application will handle data related to image files that make up the
presentation, which will be stored in a local directory.

A basic image item needs to be loaded into the list of images.

`ImageItem.java`

```java
public record ImageItem(String filename, Image image) {
    @Override
    public int hashCode() {
        return Objects.hash(filename);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ImageItem imageItem)
            && Objects.equals(filename, imageItem.filename());
    }
}
```

<figcaption>
<p align="center"><strong>Definition of an Application Image Item with Name
and Image</strong></p>
</figcaption>

Notice, how the `hashCode` and `equals` methods had to be overwritten because of
the `Image` object[^x][^x].

[^x]: In this case, two `ImageItem`s are equal if their names are equal

[^x]: The binary `Image` field made it impossible to update the same item from a
    `List` with different object instances but the same name

This item will model the images (screenshots) saved to the application data
directory. Notice this will be a simple directory tree with a depth of 1 with no
subdirectories.

The items need to be stored and loaded from our local storage.

For this, I defined the `DataRepository` API.

`DataRepository.java | ...slides.data`

```java
public interface DataRepository {
    void createOrUpdateImage(Path imagePath) throws IOException;

    void createImage(Path imagePath) throws IOException;

    List<ImageItem> readAllImages() throws IOException;

    Image readImage(String imageName) throws IOException;

    void updateImage(Path imagePath) throws IOException;

    void deleteImage(String imageName) throws IOException;

    void deleteAllImages() throws IOException;
}
```

<figcaption>
<p align="center"><strong>Application Data API</strong></p>
</figcaption>

I also wrote a `Data` utility class to hold important functions.

`Data.java | ...slides.data`

```java
public final class Data {
    private static final String EXTENSION_DOT = ".";
    private static final String[] supportedExtensions = { "png", "jpg" };

    public static boolean isFileSupported(Path path) {
        var filter = filterValidNames(Stream
            .of(path)
            .map(Path::getFileName)
            .map(Path::toString)
        );
        return filter.size() == 1;
    }

    public static boolean areValidImageFiles(Collection<? extends File> files) {
        var filter = filterValidNames(files.stream().map(File::getName));
        return filter.size() == files.size();
    }

    private static List<String> filterValidNames(Stream<String> name) {
        var valid = List.of(supportedExtensions);
        return name
            .filter(x -> x.contains(EXTENSION_DOT))
            .map(x -> x.substring(x.lastIndexOf(EXTENSION_DOT) + 1))
            .filter(valid::contains)
            .toList();
    }

    private Data() {}
}
```

<figcaption>
<p align="center"><strong>Application Data Filters</strong></p>
</figcaption>

That way, we'll know whether a given file list (that can be dropped into the
`ListView`) has supported file extensions which will avoid polluting the data
directory with random files and ensure more correctness in our logic.

These definitions will allow us to perform data operations in our application.

### Local Storage Implementation

The implementation of the `DataRepository` is straightforward.

It uses the `java.nio.file` API to access the file system, and the code written
before.

`Data.java | ...slides.data`

```java
public class LocalDataRepository implements DataRepository {
    private final String root;

    public LocalDataRepository(String root) { this.root = root; }

    @Override
    public void createOrUpdateImage(Path imagePath) throws IOException {
        var imageName = imagePath.getFileName().toString();

        if (Files.exists(pathOf(imageName))) {
            updateImage(imagePath);
        }
        else {
            createImage(imagePath);
        }
    }

    @Override
    public void createImage(Path imagePath) throws IOException {
        requireFileExists(imagePath);
        requireValidFile(imagePath);
        requireLocalStorage();

        var imageName = imagePath.getFileName().toString();

        Files.copy(imagePath, pathOf(imageName));
    }

    @Override
    public List<ImageItem> readAllImages() throws IOException {
        var images = new ArrayList<ImageItem>();
        var files = Files
            .walk(pathOf(""), 1)
            .filter(Data::isFileSupported);

        try (files) {
            for (var file : files.toList()) {
                var img = new Image(Files.newInputStream(file));
                images.add(new ImageItem(file.getFileName().toString(), img));
            }
        }
        return images;
    }

    @Override
    public Image readImage(String imageName) throws IOException {
        var path = pathOf(imageName);

        requireFileExists(path);
        requireLocalStorage();

        return new Image(Files.newInputStream(path));
    }

    @Override
    public void updateImage(Path imagePath) throws IOException {
        var imageName = imagePath.getFileName().toString();

        requireFileExists(imagePath);
        requireLocalStorage();

        deleteImage(imageName);
        createImage(imagePath);
    }

    @Override
    public void deleteImage(String imageName) throws IOException {
        var path = pathOf(imageName);

        requireFileExists(path);
        requireLocalStorage();

        Files.delete(path);
    }

    @Override
    public void deleteAllImages() throws IOException {
        requireLocalStorage();
        var walk = Files
            .walk(pathOf(""), 1)
            .filter(Files::isRegularFile);

        try (walk) {
            for (var path : walk.toList()) {
                Files.delete(path);
            }
        }
    }

    private void requireLocalStorage() throws IOException {
        var rootPath = pathOf("");

        if (!Files.exists(rootPath)) {
            Files.createDirectory(rootPath);
        }
    }

    private Path pathOf(String filename) {
        return Path.of(root, filename);
    }

    private static void requireFileExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new IOException("File " + path + " does not exist");
        }
    }

    private static void requireValidFile(Path path) throws IOException {
        if (!Data.isFileSupported(path)) {
            throw new IOException("File " + path + " is not supported");
        }
    }
}
```

<figcaption>
<p align="center"><strong>Implementation of "DataRepository" on
"LocalDataRepository"</strong></p>
</figcaption>

This realization of `DataRepository` allows us to access our `data` directory
application images.

## Master Pane

The first part of the app is a master pane that lists the images.

![Master Pane](images/master-pane.png)

<figcaption>
<p align="center"><strong>Master Pane</strong></p>
</figcaption>

This pane will be able to list the images, add new image(s) via drag-and-drop
and or a `Button` with `FileChooser`, delete an image, delete all images, and
rearrange them in the order they will appear in the presentation.

### Initializing the App Controller

The `AppController` will need some fields.

`class AppController`

```java
private static final String DATA_ROOT = "data";
private final DataRepository repository;
@FXML private Button addButton;
@FXML private Label statusLabel;

public AppController() {
    this.repository = new LocalDataRepository(DATA_ROOT);
}
```

<figcaption>
<p align="center"><strong>Members of "AppController"</strong></p>
</figcaption>

A good initialization will be needed too.

`class AppController`

```java
@FXML
public void initialize() {
    statusLabel.setText("Slides App");

    initAddButton();
}

private void initAddButton() {
    var icAdd = new Image(
        Objects.requireNonNull(
            getClass().getResourceAsStream("/ic_add.png")
        )
    );
    var addImageView = new ImageView(icAdd);

    addImageView.setFitWidth(18.0);
    addImageView.setFitHeight(18.0);
    addButton.setGraphic(addImageView);
}
```

<figcaption>
<p align="center"><strong>Initialization of "AppController"</strong></p>
</figcaption>

Including other methods will be helpful as well.

`class AppControler`

```java
private void handleError(IOException e) {
    setStatus(e.getMessage());
    e.printStackTrace();
}

private void setStatus(String msg) {
    statusLabel.setText(msg);
}
```

Now the controller set up to keep adding the other features.

### Drag and Drop

One engaging feature of this app is its file drag-and-drop, where you can create
or update one or many images just as simple.

![Dragging Files](images/dragging-files.png)

<figcaption>
<p align="center"><strong>Dragging Files</strong></p>
</figcaption>

If the files are [accepted](#application-data) by our app then they will be
added.

![Files Updated](images/files-updated.png)

<figcaption>
<p align="center"><strong>Files Updated</strong></p>
</figcaption>

They won't be added if rejected (e.g., a Photopea ".psd" file). Per
[our rules](#application-data), one invalid file is enough to reject all of
them.

![Drag Canceled: Invalid Files](images/drag-canceled-.-invalid-files.png)

<figcaption>
<p align="center"><strong>Drag Canceled: Invalid Files</strong></p>
</figcaption>

So our `DragEvent` cancels further actions with the clipboard files.

Another way the `DragEvent` can finish is when you just cancel the drop action
with your mouse by leaving the files out.

![Drag Canceled](images/drag-canceled.png)

<figcaption>
<p align="center"><strong>Drag Canceled</strong></p>
</figcaption>

The events are set from the `app.fxml` view already, so the controller 
implementation is left.

The three events that will be required for this app consist of:

- **Drag Over:** Files are being dragged onto the `ListView`, so they will 
  either be accepted or rejected.
- **Drag Dropped:** Files were deposited into the app.
- **Drag Exited:** Cancels the drag as files dragged with the mouse are out of 
  scope.

`class AppContoller`

```java
@FXML
private void onDragOver(DragEvent dragEvent) {
    var dragboard = dragEvent.getDragboard();

    if (dragboard.hasFiles()) {
        if (Data.areValidImageFiles(dragboard.getFiles())) {
            setStatus("Dragging files...");
            dragEvent.acceptTransferModes(TransferMode.COPY);
        }
        else {
            setStatus("Drag canceled (invalid files)");
        }
        dragEvent.consume();
    }
}

@FXML
private void onDragExited(DragEvent dragEvent) {
    if (!dragEvent.isDropCompleted()) {
        setStatus("Drag canceled");
    }
    dragEvent.consume();
}

@FXML
private void onDragDropped(DragEvent dragEvent) {
    var board = dragEvent.getDragboard();

    if (board.hasFiles()) {
        createOrUpdateImages(board.getFiles());
        setStatus("Files updated");
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
    }
    else {
        setStatus("Drag canceled (empty)");
    }
}
```

<figcaption>
<p align="center"><strong>Drag Event Implementations</strong></p>
</figcaption>

These implementations will allow the drag-and-drop feature in our application.

### List View

The list in the master pane is a key for completing this implementation.

First, a custom cell renderer needs to be created, obviously.

If you remember or are homesick for the Android old school like me, that would
come in handy a lot.

`class ImageItemCell`

```java
class ImageItemCell extends ListCell<ImageItem> {
    interface Listener {
        void onDelete(ImageItem item);
    }

    private final Listener l;
    private final HBox view;
    private final ImageView imageView;
    private final Label nameLabel;
    private final Button deleteButton;
    private final Tooltip tip;

    ImageItemCell(Listener l) {
        super();
        this.l = l;
        this.view = new HBox();
        this.imageView = new ImageView();
        this.nameLabel = new Label();
        this.deleteButton = new Button();
        this.tip = new Tooltip();

        init();
    }

    @Override
    protected void updateItem(ImageItem item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        }
        else {
            updateItem(item);
        }
    }

    private void init() {
        var imageParent = new HBox();

        imageView.setFitWidth(128.0);
        imageView.setFitHeight(64.0);
        imageView.setPreserveRatio(true);

        imageParent.setPrefWidth(144.0);
        imageParent.setPrefHeight(96.0);
        imageParent.setAlignment(Pos.CENTER);
        imageParent.getChildren().add(imageView);

        var maxNameLength = 20;

        nameLabel
            .textProperty()
            .addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > maxNameLength) {
                    var txt = newValue.substring(0, maxNameLength) + "...";
                    nameLabel.setText(txt);
                }
            });

        var deleteParent = new HBox();

        deleteButton.setText("X");
        deleteButton.setStyle("-fx-text-fill: #b00020;");

        HBox.setHgrow(deleteParent, Priority.ALWAYS);
        deleteParent.setSpacing(16.0);
        deleteParent.setAlignment(Pos.CENTER_RIGHT);
        deleteParent.getChildren().add(deleteButton);

        Tooltip.install(view, tip);

        view.setPrefHeight(96.0);
        view.setAlignment(Pos.CENTER_LEFT);
        view.setSpacing(16.0);
        view.getChildren().addAll(imageParent, nameLabel, deleteParent);
    }

    private void updateItem(ImageItem item) {
        nameLabel.setText(item.filename());
        imageView.setImage(item.image());
        imageView.setSmooth(true);

        var boundingBox = imageView.getLayoutBounds();
        var clip = new Rectangle(
            boundingBox.getWidth(), boundingBox.getHeight()
        );

        clip.setArcWidth(8.0);
        clip.setArcHeight(8.0);
        imageView.setClip(clip);

        deleteButton.setOnAction(event -> onDeleteButtonAction(item));
        tip.setText(item.filename());

        setGraphic(view);
    }

    private void onDeleteButtonAction(ImageItem item) {
        var alert = new Alert(
            Alert.AlertType.CONFIRMATION,
            "Delete " + item.filename() + "?",
            ButtonType.YES,
            ButtonType.NO
        );
        alert.showAndWait();

        if(alert.getResult() == ButtonType.YES) {
            l.onDelete(item);
        }
    }
}
```

<figcaption>
<p align="center"><strong>Implementation of the Cell Items for the 
List View</strong></p>
</figcaption>

I also added a `Tooltip` so the full file name is shown when you hover the item
and a maximum length for the text to be covered with an ellipsis if long.

Moreover, I set a rounded corner of the image in the method `updateItem` via
`imageView.setClip(clip);`, and set the confirmation `Alert` in the method
`onDeleteButtonAction` that sends the event to delete the item to the
`ImageItemCell.Listener` (the controller).

So, that's the `ListView` item implementation you see in the screenshots.

Then, this is integrated into the controller.

First, the cell callback that was defined can be realized by the controller,
i.e., `implements ImageItemCell.Listener` which is followed by:

`class AppController`

```java
@FXML private ListView<ImageItem> imageList;

@FXML
public void initialize() {
    // ... //
    imageList.setCellFactory(param -> new ImageItemCell(this));

    loadImageList();
}

@Override
public void onDelete(ImageItem item) {
    try {
        repository.deleteImage(item.filename());
        imageList.getItems().remove(item);
        setStatus("Item deleted");
    }
    catch (IOException e) {
        handleError(e);
    }
}

@Override
public void onDelete(ImageItem item) {
    try {
        repository.deleteImage(item.filename());
        imageList.getItems().remove(item);
    }
    catch (IOException e) {
        handleError(e);
    }
}

private void loadImageList() {
    try {
        var images = repository.readAllImages();
        imageList.setItems(FXCollections.observableList(images));
    }
    catch (IOException e) {
        handleError(e);
    }
}

private void createOrUpdateImages(Iterable<? extends File> files) {
    for (var file : files) {
        var path = file.toPath();
    
        try {
            repository.createOrUpdateImage(path);
        
            var imageName = path.getFileName().toString();
            var newImage = repository.readImage(imageName);
            var newImageItem = new ImageItem(imageName, newImage);
            var listItems = imageList.getItems();
        
            listItems.remove(newImageItem);
            listItems.add(new ImageItem(imageName, newImage));
        }
        catch (IOException e) {
            handleError(e);
        }
    }
}

private void deleteAllImages() {
    try {
        repository.deleteAllImages();
        imageList.getItems().clear();
        setStatus("All items deleted");
    }
    catch (IOException e) {
        handleError(e);
    }
}

private void handleError(IOException e) {
    statusLabel.setText(e.getMessage());
    e.printStackTrace();
}
```

<figcaption>
<p align="center"><strong>Integration of the List View and Data 
Repository into the App Controller</strong></p>
</figcaption>

At this point, the list with drag-and-drop was built which is a big part of
this master pane.

### Deleting Items

For deleting an item, you click on the delete button of the item, and a
confirmation `Alert` will finish this action.

![Delete Item](images/delete-item.png)

<figcaption>
<p align="center"><strong>Delete Item</strong></p>
</figcaption>

To delete all the items, the "clear" `Button` will "do the trick".

![Clear All](images/clear-all.png)

<figcaption>
<p align="center"><strong>Clear All</strong></p>
</figcaption>

Therefore, here we go with our controller again.

`class AppController`

```java
@FXML private Parent view;

@FXML
private void onAddButtonAction() {
    var chooser = new FileChooser();

    chooser.setTitle("Open Files");
    chooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter(
            "Image Files (*.png, *.jpg)",
            "*.png",
            "*.jpg"
        )
    );
    var files = chooser.showOpenMultipleDialog(view.getScene().getWindow());

    if (files != null) {
        createOrUpdateImages(files);
    }
}

@FXML
private void onClearButtonAction() {
    var alert = new Alert(
    Alert.AlertType.CONFIRMATION,
        "Delete all the data?",
        ButtonType.YES,
        ButtonType.NO
    );
    alert.showAndWait();

    if (alert.getResult() == ButtonType.YES) {
        deleteAllImages();
    }
}
```

<figcaption>
<p align="center"><strong>Implementation of Deleting Actions (the "Danger 
Zone")</strong></p>
</figcaption>

This provides a safe delete mechanism for our application images.

### Arranging Image Items via More Drag and Drop

Our presentation order will be the one shown in the images `ListView` that
already supports a drag-and-drop event for adding or updating files to the app,
and now it needs one more implementation for arranging its items via this fancy
mechanism.

![ListView Drag and Drop Animation](images/listview-drag-and-drop-animation.gif)

<figcaption>
<p align="center"><strong>ListView Drag and Drop Animation</strong></p>
</figcaption>

#### Cell Drag and Drop Implementation

First, a new event will need to be defined in the `ImageItemCell` `Listener`,
namely, `void onArrange(int draggedIdx, int destIdx);`. So, our abstract list of
images (the data structure) gets sorted when this happens (then updates the
view).

`init | class ImageItemCell`

```java
setDragAndDropItemSort();

getStyleClass().add("cell");
```

<figcaption>
<p align="center"><strong>Updating the Init Method of
"ImageItemCell"</strong></p>
</figcaption>

`class ImageItemCell`

```java
private void setDragAndDropItemSort() {
    setOnDragDetected(this::onDragDetected);
    setOnDragOver(this::onDragOver);
    setOnDragEntered(this::onDragEntered);
    setOnDragExited(this::onDragExited);
    setOnDragDropped(this::onDragDropped);
    setOnDragDone(DragEvent::consume);
}
```

<figcaption>
<p align="center"><strong>Drag and Drop Events Needed</strong></p>
</figcaption>

`class ImageItemCell`

```java
private void onDragDetected(MouseEvent event) {
    var item = getItem();

    if (item == null) {
        return;
    }

    var idx = getListView().getItems().indexOf(item);
    var dragboard = startDragAndDrop(TransferMode.MOVE);
    var content = new ClipboardContent();

    content.putString(String.valueOf(idx));
    dragboard.setDragView(view.snapshot(null, null));
    dragboard.setContent(content);

    event.consume();
}

private void onDragOver(DragEvent event) {
    if (event.getGestureSource() != this && event.getDragboard().hasString()) {
        event.acceptTransferModes(TransferMode.MOVE);
        event.consume();
    }
}

private void onDragEntered(DragEvent event) {
    if (event.getGestureSource() != this && event.getDragboard().hasString()) {
        getStyleClass().add("entered");
        event.consume();
    }
}

private void onDragExited(DragEvent event) {
    if (event.getGestureSource() != this && event.getDragboard().hasString()) {
        getStyleClass().remove("entered");
        event.consume();
    }
}

private void onDragDropped(DragEvent event) {
    var item = getItem();

    if (item == null) {
        return;
    }

    var dragboard = event.getDragboard();

    if (dragboard.hasString()) {
        var items = getListView().getItems();
        int draggedIdx = Integer.parseInt(dragboard.getString());
        int destIdx = items.indexOf(item);

        l.onArrange(draggedIdx, destIdx);

        event.setDropCompleted(true);
        event.consume();
    }
}
```

<figcaption>
<p align="center"><strong>Drag Event Implementations to Rearrange a List Cell</strong></p>
</figcaption>

First, notice that if you're not careful, you'll introduce side effects to
these kinds of events, as we can have many event implementations. In this case,
the drag events fall into the `ListView` (the "bigger") and are also listened
from each of its cells, that is, each `ImageItemCell`.

What controls this crazy state sharing, if you notice, are the calls to the
`consume` method of the `DragEvent`s. Setting `setDropCompleted` to `true` also
helps with this.

So, I carefully **tested the app so both drag-and-drop events for files and for
arranging cells work properly**.

The drag view is set to a *snapshot* of the cell being dragged. This is the
graphic you see on the tip of your mouse when dragging something on the screen.

There are some CSS classes to apply a visual effect on the cells when dragging
one cell onto another. These classes have to be added to the app from a CSS
file.

That was with respect to the `ImageItemCell`. Now this feature has to be added
to the `AppController` part.

#### Controller Update

The previous changes have to be implemented in the app controller.

I lately made the app less coupled, so we'll work with a `images` `List`
stored in the controller as the "source of truth" for the image data. Then, this
list will behave **reactively** to update the GUI.

`class AppController`

```java
private final ObservableList<ImageItem> images;

public AppController() {
    // ... //
    this.images = FXCollections.observableArrayList();
}

@FXML
public void initialize() {
    // ... //
    imageList.setItems(images);
    // ... //
}

@Override
public void onArrange(int draggedIdx, int destIdx) {
    var dragged = images.get(draggedIdx);
    var dest = images.get(destIdx);

    images.set(draggedIdx, dest);
    images.set(destIdx, dragged);
    setStatus("Item arranged");
}

private void loadImageList() {
    try {
        var loadedImages = repository.readAllImages();

        images.clear();
        images.addAll(loadedImages);
    }
    catch (IOException e) {
        handleError(e);
    }
}
```

<figcaption>
<p align="center"><strong>Setting up the App Controller for the 
"onArrange" Event</strong></p>
</figcaption>

What's mostly new here, is the `onArrange` event that was defined before in the
`ImageItemCell` `Listener`. This method updates the data, and this data is
reactive, so it automatically updates the `ListView` which was bound to the
`images` list via `imageList.setItems(images)`.

Cells are swapped and automatic scroll is missing which are limitations to this
implementation. The wanted behaviour may probably be more advanced but that's
out of the scope of this EP.

Now both the cell and controller implementations are in sync.

#### App CSS Styles

It was time to introduce some CSS here to add a class to the list cell when
another item is being dragged to it.

`app.css`

```css
.cell {
    -fx-border-width: 2px;
    -fx-border-radius: 4px;
    -fx-border-color: transparent;
}

.entered {
    -fx-border-color: #4FC3F7;
}
```

<figcaption>
<p align="center"><strong>CSS Styles for Dragging a List Cell into
Another Cell</strong></p>
</figcaption>

I also added a new method in `Main.java` to help load these resources.

`start | class Main`

```java
scene.getStylesheets().add(
    loadResource("app.css").toExternalForm()
);
```

<figcaption>
<p align="center"><strong>Loading CSS Styles into the App</strong></p>
</figcaption>

`class Main`

```java
private URL loadResource(String name) {
    return Objects.requireNonNull(
        getClass().getClassLoader().getResource(name)
    );
}
```

<figcaption>
<p align="center"><strong>Method that Loads an App Resource by
File Name</strong></p>
</figcaption>

Now, the CSS classes defined above can be used to style the `ListView` cells.

#### Order of the Presentation Slides

With all this, we now have the arrangement of the images done as well as the
master pane complete.