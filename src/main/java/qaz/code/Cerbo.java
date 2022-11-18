package qaz.code;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import qaz.code.model.Snippet;
import qaz.code.model.Snippets;
import qaz.code.model.Views;
import qaz.code.view.MainPane;

import java.util.Objects;

public class Cerbo extends Application {
    private MainPane mainPane;
    private static Stage mainStage;
    
    public static Stage getMainStage() {
        return mainStage;
    }
    
    public final Snippets snippets = new Snippets();
    public final Views views = new Views();
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        mainPane = new MainPane(this);
        mainStage = stage;
        Scene scene = new Scene(mainPane, 800, 500);
//        DarculaFX.applyDarculaStyle(scene);
        scene.getStylesheets().addAll(
            getClass().getResource("/darcula.css").toExternalForm(),
            getClass().getResource("/stylesheet.css").toExternalForm()
        );
        changeTitle();
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN), () -> {
            mainPane.getSheetsPane().addEmptySheet();
        });
        snippets.getSelectedSnippetProperty().addListener(observable -> changeTitle());
        Image icon = new Image(Objects.requireNonNull(Cerbo.class.getResourceAsStream("/icon.png"), "Failed to load icon"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
    }

    public void changeTitle() {
        Snippet snippet = snippets.getSelectedSnippetProperty().get();
        mainStage.setTitle("Cerbo IDE - " + (snippet != null ? snippet.getNameProperty().get() : "None"));
    }
}
