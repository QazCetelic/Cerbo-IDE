package qaz.code.view;

import javafx.scene.control.Tab;
import qaz.code.model.Sheet;

public class SheetTab extends Tab {
    private final Sheet sheet;
    public SheetTab(Sheet sheet) {
        super(sheet.nameProperty().get(), null);
        this.sheet = sheet;
        setContent(new SheetPane(sheet));
    }
    
    public Sheet getSheet() {
        return sheet;
    }
    
    public SheetPane getSheetView() {
        return (SheetPane) getContent();
    }
}
