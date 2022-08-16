package qaz.code.view;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import qaz.code.Cerbo;
import qaz.code.model.Sheet;
import qaz.code.model.Sheets;

// TODO split menu's into seperate classes
public class MenuBar extends javafx.scene.control.MenuBar {
    
    private final BooleanProperty showMemoryView;
    public BooleanProperty showMemoryViewProperty() {
        return showMemoryView;
    }
    
    private final BooleanProperty showOutputView;
    public BooleanProperty showOutputViewProperty() {
        return showOutputView;
    }
    
    private final BooleanProperty showLineOutputView;
    public BooleanProperty showLineOutputViewProperty() {
        return showLineOutputView;
    }
    
    private final BooleanProperty showGeneralOutputView;
    public BooleanProperty showGeneralOutputViewProperty() {
        return showGeneralOutputView;
    }
    
    public MenuBar(Sheets sheets) {
        Menu file = new Menu("File");
        MenuItem save = new MenuItem("Save");
        save.setOnAction(e -> sheets.selectedSheetProperty().get().save());
        MenuItem load = new MenuItem("Load");
        load.setOnAction(e -> {
            Sheet loadedSheet = Sheet.load();
            if (loadedSheet != null) {
                sheets.select(loadedSheet);
            }
        });
        file.getItems().addAll(save, load);
        Menu edit = new Menu("Edit");
        MenuItem minify = new MenuItem("Minify");
        minify.setOnAction(e -> sheets.selectedSheetProperty().get().minify(50));
        MenuItem reduceSpacing = new MenuItem("Reduce Spacing");
        reduceSpacing.setOnAction(e -> sheets.selectedSheetProperty().get().reduceSpacing());
        edit.getItems().addAll(minify, reduceSpacing);
        Menu view = new Menu("View");
        RadioMenuItem memoryView = new RadioMenuItem("Memory view");
        showMemoryView = memoryView.selectedProperty();
        RadioMenuItem outputView = new RadioMenuItem("Output view");
        showOutputView = outputView.selectedProperty();
        RadioMenuItem lineOutputView = new RadioMenuItem("Line output view");
        showLineOutputView = lineOutputView.selectedProperty();
        RadioMenuItem generalOutputView = new RadioMenuItem("General output view");
        showGeneralOutputView = generalOutputView.selectedProperty();
        view.getItems().addAll(memoryView, outputView, lineOutputView, generalOutputView);
        view.getItems().forEach(menuItem -> ((RadioMenuItem) menuItem).setSelected(true));
        Menu execution = new Menu("Execution");
        RadioMenuItem execute = new RadioMenuItem("Execute");
        execute.setSelected(true);
        // TODO add execution profiles
        execution.getItems().addAll(execute);
        getMenus().addAll(file, edit, view, execution);
    }
}
