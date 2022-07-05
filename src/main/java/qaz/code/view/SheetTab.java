package qaz.code.view;

import javafx.scene.control.Tab;
import qaz.code.model.Sheet;

public class SheetTab extends Tab {
    public SheetTab(Sheet sheet) {
        super(sheet.nameProperty().get(), null);
        setContent(new SheetPane(sheet));
    }

    public SheetPane getSheetView() {
        return (SheetPane) getContent();
    }
}
