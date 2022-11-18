package qaz.code.view;

import javafx.application.Platform;
import javafx.scene.control.TabPane;
import qaz.code.Cerbo;
import qaz.code.model.Snippet;
import qaz.code.model.Snippets;

import java.util.concurrent.atomic.AtomicInteger;

public class SheetsPane extends TabPane {
    private final Snippets snippets;
    public SheetsPane(Cerbo cerbo) {
        this.snippets = cerbo.snippets;
        
        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                SheetTab tab = (SheetTab) newValue;
                if (tab != null) snippets.getSelectedSnippetProperty().set(tab.getSheet());
            });
        });
        snippets.getSnippetsProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                getTabs().clear();
                getTabs().addAll(newValue.stream().map(sheet -> new SheetTab(sheet, cerbo)).toList());
            });
        });
    }
    
    public void addEmptySheet() {
        Snippet snippet = new Snippet(getUsableName());
        snippets.getSnippetsProperty().add(snippet);
    }

    private String getUsableName() {
        AtomicInteger num = new AtomicInteger(0);
        String name;
        do {
            num.incrementAndGet();
            name = "Untitled " + num;
        }
        while (getTabs().stream().anyMatch(tab -> tab.getText().equals("Untitled " + num)));
        return name;
    }

    public SheetTab getSelectedSheetTab() {
        return (SheetTab) getSelectionModel().getSelectedItem();
    }
}
