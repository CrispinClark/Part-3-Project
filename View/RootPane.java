/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Control.Controller;
import Model.Agent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Crispin
 */
public class RootPane extends BorderPane
{
    final private Controller control;
    final private GridPane mainPane;
    
    final private Label popSizeLab, evoTypeLab, gameNoLab, clearLab;
    final private TextField popSizeField, gameNoField;
    final private ComboBox<String> evoTypeCombo;
    final private CheckBox vendettasCheck, scoreCheck;
    
    final private Label suckLab, tempLab, rewLab, punLab;
    final private TextField suckField, tempField, rewField, punField;
    
    final private ScrollPane stratScroll;
    final private Label stratTitle;
    final private CheckBox coop, def, t4tp, alt, ran, t4ti;
    
    final private Button runBtn;
    
    ObservableList<String> evoOptions = 
    FXCollections.observableArrayList(
        "Trim",
        "Tournament",
        "Playoff"
    );
    
    public RootPane(Controller control)
    {
        this.control = control;
        
        //INITIALISE THE COMPONENTS
        mainPane = new GridPane();
        stratScroll = new ScrollPane();
        
        popSizeLab = new Label("Population Size:"); popSizeField = new TextField();
        evoTypeLab = new Label("Evolution Type:");  evoTypeCombo = new ComboBox<>(evoOptions); evoTypeCombo.setValue(evoOptions.get(0));
        gameNoLab = new Label("Number of Games:");  gameNoField = new TextField();
        clearLab = new Label("Clear:"); vendettasCheck = new CheckBox("Vendettas"); scoreCheck = new CheckBox("Score");
        
        stratTitle = new Label("SELECT STRATEGIES");
        coop = new CheckBox("Always Cooperate");
        coop.setSelected(true);
        def = new CheckBox("Always Defect");
        def.setSelected(true);
        t4tp = new CheckBox("Tit for Tat (Personal)");
        t4tp.setSelected(true);
        t4ti = new CheckBox("Tit for Tat (Impersonal)");
        t4ti.setSelected(true);
        alt = new CheckBox("Alternate");
        alt.setSelected(true);
        ran = new CheckBox("Random");
        ran.setSelected(true);
        
        suckLab = new Label("Sucker:");
        tempLab = new Label("Temptation:");
        punLab = new Label("Punishment:");
        rewLab = new Label("Reward:");
        
        suckField = new TextField();
        tempField = new TextField();
        punField = new TextField();
        rewField = new TextField();
        
        runBtn = new Button("RUN");
        
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
                    control.runSimulation();
                    runBtn.setDisable(false);
                }
                catch(Exception e)
                {
                    System.err.println(e.getMessage());
                }
            }
        });
    }
    
    private void layoutComponents()
    {
        mainPane.setPadding(new Insets(10));
        mainPane.setHgap(10);
        mainPane.setVgap(10);
        
        popSizeLab.setMinWidth(100);
        popSizeLab.setPadding(new Insets(0, 2, 0, 0));
        popSizeLab.setAlignment(Pos.CENTER_RIGHT);
//        popSizeField.setMinWidth(150);
        
        gameNoLab.setMinWidth(100);
        gameNoLab.setPadding(new Insets(0, 2, 0, 0));
        gameNoLab.setAlignment(Pos.CENTER_RIGHT);
  //      gameNoField.setMinWidth(150);
        
        evoTypeLab.setMinWidth(100);
        evoTypeLab.setPadding(new Insets(0, 2, 0, 0));
        evoTypeLab.setAlignment(Pos.CENTER_RIGHT);
    //    evoTypeCombo.setMinWidth(150);
        
        clearLab.setMinWidth(100);
        clearLab.setPadding(new Insets(0, 2, 0, 0));
        clearLab.setAlignment(Pos.CENTER_RIGHT);
        
        suckLab.setMinWidth(100);
        suckLab.setPadding(new Insets(0, 2, 0, 0));
        suckLab.setAlignment(Pos.CENTER_RIGHT);
        
        tempLab.setMinWidth(100);
        tempLab.setPadding(new Insets(0, 2, 0, 0));
        tempLab.setAlignment(Pos.CENTER_RIGHT);
        
        rewLab.setMinWidth(100);
        rewLab.setPadding(new Insets(0, 2, 0, 0));
        rewLab.setAlignment(Pos.CENTER_RIGHT);
        
        punLab.setMinWidth(100);
        punLab.setPadding(new Insets(0, 2, 0, 0));
        punLab.setAlignment(Pos.CENTER_RIGHT);
        
        stratTitle.setAlignment(Pos.CENTER);
        stratTitle.setMinWidth(180);
        FlowPane fp = createStrategyContentPane();
        fp.setMaxWidth(Double.MAX_VALUE);
        stratScroll.setContent(fp);
        stratScroll.setMinWidth(180);
        stratScroll.setMaxWidth(Double.MAX_VALUE);
        stratScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        runBtn.setMaxWidth(Double.MAX_VALUE);
        
        mainPane.add(popSizeLab, 0, 0, 4, 1);   mainPane.add(getPopSizeField(), 4, 0, 4, 1); mainPane.add(stratTitle, 12, 0, 6, 1);
        mainPane.add(gameNoLab, 0, 1, 4, 1);    mainPane.add(getGameNoField(), 4, 1, 4, 1);  mainPane.add(stratScroll, 12, 1, 6, 6);
        mainPane.add(evoTypeLab, 0, 2, 4, 1);   mainPane.add(evoTypeCombo, 4, 2, 4, 1);
        mainPane.add(clearLab, 0, 3, 2, 1); mainPane.add(vendettasCheck, 2, 3, 3, 1);   mainPane.add(scoreCheck, 5, 3, 3, 1);
        mainPane.add(suckLab, 0, 4, 4, 1);  mainPane.add(suckField, 4, 4, 4, 1);
        mainPane.add(rewLab, 0, 5, 4, 1);  mainPane.add(rewField, 4, 5, 4, 1);
        mainPane.add(punLab, 0, 6, 4, 1);  mainPane.add(punField, 4, 6, 4, 1);
        mainPane.add(tempLab, 0, 7, 4, 1);  mainPane.add(tempField, 4, 7, 4, 1);
        mainPane.add(runBtn, 12, 7, 6, 1);
        
        this.setCenter(mainPane);
        
        BorderPane.setAlignment(mainPane, Pos.CENTER);
    }
    
    private FlowPane createStrategyContentPane()
    {
        FlowPane fp = new FlowPane(Orientation.VERTICAL);
        fp.getChildren().addAll(alt, coop, def, t4tp, t4ti, ran);
        
        return fp;
    }

    public void fillDefaults()
    {
        popSizeField.setText(String.valueOf(control.getPopulationSize()));
        gameNoField.setText(String.valueOf(control.getNoOfGames()));
        
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
        
        control.setClearScores(scoreCheck.isSelected());
        control.setClearVendettas(vendettasCheck.isSelected());
        
        if (error)
        {
            runBtn.setDisable(false);
            throw new Exception("Variables input incorrectly");
        }
        
    }
    
    public void setAvailableStrategies()
    {
        control.clearStrategies();
        if (coop.isSelected())
        {
            control.addStrategy(Model.Agent.Strategy.ALWAYS_COOPERATE);
        }
        if (def.isSelected())
        {
            control.addStrategy(Model.Agent.Strategy.ALWAYS_DEFECT);
        }
        if (t4tp.isSelected())
        {
            control.addStrategy(Model.Agent.Strategy.TIT_FOR_TAT_PERSONAL);
        }
        if (t4ti.isSelected())
        {
            control.addStrategy(Model.Agent.Strategy.TIT_FOR_TAT_IMPERSONAL);
        }
        if (alt.isSelected())
        {
            control.addStrategy(Model.Agent.Strategy.ALTERNATE);
        }
        if (ran.isSelected())
        {
            control.addStrategy(Model.Agent.Strategy.RANDOM);
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
}
