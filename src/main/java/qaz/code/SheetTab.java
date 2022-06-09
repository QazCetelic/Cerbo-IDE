package qaz.code;

import javafx.scene.control.Tab;

public class SheetTab extends Tab {
    public SheetTab(String name, String content) {
        super(name, new Sheet(content));
    }

    public Sheet getSheet() {
        return (Sheet) getContent();
    }
}
