package qaz.code.view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import qaz.code.Cerbo;
import qaz.code.model.Snippet;

// TODO split menu's into seperate classes
public class MenuBar extends javafx.scene.control.MenuBar {
    
    public MenuBar(Cerbo cerbo) {
        Menu snippet = new Menu("Snippet");
        MenuItem save = new MenuItem("Save");
        save.setOnAction(e -> cerbo.snippets.getSelectedSnippetProperty().get().save());
        MenuItem load = new MenuItem("Load");
        load.setOnAction(e -> {
            Snippet loadedSnippet = Snippet.load();
            if (loadedSnippet != null) {
                cerbo.snippets.getSelectedSnippetProperty().set(loadedSnippet);
            }
        });
        snippet.getItems().addAll(save, load);
        
        Menu edit = new Menu("Edit");
        MenuItem minify = new MenuItem("Minify");
        minify.setOnAction(e -> cerbo.snippets.getSelectedSnippetProperty().get().minify());
        MenuItem reduceSpacing = new MenuItem("Reduce Spacing");
        reduceSpacing.setOnAction(e -> cerbo.snippets.getSelectedSnippetProperty().get().reduceSpacing());
        edit.getItems().addAll(minify, reduceSpacing);
        
        Menu view = new Menu("View");
        RadioMenuItem memoryView = new RadioMenuItem("Memory view");
            cerbo.views.showMemoryPane.bindBidirectional(memoryView.selectedProperty());
        RadioMenuItem inputView = new RadioMenuItem("Input view");
            cerbo.views.showInputView.bindBidirectional(inputView.selectedProperty());
        RadioMenuItem generalOutputView = new RadioMenuItem("General output view");
            cerbo.views.showOutputView.bindBidirectional(generalOutputView.selectedProperty());
        RadioMenuItem lineOutputView = new RadioMenuItem("Line output view");
            cerbo.views.showLineOutput.bindBidirectional(lineOutputView.selectedProperty());
        RadioMenuItem operationVisualizer = new RadioMenuItem("Operation visualizer");
            cerbo.views.showOperationsView.bindBidirectional(operationVisualizer.selectedProperty());
        view.getItems().addAll(memoryView, lineOutputView, generalOutputView, operationVisualizer);
        view.getItems().forEach(menuItem -> ((RadioMenuItem) menuItem).setSelected(true));
        Menu execution = new Menu("Execution");
        RadioMenuItem execute = new RadioMenuItem("Execute");
        execute.setSelected(true);
        // TODO add execution profiles
        execution.getItems().addAll(execute);
        getMenus().addAll(snippet, edit, view, execution);
    }
}
