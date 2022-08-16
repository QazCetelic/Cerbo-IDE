package qaz.code.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import qaz.code.model.Execution;
import qaz.code.model.Result;
import qaz.code.model.Sheet;

import java.util.List;

public class OperationsView extends BorderPane {
    private final PieChart operationsDistributionChart;
    
    public OperationsView(Sheet sheet) {
        operationsDistributionChart = new PieChart();
        operationsDistributionChart.setTitle(null);
        operationsDistributionChart.setStartAngle(90);
        operationsDistributionChart.setLegendVisible(false);
    
        sheet.lastResultProperty().addListener((observable, oldValue, newValue) -> {
            Result result = sheet.lastResultProperty().get();
            if (result == null) {
                Platform.runLater(() -> setCenter(new Label("No results")));
            }
            else {
                Execution execution = result.execution;
                Platform.runLater(() -> {
                    setCenter(operationsDistributionChart);
                    operationsDistributionChart.dataProperty().set(FXCollections.observableList(List.of(
                            new PieChart.Data("<", execution.getOperationsMoveLeft()),
                            new PieChart.Data(">", execution.getOperationsMoveRight()),
                            new PieChart.Data("+", execution.getOperationsIncrease()),
                            new PieChart.Data("-", execution.getOperationsDecrease()),
                            new PieChart.Data("[", execution.getOperationsLeftLoop()),
                            new PieChart.Data("]", execution.getOperationsRightLoop()),
                            new PieChart.Data(".", execution.getOperationsInput()),
                            new PieChart.Data(",", execution.getOperationsOutput())
                    )));
                });
            }
        });
    }
}
