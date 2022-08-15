package qaz.code.view;

import javafx.scene.layout.BorderPane;
import qaz.code.model.Sheets;

public class MainPane extends BorderPane {
    private final SheetsPane sheetsPane;
    private final MenuBar menuBar;

    public SheetsPane getSheetsPane() {
        return sheetsPane;
    }

    public MainPane(Sheets sheets) {
        menuBar = new MenuBar(sheets);
        sheetsPane = new SheetsPane(sheets);
        setCenter(sheetsPane);
        setTop(menuBar);
        sheetsPane.addEmptySheet();
    }
}
