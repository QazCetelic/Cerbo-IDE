package qaz.code.view;

import javafx.scene.control.Tab;
import qaz.code.Sheet;

public class SheetTab extends Tab {
    public SheetTab(String name, String content) {
        super(name, new Sheet(content));
    }

    public Sheet getSheet() {
        return (Sheet) getContent();
    }
}
