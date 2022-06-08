package qaz.code;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.util.Objects;

public class Cerbo extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        MainPane mainPane = new MainPane();
        stage.setTitle("Cerbo IDE");
        Scene scene = new Scene(mainPane, 800, 500);
        scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN), () -> {
            mainPane.getSheetsPane().addSheet(new SheetTab());
        });
        mainPane.getSheetsPane().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            SheetTab sheetTab = (SheetTab) newValue;
            stage.setTitle("Cerbo IDE - " + sheetTab.getText());
        });
        Image icon = new Image(Objects.requireNonNull(Cerbo.class.getResourceAsStream("/icon.png"), "Failed to load icon"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }
}
