/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Control.Controller;
import Model.PopulationModel.Strategy;
import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;

/**
 *
 * @author Crispin
 */
public class RootPane extends BorderPane
{
    final private Controller control;
    final private Window window;
    
    final private FlowPane parametersPane;
    
    final private Label popSizeLab, evoTypeLab, gameNoLab, stopLab, termLab;
    final private TextField popSizeField, gameNoField, stopField, termField;
    final private ComboBox<String> evoTypeCombo;
    
    final private Label suckLab, tempLab, rewLab, punLab;
    final private TextField suckField, tempField, rewField, punField;
    
    final private ScrollPane stratScroll;
    final private Label stratTitle;
    final private CheckBox coop, def, t4tp, ran, unf_p; /*t4ti, unf_i, alt,;*/
    
    final private Button runBtn, saveButton;
    
    final private LineChart<Number, Number> chart;
    final private NumberAxis xAxis, yAxis;
    
    XYChart.Series series;
    
    ObservableList<String> evoOptions = 
    FXCollections.observableArrayList(
        "Trim",
        "Tournament"
    );
    
    public RootPane(Controller control, Window window)
    {
        this.control = control;
        this.window = window;
        
        //INITIALISE THE COMPONENTS
        parametersPane = new FlowPane();
        parametersPane.setPrefWrapLength(Double.MAX_VALUE);
        stratScroll = new ScrollPane();
        
        popSizeLab = new Label("Population Size:");     popSizeField = new TextField();
        evoTypeLab = new Label("Evolution Type:");      evoTypeCombo = new ComboBox<>(evoOptions); evoTypeCombo.setValue(evoOptions.get(0));
        gameNoLab = new Label("Number of Games:");      gameNoField = new TextField();
        stopLab = new Label("Stop if no change after"); stopField = new TextField();
        termLab = new Label("Terminate after:");        termField = new TextField();
        
        stratTitle = new Label("SELECT STRATEGIES");
        coop = new CheckBox("Always Cooperate");
        coop.setSelected(true);
        def = new CheckBox("Always Defect");
        def.setSelected(true);
        t4tp = new CheckBox("Tit for Tat");
        t4tp.setSelected(true);
/*        t4ti = new CheckBox("Tit for Tat (Impersonal)");
        t4ti.setSelected(true);
        alt = new CheckBox("Alternate");
        alt.setSelected(true);*/
        ran = new CheckBox("Random");
        ran.setSelected(true);
/*        unf_i = new CheckBox("Unforgiving (Impersonal)");
        unf_i.setSelected(true);*/
        unf_p = new CheckBox("Unforgiving");
        unf_p.setSelected(true);
        
        suckLab = new Label("Sucker:");
        tempLab = new Label("Temptation:");
        punLab = new Label("Punishment:");
        rewLab = new Label("Reward:");
        
        suckField = new TextField();
        tempField = new TextField();
        punField = new TextField();
        rewField = new TextField();
        
        runBtn = new Button("RUN");
        saveButton = new Button("Export");
        saveButton.setDisable(true);
        
        xAxis = new NumberAxis();
        xAxis.setLabel("Number of evolutions");
        yAxis = new NumberAxis();
        yAxis.setLabel("Size of group");
        
        chart = new LineChart(xAxis, yAxis);
        chart.setTitle("Evolution of strategies");
        chart.setCreateSymbols(false);
        
        addHandlers();
        layoutComponents();
    }
    private void addHandlers()
    {
        runBtn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                try
                {
                    runBtn.setDisable(true);
                    setNewVariables();
                    setAvailableStrategies();
                }
                catch (Exception e)
                {
                    System.err.println(e.getMessage());
                    runBtn.setDisable(false);
                    return;
                }
                
                try
                {
                    control.runSimulation();
                    runBtn.setDisable(false);
                    saveButton.setDisable(false);
                }
                catch(Exception e)
                {
                    System.err.println(e.getMessage());
                }
            }
        });
        
        saveButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    FileChooser fc = new FileChooser();
                    fc.setTitle("Save graph as image");
                    fc.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("PNG", "*.png")
                    );
                    fc.setInitialDirectory(new File("C:/Users/Crispin/Documents/3200 - Individual Project"));
                    
                    File file = fc.showSaveDialog(window);
                    
                    if (file != null)
                    {
                        WritableImage graphImage = chart.snapshot(null, null);
                        ImageIO.write(SwingFXUtils.fromFXImage(graphImage, null), "png", file);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private void layoutComponents()
    {
        parametersPane.setPadding(new Insets(10));
        parametersPane.setHgap(10);
        parametersPane.setVgap(10);
        
        popSizeLab.setMinWidth(100);
        popSizeLab.setPadding(new Insets(0, 2, 0, 0));
        popSizeLab.setAlignment(Pos.CENTER_RIGHT);
        FlowPane popSizePane = new FlowPane();
        popSizePane.getChildren().addAll(popSizeLab, popSizeField);
        
        gameNoLab.setMinWidth(100);
        gameNoLab.setPadding(new Insets(0, 2, 0, 0));
        gameNoLab.setAlignment(Pos.CENTER_RIGHT);
        FlowPane gameNoPane = new FlowPane();
        gameNoPane.getChildren().addAll(gameNoLab, gameNoField);
        
        evoTypeLab.setMinWidth(100);
        evoTypeLab.setPadding(new Insets(0, 2, 0, 0));
        evoTypeLab.setAlignment(Pos.CENTER_RIGHT);
        FlowPane evoTypePane = new FlowPane();
        evoTypePane.getChildren().addAll(evoTypeLab, evoTypeCombo);
        
        stopLab.setMinWidth(100);
        stopLab.setPadding(new Insets(0, 2, 0, 0));
        stopLab.setAlignment(Pos.CENTER_RIGHT);
        FlowPane stopPane = new FlowPane();
        stopPane.getChildren().addAll(stopLab, stopField);
        
        termLab.setMinWidth(100);
        termLab.setPadding(new Insets(0, 2, 0, 0));
        termLab.setAlignment(Pos.CENTER_RIGHT);
        FlowPane termPane = new FlowPane();
        termPane.getChildren().addAll(termLab, termField);
        
        suckLab.setMinWidth(100);
        suckLab.setPadding(new Insets(0, 2, 0, 0));
        suckLab.setAlignment(Pos.CENTER_RIGHT);
        FlowPane suckPane = new FlowPane();
        suckPane.getChildren().addAll(suckLab, suckField);
        
        tempLab.setMinWidth(100);
        tempLab.setPadding(new Insets(0, 2, 0, 0));
        tempLab.setAlignment(Pos.CENTER_RIGHT);
        FlowPane tempPane = new FlowPane();
        tempPane.getChildren().addAll(tempLab, tempField);
        
        rewLab.setMinWidth(100);
        rewLab.setPadding(new Insets(0, 2, 0, 0));
        rewLab.setAlignment(Pos.CENTER_RIGHT);
        FlowPane rewPane = new FlowPane();
        rewPane.getChildren().addAll(rewLab, rewField);
        
        punLab.setMinWidth(100);
        punLab.setPadding(new Insets(0, 2, 0, 0));
        punLab.setAlignment(Pos.CENTER_RIGHT);
        FlowPane punPane = new FlowPane();
        punPane.getChildren().addAll(punLab, punField);
        
        FlowPane scorePane = new FlowPane();
        scorePane.getChildren().addAll(suckPane, rewPane, punPane, tempPane);
        
        FlowPane strategiesPane = new FlowPane();
        strategiesPane.setHgap(5);
        strategiesPane.setVgap(5);
        strategiesPane.getChildren().addAll(coop, def, ran, t4tp, unf_p);
        
        parametersPane.getChildren().addAll(popSizePane,
                                            gameNoPane,
                                            evoTypePane,
                                            stopPane,
                                            termPane,
                                            scorePane,
                                            strategiesPane);
        
        FlowPane btnPanel = new FlowPane();
        btnPanel.getChildren().addAll(runBtn, saveButton);
        btnPanel.setPadding(new Insets(5));
        btnPanel.setHgap(5);
        
        this.setTop(parametersPane);
        this.setCenter(chart);
        this.setBottom(btnPanel);
        btnPanel.setAlignment(Pos.CENTER);
        
        BorderPane.setAlignment(parametersPane, Pos.CENTER);
        BorderPane.setAlignment(btnPanel, Pos.CENTER);
    }
    
    public void fillDefaults()
    {
        popSizeField.setText(String.valueOf(control.getPopulationSize()));
        gameNoField.setText(String.valueOf(control.getNoOfGames()));
        stopField.setText(String.valueOf(control.getStopLevel()));
        termField.setText(String.valueOf(control.getTerminate()));
        
        suckField.setText(String.valueOf(control.getSucker()));
        rewField.setText(String.valueOf(control.getReward()));
        punField.setText(String.valueOf(control.getPunishment()));
        tempField.setText(String.valueOf(control.getTemptation()));
    }
    
    public void setNewVariables() throws Exception
    {
        boolean error = false;
        
        try
        {
            control.setPopulationSize(Integer.parseInt(popSizeField.getText()));
        }
        catch (Exception e)
        {
            popSizeField.setText("1 or more");
            error = true;
        }
        
        try
        {
            control.setNoOfGames(Integer.parseInt(gameNoField.getText()));
        }
        catch (Exception e)
        {
            gameNoField.setText("1 or more");
            error = true;
        }
        
        try
        {
            control.setStopLevel(Integer.parseInt(stopField.getText()));
        }
        catch (Exception e)
        {
            stopField.setText("1 or more");
            error = true;
        }
        
        try
        {
            control.setTerminate(Integer.parseInt(termField.getText()));
        }
        catch (Exception e)
        {
            termField.setText("1 or more");
            error = true;
        }
        
        try
        {
            control.setTemptation(Integer.parseInt(tempField.getText()));
        }
        catch (Exception e)
        {
            tempField.setText("0 or more");
            error = true;
        }
        
        try
        {
            control.setReward(Integer.parseInt(rewField.getText()));
        }
        catch (Exception e)
        {
            rewField.setText("0 or more");
            error = true;
        }
        
        try
        {
            control.setPunishment(Integer.parseInt(punField.getText()));
        }
        catch (Exception e)
        {
            punField.setText("0 or more");
            error = true;
        }
        
        try
        {
            control.setSucker(Integer.parseInt(suckField.getText()));
        }
        catch (Exception e)
        {
            suckField.setText("0 or more");
            error = true;
        }
        
        switch (evoTypeCombo.getValue()) {
            case "Trim":
                control.setEvoType("Trim");
                break;
            case "Tournament":
                control.setEvoType("Tournament");
                break;
            case "Playoff":
                control.setEvoType("Playoff");
                break;
        }
        
/*        control.setClearScores(scoreCheck.isSelected());
        control.setClearVendettas(vendettasCheck.isSelected());
*/        
        if (error)
        {
            runBtn.setDisable(false);
            throw new Exception("Variables input incorrectly");
        }
        
    }
    
    public void setAvailableStrategies() throws Exception
    {
        boolean somethingSelected = false;
        
        control.clearStrategies();
        if (coop.isSelected())
        {
            control.addStrategy(Strategy.ALWAYS_COOPERATE);
            somethingSelected = true;
        }
        if (def.isSelected())
        {
            control.addStrategy(Strategy.ALWAYS_DEFECT);
            somethingSelected = true;
        }
        if (t4tp.isSelected())
        {
            control.addStrategy(Strategy.TIT_FOR_TAT_PERSONAL);
            somethingSelected = true;
        }
        if (ran.isSelected())
        {
            control.addStrategy(Strategy.RANDOM);
            somethingSelected = true;
        }
        if (unf_p.isSelected())
        {
            control.addStrategy(Strategy.UNFORGIVING_PERSONAL);
            somethingSelected = true;
        }
        /*if (t4ti.isSelected())
        {
            control.addStrategy(Model.Agent.Strategy.TIT_FOR_TAT_IMPERSONAL);
            somethingSelected = true;
        }
        if (alt.isSelected())
        {
            control.addStrategy(Model.Agent.Strategy.ALTERNATE);
            somethingSelected = true;
        }*/
        /*if (unf_i.isSelected())
        {
            control.addStrategy(Model.Agent.Strategy.UNFORGIVING_IMPERSONAL);
            somethingSelected = true;
        }*/
        
        if (!somethingSelected)
        {
            throw new Exception("No strategies selected!");
        }
    }
    
    /**
     * @return the popSizeField
     */
    public TextField getPopSizeField() {
        return popSizeField;
    }

    /**
     * @return the gameNoField
     */
    public TextField getGameNoField() {
        return gameNoField;
    }
    
    public XYChart getChart()
    {
        return chart;
    }
}
