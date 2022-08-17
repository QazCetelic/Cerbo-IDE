package qaz.code.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import qaz.code.model.Execution;
import qaz.code.model.Result;
import qaz.code.model.Sheet;

import java.util.ArrayList;

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
                ObservableList<PieChart.Data> list = FXCollections.observableArrayList(new ArrayList<>());
                list.add(new PieChart.Data("<", execution.getOperationsMoveLeft()));
                list.add(new PieChart.Data(">", execution.getOperationsMoveRight()));
                list.add(new PieChart.Data("+", execution.getOperationsIncrease()));
                list.add(new PieChart.Data("-", execution.getOperationsDecrease()));
                list.add(new PieChart.Data("[", execution.getOperationsLeftLoop()));
                list.add(new PieChart.Data("]", execution.getOperationsRightLoop()));
                list.add(new PieChart.Data(".", execution.getOperationsInput()));
                list.add(new PieChart.Data(",", execution.getOperationsOutput()));
                list.removeIf(data -> data.getPieValue() == 0);
                Platform.runLater(() -> {
                    setCenter(operationsDistributionChart);
                    operationsDistributionChart.dataProperty().set(list);
                });
            }
        });
    }
}