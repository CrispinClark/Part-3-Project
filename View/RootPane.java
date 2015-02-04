/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Crispin
 */
public class RootPane extends BorderPane
{
    final private GridPane mainPane;
    
    final private VariablesPane varPane;
    final private StrategiesPane stratPane;
    
    final private Button runBtn;
    
    public RootPane()
    {
        //INITIALISE THE COMPONENTS
        mainPane = new GridPane();
        varPane = new VariablesPane();
        varPane.layoutComponents();
        
        stratPane = new StrategiesPane();
        stratPane.layoutComponents();
        
        runBtn = new Button("RUN");
        
        addHandlers();
        layoutComponents();
    }
    private void addHandlers()
    {
        runBtn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
    }
    
    private void layoutComponents()
    {
        mainPane.add(varPane, 0, 0, 2, 1);
        mainPane.add(stratPane, 2, 0, 1, 1);
        
        this.setCenter(mainPane);
        this.setBottom(runBtn);
    }
}
