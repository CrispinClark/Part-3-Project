/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Model.Agent.Strategy;
import Model.PopulationModel;
import View.RootPane;

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
    
    public void setClearVendettas(Boolean b)
    {
        model.setClearVendettas(b);
    }
    
    public void setClearScores(Boolean b)
    {
        model.setClearScores(b);
    }
    
    public void clearStrategies()
    {
        model.clearStrategies();
    }
    
    public void addStrategy(Strategy strategy)
    {
        model.addStrategy(strategy);
    }
}