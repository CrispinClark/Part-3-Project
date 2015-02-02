/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;

/**
 *
 * @author Crispin
 */
public class Agent implements Comparable
{    
    public enum Strategy{ALWAYS_DEFECT, ALWAYS_COOPERATE, TIT_FOR_TAT_PERSONAL,
                            ALTERNATE};
    
    private boolean isCooperator;
    private int totalScore;
    private Strategy strategy;
    
    //keep a store of those who have previously defected in a game with the agent
    private ArrayList<Agent> vendettas; 
    
    public Agent(Strategy strategy)
    {
        this.strategy = strategy;
        
        vendettas = new ArrayList<>();
        isCooperator = false;
        totalScore = 0;
    }
    
    public void makeDecision(Agent competitor)
    {
        switch (strategy)
        {
            case ALWAYS_DEFECT:
                //System.out.println("Making decision for always defector");
                isCooperator = false;
                break;
            
                
            case ALWAYS_COOPERATE:
                //System.out.println("Making decision for always cooperator");
                isCooperator = true;
                break;
            
            case TIT_FOR_TAT_PERSONAL:
                //System.out.println("Making decision for tit for tatter");
                isCooperator = !vendettas.contains(competitor);
                break;
                
            case ALTERNATE:
                isCooperator =! isCooperator;
        }
    }
    
    public void addDefector(Agent defector) throws NullPointerException
    {
        if (vendettas == null)
            throw new NullPointerException("Previous defectors list does not exist");
        
        if (defector == null)
            throw new NullPointerException("Trying to add a null agent");
        
        if (!vendettas.contains(defector))
            vendettas.add(defector);
    }
    
    public void removeDefector(Agent defector) throws NullPointerException
    {
        if (vendettas == null)
            throw new NullPointerException("Previous defectors list does not exist");
        
        if (defector == null)
            throw new NullPointerException("Trying to remove a null agent");
        
        vendettas.remove(defector);
    }
    
    public void setStrategy(Strategy newStrategy)
    {
        this.strategy = newStrategy;
    }
    
    public Strategy getStrategy()
    {
        return this.strategy;
    }

    public boolean isCooperator()
    {
        return this.isCooperator;
    }
    
    public void switchCooperator()
    {
        this.isCooperator = this.isCooperator != true;
    }
    
    public void setIsCooperator(boolean isCooperator)
    {
        this.isCooperator = isCooperator;
    }
    
    public void addToTotalScore(int payoff)
    {
        this.totalScore += payoff;
    }
    
    public void setTotalScore(int payoff)
    {
        this.totalScore = payoff;
    }
    
    public void resetScore()
    {
        this.totalScore = 0;
    }
    
    public int getScore()
    {
        return this.totalScore;
    }
    
    public ArrayList<Agent> getVendettas()
    {
        return this.vendettas;
    }
    
    public void clearVendettas()
    {
        vendettas.clear();
    }
    
    @Override
    public int compareTo(Object o) 
    {
        Agent agent = (Agent) o;
        
        if (this.totalScore > agent.getScore())
            return 1;
        else if (this.totalScore < agent.getScore())
            return -1;
        else return 0;
    }
    
}
