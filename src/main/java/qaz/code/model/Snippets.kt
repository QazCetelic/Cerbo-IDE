package qaz.code.model

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SetProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleSetProperty
import javafx.collections.FXCollections

class Snippets {
    val snippetsProperty: SetProperty<Snippet> = SimpleSetProperty(FXCollections.observableSet(HashSet()))
    val selectedSnippetProperty: ObjectProperty<Snippet> = SimpleObjectProperty<Snippet>().apply {
        addListener { _, oldValue, newValue ->
            // Ensure that the sheet is in the set of sheets.
            if (newValue !in snippetsProperty) {
                snippetsProperty.add(newValue)
            }

            println("selectedSheetProperty changed from ${oldValue?.nameProperty?.get() ?: "null"} to ${newValue?.nameProperty?.get() ?: "null"}")
        }
    }
}