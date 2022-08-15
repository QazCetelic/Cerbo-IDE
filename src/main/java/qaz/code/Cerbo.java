package qaz.code;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import qaz.code.model.Sheet;
import qaz.code.view.MainPane;

import java.util.Objects;

public class Cerbo extends Application {
    private MainPane mainPane;
    private Stage mainStage;
    private static final ObjectProperty<Sheet> selectedSheet = new SimpleObjectProperty<>();
    
    public static ObjectProperty<Sheet> selectedSheetProperty() {
        return selectedSheet;
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        mainPane = new MainPane();
        mainStage = stage;
        stage.setTitle("Cerbo IDE");
        Scene scene = new Scene(mainPane, 800, 500);
//        DarculaFX.applyDarculaStyle(scene);
        scene.getStylesheets().add(getClass().getResource("/darcula.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
        changeTitle();
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN), () -> {
            mainPane.getSheetsPane().addEmptySheet();
        });
        mainPane.getSheetsPane().getSelectionModel().selectedItemProperty().addListener(observable -> changeTitle());
        Image icon = new Image(Objects.requireNonNull(Cerbo.class.getResourceAsStream("/icon.png"), "Failed to load icon"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
    }

    public void changeTitle() {
        mainStage.setTitle("Cerbo IDE - " + mainPane.getSheetsPane().getSelectedSheetTab().getText());
    }
}
