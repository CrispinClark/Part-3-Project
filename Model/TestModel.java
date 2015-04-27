/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Control.Controller;
import Model.Agents.CooperateAgent;
import Model.Agents.DefectorAgent;
import Model.Agents.RandomAgent;
import Model.Agents.TitForTatAgent;
import Model.Agents.UnforgivingAgent;
import View.RootPane;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Crispin
 */
public class TestModel extends Application
{
    static Controller control = new Controller();    
    static PopulationModel model = new PopulationModel(control);
    
    static int noOfRuns = 100;
    
    static String evo = "Tournament";
    static int popSize = 100;
    static int terminate = 1000000;
    static int noOfGames = 1000;
    static int stopLevel = 100;
    static boolean vendettas = false;          
    
    static int punishment = 1;
    static int reward = 3;
    static int temptation = 5;
    static int sucker = 0;
    
    static HashMap<String, ArrayList<Integer>> finalLevels = new HashMap<>();
    
    public static void main(String[] args)
    {
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
        
        try
        {
            model.setEvoType(evo);
            model.setNoOfGames(noOfGames);
            model.setPopulationSize(popSize);
            model.setTerminate(terminate);
            model.setStopLevel(stopLevel);
            model.setOffspringGetVendettas(vendettas);

            finalLevels.put(RandomAgent.class.getSimpleName().replaceAll("Agent", ""), new ArrayList<>());
            finalLevels.put(TitForTatAgent.class.getSimpleName().replaceAll("Agent", ""), new ArrayList<>());
            finalLevels.put(CooperateAgent.class.getSimpleName().replaceAll("Agent", ""), new ArrayList<>());
            finalLevels.put(DefectorAgent.class.getSimpleName().replaceAll("Agent", ""), new ArrayList<>());
            finalLevels.put(UnforgivingAgent.class.getSimpleName().replaceAll("Agent", ""), new ArrayList<>());
                
            for (int i = 1; i <= noOfRuns; i++)
            {
                System.out.println(i);
                
                model.addStrategy(PopulationModel.Strategy.RANDOM);
                model.addStrategy(PopulationModel.Strategy.UNFORGIVING_PERSONAL);
                model.addStrategy(PopulationModel.Strategy.TIT_FOR_TAT_PERSONAL);
                model.addStrategy(PopulationModel.Strategy.ALWAYS_COOPERATE);
                model.addStrategy(PopulationModel.Strategy.ALWAYS_DEFECT);
                
                HashMap<Class, ArrayList<Integer>> strategyLevels = model.runSimulation(true);
                
                for (Class c : strategyLevels.keySet())
                {
                    ArrayList<Integer> al = finalLevels.get(c.getSimpleName().replaceAll("Agent", ""));
                    ArrayList<Integer> res = strategyLevels.get(c);
                    
                    int x = res.size() - 1;
                    
                    al.add(res.get(x));
                    
                    System.out.println(c.getSimpleName()+ "= " + strategyLevels.get(c).get(strategyLevels.get(c).size()-1));
                }
                
                model.resetValues();
            }
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
        
        String path = "C:\\Users\\Crispin\\Dropbox\\3200 - Part III Project\\results.csv";
        try 
        {
            FileWriter writer;
            writer = new FileWriter(path, false);  //True = Append to file, false = Overwrite
            FileWriter matlabWriter = new FileWriter("C:\\Users\\Crispin\\Dropbox\\3200 - Part III Project\\resultsMatlab.csv", false);
            
            String title = "Evolution " + evo + ". PopSize " +popSize+ ". NoOfGames "+
                    noOfGames+ ". Terminate "+terminate+ ". StopLevel "+stopLevel+ ". Vendettas "+vendettas;
            
            writer.write(title + ",\r\n");
            
            // Write CSV
            for (String s : finalLevels.keySet())
            {
                ArrayList<Integer> myArrList = finalLevels.get(s);
                
                System.out.println(myArrList.size());
                
                writer.write(s+",");
                matlabWriter.write(s+",");
                
                float total = 0;
                for (Integer myArrList1 : myArrList) {
                    total += myArrList1;
                    
                    matlabWriter.write(myArrList1.toString());
                    matlabWriter.write(",");
                            
                }
                
                writer.write((total/myArrList.size()) + ",\r\n");
                matlabWriter.write("\r\n");
            }

            System.out.println("Write success!");
            writer.close();
            matlabWriter.close();

            } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
