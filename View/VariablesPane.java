/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

/**
 *
 * @author Crispin
 */
public class VariablesPane extends FlowPane
{
    final private Label popSizeLab, evoTypeLab, gameNoLab, clearLab;
    final private TextField popSizeField, gameNoField;
    final private ComboBox<String> evoTypeCombo;
    final private CheckBox vendettasCheck, scoreCheck;
    
    final private FlowPane popSizePane, evoTypePane, gameNoPane, 
            clearPane;
    
    public VariablesPane()
    {
        setOrientation(Orientation.VERTICAL);
        setAlignment(Pos.CENTER);
        
        popSizeLab = new Label("Population Size:");
        evoTypeLab = new Label("Evolution Type:");
        gameNoLab = new Label("Number of Games:");
        clearLab = new Label("Clear:");
        
        popSizeField = new TextField();
        gameNoField = new TextField();
        
        evoTypeCombo = new ComboBox<>();
        
        vendettasCheck = new CheckBox("Vendettas");
        scoreCheck = new CheckBox("Score");
        
        popSizePane = new FlowPane();        
        evoTypePane = new FlowPane();        
        gameNoPane = new FlowPane();        
        clearPane = new FlowPane();
    }
    
    public void layoutComponents()
    {
        popSizeLab.setMinWidth(100);
        popSizeLab.setPadding(new Insets(0, 2, 0, 0));
        popSizeLab.setAlignment(Pos.CENTER_RIGHT);
        evoTypeLab.setMinWidth(100);
        evoTypeLab.setPadding(new Insets(0, 2, 0, 0));
        evoTypeLab.setAlignment(Pos.CENTER_RIGHT);
        gameNoLab.setMinWidth(100);
        gameNoLab.setPadding(new Insets(0, 2, 0, 0));
        gameNoLab.setAlignment(Pos.CENTER_RIGHT);
        clearLab.setMinWidth(100);
        clearLab.setPadding(new Insets(0, 2, 0, 0));
        clearLab.setAlignment(Pos.CENTER_RIGHT);
        
        popSizePane.setPadding(new Insets(5));
        popSizePane.setHgap(10);
        popSizePane.getChildren().addAll(popSizeLab, popSizeField);
        
        evoTypePane.setPadding(new Insets(5));
        evoTypePane.setHgap(10);
        evoTypePane.getChildren().addAll(evoTypeLab, evoTypeCombo);
        
        gameNoPane.setPadding(new Insets(5));
        gameNoPane.setHgap(10);
        gameNoPane.getChildren().addAll(gameNoLab, gameNoField);
        
        clearPane.setPadding(new Insets(5));
        clearPane.setHgap(10);
        clearPane.getChildren().addAll(clearLab, vendettasCheck, scoreCheck);
        
        this.setVgap(10);
        this.getChildren().addAll(popSizePane, evoTypePane, gameNoPane, clearPane);
    }
}
