package qaz.code.view;

import javafx.scene.layout.BorderPane;
import qaz.code.Cerbo;
import qaz.code.model.Sheets;

public class MainPane extends BorderPane {
    private final SheetsPane sheetsPane;
    private final MenuBar menuBar;

    public SheetsPane getSheetsPane() {
        return sheetsPane;
    }

    public MainPane(Cerbo cerbo) {
        menuBar = new MenuBar(cerbo);
        sheetsPane = new SheetsPane(cerbo);
        setCenter(sheetsPane);
        setTop(menuBar);
        sheetsPane.addEmptySheet();
    }
}
