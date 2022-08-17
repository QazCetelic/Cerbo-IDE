package qaz.code.view;

import javafx.scene.control.Tab;
import qaz.code.Cerbo;
import qaz.code.model.Sheet;
import qaz.code.model.Sheets;

public class SheetTab extends Tab {
    private final Sheet sheet;
    public SheetTab(Sheet sheet, Cerbo cerbo) {
        super(sheet.nameProperty().get(), null);
        this.sheet = sheet;
        setContent(new SheetPane(sheet, cerbo));
        setOnClosed(event -> cerbo.sheets.sheetsProperty().remove(sheet));
    }
    
    public Sheet getSheet() {
        return sheet;
    }
    
    public SheetPane getSheetView() {
        return (SheetPane) getContent();
    }
}
