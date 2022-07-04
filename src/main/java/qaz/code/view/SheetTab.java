package qaz.code.view;

import javafx.scene.control.Tab;
import qaz.code.model.Sheet;

public class SheetTab extends Tab {
    public SheetTab(Sheet sheet) {
        super(sheet.nameProperty().get(), null);
        setContent(new SheetView(sheet));
    }

    public SheetView getSheetView() {
        return (SheetView) getContent();
    }
}
