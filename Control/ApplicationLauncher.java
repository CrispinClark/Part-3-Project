
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Model.PopulationModel;
import View.RootPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class ApplicationLauncher extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game Theory Simulation");
        
        Controller control = new Controller();
        RootPane view = new RootPane(control, primaryStage);
        PopulationModel model = new PopulationModel(control);        
        
        control.setView(view);
        control.setModel(model);
        
        primaryStage.setScene(new Scene(view, 1000, 600));
        primaryStage.show();
        
        view.fillDefaults();
    }
}
