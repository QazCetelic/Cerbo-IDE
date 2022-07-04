package qaz.code.view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;

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
        Menu view = new Menu("View");
        RadioMenuItem memoryView = new RadioMenuItem("Memory view");
        RadioMenuItem outputView = new RadioMenuItem("Line output view");
        RadioMenuItem lineOutputView = new RadioMenuItem("Line output view");
        RadioMenuItem generalOutputView = new RadioMenuItem("General output view");
        view.getItems().addAll(memoryView, outputView, lineOutputView, generalOutputView);
        view.getItems().forEach(menuItem -> ((RadioMenuItem) menuItem).setSelected(true));
        Menu execution = new Menu("Execution");
        RadioMenuItem execute = new RadioMenuItem("Execute");
        // TODO add execution profiles
        execution.getItems().addAll(execute);
        getMenus().addAll(file, edit, view, execution);
    }
}
