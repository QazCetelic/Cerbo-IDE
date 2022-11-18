package qaz.code.view;

import javafx.scene.control.Tab;
import qaz.code.Cerbo;
import qaz.code.model.Snippet;

public class SheetTab extends Tab {
    private final Snippet snippet;
    public SheetTab(Snippet snippet, Cerbo cerbo) {
        super(snippet.getNameProperty().get(), null);
        this.snippet = snippet;
        setContent(new SheetPane(snippet, cerbo));
        setOnClosed(event -> cerbo.snippets.getSnippetsProperty().remove(snippet));
    }
    
    public Snippet getSheet() {
        return snippet;
    }
    
    public SheetPane getSheetView() {
        return (SheetPane) getContent();
    }
}
