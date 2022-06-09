package qaz.code;

import javafx.scene.layout.BorderPane;

public class MainPane extends BorderPane {
    private final SheetsPane sheetsPane = new SheetsPane();
    private final MenuBar menuBar = new MenuBar();

    public SheetsPane getSheetsPane() {
        return sheetsPane;
    }

    public MainPane() {
        setCenter(sheetsPane);
        setTop(menuBar);
        sheetsPane.addEmptySheet();
    }
}
