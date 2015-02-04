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
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Crispin
 */
public class StrategiesPane extends FlowPane
{
    final private Label title;
    final private CheckBox coop, def, t4tp, alt;     
    
    public StrategiesPane()
    {
        title = new Label("SELECT STRATEGIES");
        
        coop = new CheckBox("Always Cooperate");
        coop.setSelected(true);
        def = new CheckBox("Always Defect");
        def.setSelected(true);
        t4tp = new CheckBox("Tit for Tat (Personal)");
        t4tp.setSelected(true);
        alt = new CheckBox("Alternate");
        alt.setSelected(true);
    }
    
    public void layoutComponents()
    {
        this.setOrientation(Orientation.VERTICAL);
        this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setPadding(new Insets(5));
        
        title.setAlignment(Pos.CENTER);
        
        coop.setTextAlignment(TextAlignment.LEFT);
        coop.setPadding(new Insets(5));
        def.setTextAlignment(TextAlignment.LEFT);
        def.setPadding(new Insets(5));
        t4tp.setTextAlignment(TextAlignment.LEFT);
        t4tp.setPadding(new Insets(5));
        alt.setTextAlignment(TextAlignment.LEFT);
        alt.setPadding(new Insets(5));
        
        this.getChildren().addAll(title, coop, def, t4tp, alt);
    }
}
