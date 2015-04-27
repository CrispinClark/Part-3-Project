/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Model.PopulationModel;
import Model.PopulationModel.Strategy;
import View.RootPane;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Crispin
 */
public class Controller 
{
    private RootPane view;
    private PopulationModel model;
    
    public void runSimulation()
    {
        model.runSimulation();
    }
    
    public void setView(RootPane view)
    {
        this.view = view;
    }
    
    public void setModel(PopulationModel model)
    {
        this.model = model;
    }
    
    public void setPopulationSize(int populationSize) throws Exception
    {
        if (populationSize < 1)
            throw new Exception("Population Size must be greater than 0");
                 
        model.setPopulationSize(populationSize);
    }
    
    public int getPopulationSize()
    {
        if (model == null)
            System.out.println("model is null");
        return model.getPopulationSize();
    }
    
    public void setNoOfGames(int noOfGames) throws Exception
    {
        if (noOfGames < 1)
            throw new Exception("Number of Games must be greater than 0");
        
        model.setNoOfGames(noOfGames);
    }
    
    public int getNoOfGames()
    {
        return model.getNoOfGames();
    }
    
    public void setStopLevel(int stopLevel) throws Exception
    {
        if (stopLevel < 1)
            throw new Exception("Stop level must be greater than 0");
        else
            model.setStopLevel(stopLevel);
    }
    
    public int getStopLevel()
    {
        return model.getStopLevel();
    }
    
    public void setTerminate(int terminate) throws Exception
    {
        if (terminate < 1)
            throw new Exception("Terminate must be greater than 0");
        else
            model.setTerminate(terminate);
    }
    
    public int getTerminate()
    {
        return model.getTerminate();
    }
    
    public void setTemptation(int i) throws Exception
    {
        if (i < 0)
            throw new Exception("Temptation must be at least 0");
        
        model.setTemptation(i);
    }
    
    public int getTemptation()
    {
        return model.getTemptation();
    }
    
    public void setReward(int i) throws Exception
    {
        if (i < 0)
            throw new Exception("Reward must be at least 0");
        
        model.setReward(i);
    }
    
    public int getReward()
    {
        return model.getReward();
    }
    
    public void setSucker(int i) throws Exception
    {
        if (i < 0)
            throw new Exception("Sucker must be at least 0");
        
        model.setSucker(i);
    }
    
    public int getSucker()
    {
        return model.getSucker();
    }
    
    public void setPunishment(int i) throws Exception
    {
        if (i < 0)
            throw new Exception("Punishment must be at least 0");
        
        model.setPunishment(i);
    }
    
    public int getPunishment()
    {
        return model.getPunishment();
    }
    
    public void setEvoType(String s) throws Exception
    {
        try
        {
            model.setEvoType(s);
        }        
        catch (Exception e)
        {
            throw new Exception("Controller: error setting evo type");
        }
    }
    
/*    public void setClearVendettas(Boolean b)
    {
        model.setClearVendettas(b);
    }
    
    public void setClearScores(Boolean b)
    {
        model.setClearScores(b);
    }
  */  
    public void clearStrategies()
    {
        model.clearStrategies();
    }
    
    public void addStrategy(Strategy strategy)
    {
        model.addStrategy(strategy);
    }

    public void setGraphData(HashMap<Class, ArrayList<Integer>> strategyLevels, int cooperation) 
    {
        view.getChart().getData().clear();
        view.getChart().setTitle("Population Size: " + model.getPopulationSize()
                + "; No of Games: " + model.getNoOfGames()
                + "; P = " + model.getPunishment()
                + "; R = " + model.getReward()
                + "; S = " + model.getSucker()
                + "; T = " + model.getTemptation()
                + "; Final cooperation = " + cooperation);
        ArrayList<XYChart.Series> seriesList = new ArrayList<>();
        
        for (Class s : strategyLevels.keySet())
        {
            XYChart.Series series = new XYChart.Series();
            series.setName(s.getSimpleName());
            
            ArrayList<Integer> values = strategyLevels.get(s);
            for (int i = 0; i < values.size(); i++)
            {
                series.getData().add(new XYChart.Data(i, values.get(i)));
            }
            
            seriesList.add(series);
        }
        
        view.getChart().getData().addAll(seriesList);
        
    }
    
    public void setOffspringGetVendettas(boolean b)
    {
        model.setOffspringGetVendettas(b);
    }
    
    public boolean getOffspringGetVendettas()
    {
        return model.isOffspringGetVendettas();
    }   
}
