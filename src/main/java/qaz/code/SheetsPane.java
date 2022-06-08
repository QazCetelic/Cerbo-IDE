package qaz.code;

import javafx.scene.control.TabPane;

public class SheetsPane extends TabPane {
    public SheetsPane() {

    }
    public void addSheet(SheetTab sheetTab) {
        getTabs().add(sheetTab);
    }
}
