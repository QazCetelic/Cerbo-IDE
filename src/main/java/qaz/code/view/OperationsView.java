package qaz.code.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import qaz.code.model.*;

import java.util.ArrayList;

public class OperationsView extends BorderPane {
    private final PieChart operationsDistributionChart;
    
    public OperationsView(Snippet snippet) {
        operationsDistributionChart = new PieChart();
        operationsDistributionChart.setTitle(null);
        operationsDistributionChart.setStartAngle(90);
        operationsDistributionChart.setLegendVisible(false);
    
        snippet.getLastResultProperty().addListener((observable, oldValue, newValue) -> {
            Result result = snippet.getLastResultProperty().get();
            if (result == null) {
                Platform.runLater(() -> setTop(new Label("No results")));
            }
            else {
                Execution execution = result.getExecution();
                ObservableList<PieChart.Data> list = FXCollections.observableArrayList(new ArrayList<>());
    
                StringBuilder tooltipText = new StringBuilder().append("Operations:");
                // Add the amount of times an operator was used to the data for each operator
                for (Operator operator : execution.getOperationsCounter().keySet()) {
                    String charString = Character.toString(operator.getChar());
                    int count = execution.getOperationsCounter().get(operator);
                    list.add(new PieChart.Data(charString, count));
                    tooltipText.append('\n').append(operator).append(": ").append(count);
                }
                // Remove the entries for operators that were not used to prevent chart clutter
                list.removeIf(data -> data.getPieValue() == 0);
                
                // Operations:
                // INCREMENT: 4
                // OUTPUT: 3
                Tooltip tooltip = new Tooltip(tooltipText.toString());
                Tooltip.install(operationsDistributionChart, tooltip);
                
                Platform.runLater(() -> {
                    String text = execution.getOperations() + " operations, " + snippet.getApproximateLoopsProperty().get() + " loops";
                    Label operationsLabel = new Label(text);
                    operationsLabel.setPadding(new Insets(0, 0, 0, 5));
                    setTop(operationsLabel);
                    setCenter(operationsDistributionChart);
                    operationsDistributionChart.dataProperty().set(list);
                });
            }
        });
    }
}
