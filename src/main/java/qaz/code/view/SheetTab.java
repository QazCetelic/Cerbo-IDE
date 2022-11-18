package qaz.code.view;

import javafx.scene.control.Tab;
import qaz.code.Cerbo;
import qaz.code.model.Sheet;

public class SheetTab extends Tab {
    private final Sheet sheet;
    public SheetTab(Sheet sheet, Cerbo cerbo) {
        super(sheet.getNameProperty().get(), null);
        this.sheet = sheet;
        setContent(new SheetPane(sheet, cerbo));
        setOnClosed(event -> cerbo.sheets.getSheetsProperty().remove(sheet));
    }
    
    public Sheet getSheet() {
        return sheet;
    }
    
    public SheetPane getSheetView() {
        return (SheetPane) getContent();
    }
}
