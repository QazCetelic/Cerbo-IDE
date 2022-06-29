package qaz.code.view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class MenuBar extends javafx.scene.control.MenuBar {
    
    private final MenuItem save;
    private final MenuItem load;
    private final MenuItem minify;
    
    public MenuBar() {
        Menu file = new Menu("File");
        save = new MenuItem("Save");
        load = new MenuItem("Load");
        file.getItems().addAll(save, load);
        Menu edit = new Menu("Edit");
        minify = new MenuItem("Minify");
        edit.getItems().addAll(minify);
        getMenus().addAll(file, edit);
    }
}
