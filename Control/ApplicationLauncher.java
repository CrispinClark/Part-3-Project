/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

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
        
        RootPane root = new RootPane();
        
        primaryStage.setScene(new Scene(root, 500, 250));
        primaryStage.show();
    }
}
