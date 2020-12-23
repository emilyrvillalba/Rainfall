package sample;

import com.sun.media.sound.InvalidDataException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.border.Border;


public class Main extends Application {
    BorderPane pane = new BorderPane();

    XYChart<String,Number> chart;
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();

    int janRainfall;
    int febRainfall;
    int marRainfall;
    int aprRainfall;

    boolean janValid;
    boolean febValid;
    boolean marValid;
    boolean aprValid;


    TextField januaryField = new TextField();
    TextField februaryField = new TextField();
    TextField marchField = new TextField();
    TextField aprilField = new TextField();

    Button submitButton = new Button("Submit");

    @Override
    public void start(Stage primaryStage) throws Exception{

        Label titleLabel = new Label("Rainfall during Month");
        titleLabel.setStyle("-fx-font-size: 15pt");
        Label instructionsLabel = new Label("Enter amount of rainfall in inches as an integer.");
        instructionsLabel.setStyle("-fx-font-size: 8pt");
        Label januaryLabel = new Label("January: ");
        Label februaryLabel = new Label("February: ");
        Label marchLabel = new Label("March: ");
        Label aprilLabel = new Label("April: ");

        Tooltip inputTip =  new Tooltip("Please enter an integer value.");
        januaryField.setTooltip(inputTip);
        februaryField.setTooltip(inputTip);
        marchField.setTooltip(inputTip);
        aprilField.setTooltip(inputTip);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.addRow(0, januaryLabel, januaryField);
        grid.addRow(1, februaryLabel, februaryField);
        grid.addRow(2, marchLabel, marchField);
        grid.addRow(3, aprilLabel, aprilField);

        VBox dataEntryPage = new VBox(titleLabel, instructionsLabel, grid,submitButton);
        dataEntryPage.setAlignment(Pos.CENTER);
        dataEntryPage.setPadding(new Insets(15));

        pane.setCenter(dataEntryPage);

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        Menu viewMenu = new Menu("View");

        menuBar.getMenus().addAll(fileMenu,viewMenu);
        pane.setTop(menuBar);

        //create file menu's items and accelerators
        MenuItem newItem = new MenuItem("New");
        newItem.setAccelerator(KeyCombination.keyCombination("ctrl+N"));
        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setAccelerator(KeyCombination.keyCombination("ctrl+Q"));
        fileMenu.getItems().addAll(newItem, new SeparatorMenuItem(), quitItem);

        //create view menu's items and accelerators
        RadioMenuItem barItem = new RadioMenuItem("Bar Chart");
        RadioMenuItem areaItem = new RadioMenuItem("Area Chart");
        RadioMenuItem scatterItem = new RadioMenuItem("Scatter Chart");
        barItem.setAccelerator(KeyCombination.keyCombination("ctrl+B"));
        areaItem.setAccelerator(KeyCombination.keyCombination("ctrl+A"));
        scatterItem.setAccelerator(KeyCombination.keyCombination("ctrl+S"));

        viewMenu.getItems().addAll(barItem, areaItem, scatterItem);
        viewMenu.setDisable(true);

        ToggleGroup charts = new ToggleGroup();
        barItem.setToggleGroup(charts);
        areaItem.setToggleGroup(charts);
        scatterItem.setToggleGroup(charts);

        TextFieldListener listener = new TextFieldListener();

        //makes the border red if the input is anything other than an integer
        januaryField.textProperty().addListener(listener);
        januaryField.textProperty().addListener((source, o ,n) ->{
            if(!janValid)
                januaryField.setStyle("-fx-border-color: red");
            else
                januaryField.setStyle("-fx-border-color: transparent");

        });
        februaryField.textProperty().addListener(listener);
        februaryField.textProperty().addListener((source, o ,n) ->{
            if(!febValid)
                februaryField.setStyle("-fx-border-color: red");
            else
                februaryField.setStyle("-fx-border-color: transparent");
        });
        marchField.textProperty().addListener(listener);
        marchField.textProperty().addListener((source, o ,n) ->{
            if(!marValid)
                marchField.setStyle("-fx-border-color: red");
            else
                marchField.setStyle("-fx-border-color: transparent");
        });
        aprilField.textProperty().addListener(listener);
        aprilField.textProperty().addListener((source, o ,n) ->{
            if(!aprValid)
                aprilField.setStyle("-fx-border-color: red");
            else
                aprilField.setStyle("-fx-border-color: transparent");
        });

        //disables the submit button until the listener makes sure all inputs are valid and not empty
        submitButton.setDisable(true);
        submitButton.setOnAction(event -> {
                janRainfall = Integer.parseInt(januaryField.getText());
                febRainfall = Integer.parseInt(februaryField.getText());
                marRainfall = Integer.parseInt(marchField.getText());
                aprRainfall = Integer.parseInt(aprilField.getText());

                barItem.setSelected(true);
                chart = new BarChart<>(xAxis, yAxis);
                createChart(janRainfall, febRainfall, marRainfall, aprRainfall);

                pane.setCenter(chart);
                viewMenu.setDisable(false);
        });

        newItem.setOnAction(event -> {
            januaryField.setText("");
            februaryField.setText("");
            marchField.setText("");
            aprilField.setText("");
            pane.setCenter(dataEntryPage);
            viewMenu.setDisable(true);
            submitButton.setDisable(true);

        });

        quitItem.setOnAction(event -> {
            primaryStage.close();
        });

        barItem.setOnAction(event -> {
            chart = new BarChart<>(xAxis, yAxis);
            createChart(janRainfall, febRainfall, marRainfall, aprRainfall);
        });

        areaItem.setOnAction(event -> {
            chart = new AreaChart<>(xAxis, yAxis);
            createChart(janRainfall, febRainfall, marRainfall, aprRainfall);
        });

        scatterItem.setOnAction(event -> {
            chart = new ScatterChart<>(xAxis, yAxis);
            createChart(janRainfall, febRainfall, marRainfall, aprRainfall);
        });

        Scene myScene = new Scene(pane);
        myScene.getStylesheets().add("mystyles.css");
        primaryStage.setScene(myScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void createChart(int janRainfall, int febRainfall, int marRainfall, int aprRainfall) {

        xAxis.setLabel("Months");
        yAxis.setLabel("Rainfall (inches)");

        chart.setTitle("Rainfall by Month");

        XYChart.Series<String, Number> points = new XYChart.Series<>();

        points.getData().add( new XYChart.Data<>("January", janRainfall));
        points.getData().add( new XYChart.Data<>("February", febRainfall));
        points.getData().add( new XYChart.Data<>("March", marRainfall));
        points.getData().add( new XYChart.Data<>("April", aprRainfall));
        chart.getData().add(points);
        chart.setLegendVisible( false);
        pane.setCenter(chart);
    }

    private class TextFieldListener implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> source, String oldValue, String newValue)   {

            try {
                if (!januaryField.getText().equals("")) {
                    janRainfall = Integer.parseInt(januaryField.getText());
                }
                janValid = true;
            } catch (Exception e) { janValid = false; }

            try {
                if (!februaryField.getText().equals("")) {
                    febRainfall = Integer.parseInt(februaryField.getText());
                }
                febValid = true;
            } catch (Exception e) { febValid = false; }

            try {
                if (!marchField.getText().equals("")) {
                    marRainfall = Integer.parseInt(marchField.getText());
                }
                marValid = true;
            } catch (Exception e) { marValid = false; }

            try {
                if (!aprilField.getText().equals("")) {
                    aprRainfall = Integer.parseInt(aprilField.getText());
                }
                aprValid = true;
            } catch (Exception e) { aprValid = false; }


            if (!janValid|| !febValid || !marValid || !aprValid ||
                    januaryField.getText().equals("") || februaryField.getText().equals("") || marchField.getText().equals("") || aprilField.getText().equals("") ){
                submitButton.setDisable(true);}
            else
                submitButton.setDisable(false);
        }
    };

}
