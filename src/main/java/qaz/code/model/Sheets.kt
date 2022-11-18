package qaz.code.model

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SetProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleSetProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import java.util.*

class Sheets {
    val sheetsProperty: SetProperty<Sheet> = SimpleSetProperty(FXCollections.observableSet(HashSet()))
    val selectedSheetProperty: ObjectProperty<Sheet> = SimpleObjectProperty<Sheet>().apply {
        addListener { _, oldValue, newValue ->
            // Ensure that the sheet is in the set of sheets.
            if (newValue !in sheetsProperty) {
                sheetsProperty.add(newValue)
            }

            println("selectedSheetProperty changed from ${oldValue?.nameProperty?.get() ?: "null"} to ${newValue?.nameProperty?.get() ?: "null"}")
        }
    }
}