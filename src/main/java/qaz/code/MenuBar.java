package qaz.code;

import javafx.scene.control.Menu;

public class MenuBar extends javafx.scene.control.MenuBar {
    private final Menu file = new Menu("File");
    private final Menu edit = new Menu("Edit");
    public MenuBar() {
        getMenus().addAll(file, edit);
    }
}
